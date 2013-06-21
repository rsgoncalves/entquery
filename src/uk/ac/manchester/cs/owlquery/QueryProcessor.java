package uk.ac.manchester.cs.owlquery;

import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.management.RuntimeErrorException;
import javax.servlet.ServletException;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasoner;
import uk.ac.manchester.cs.jfact.JFactReasoner;
import uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree.AxiomTree;
import uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree.AxiomTreeBuilder;
import uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree.AxiomTreeNode;
import uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree.PairMapping;
import uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree.TreeNodeInfo;
import uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree.TreePair;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxObjectRenderer;
import uk.ac.manchester.cs.owlapi.modularity.ModuleType;
import uk.ac.manchester.cs.owlapi.modularity.SyntacticLocalityModuleExtractor;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

/**
 * Author: 	Rafael Gonçalves<br>
 *          The University Of Manchester<br>
 *          Information Management Group<br>
 * Date: 	25-Jul-2012<br><br>
 */

public class QueryProcessor {

	private OWLOntology ont;
	private OWLOntologyManager man;
	private OWLDataFactory df;
	private String reasonerName;
	private List<OWLAxiom> queries;
	private boolean verbose, inferredOnly, extendedGrammar;
	private ThreadMXBean bean;
	private SyntacticLocalityModuleExtractor bot_extractor, top_extractor, star_extractor;
	
	
	/*
	 * Constructor
	 */
	public QueryProcessor(OWLOntology ont, List<OWLAxiom> queries, String reasonerName, 
			boolean inferredOnly, boolean extendedGrammar, boolean verbose) throws ServletException {
		this.ont = ont;
		this.queries = queries;
		this.verbose = verbose;
		this.inferredOnly = inferredOnly;
		this.extendedGrammar = extendedGrammar;
		this.reasonerName = reasonerName;
		this.man = ont.getOWLOntologyManager();
		this.df = OWLManager.getOWLDataFactory();
		
		bean = ManagementFactory.getThreadMXBean();
		bot_extractor = new SyntacticLocalityModuleExtractor(man, ont, ModuleType.BOT);
		top_extractor = new SyntacticLocalityModuleExtractor(man, ont, ModuleType.TOP);
		star_extractor = new SyntacticLocalityModuleExtractor(man, ont, ModuleType.STAR);
		
		if(reasonerName.equals("Fact")) {
			try {
				addLibraryPath("/Users/rafa/Documents/PhD/workspace/entquery/lib/");
				addLibraryPath("/usr/local/tomcat/webapps/entquery/WEB-INF/lib/");
			} catch (IllegalArgumentException e) {
				throw new ServletException(e);
			} catch (SecurityException e) {
				throw new ServletException(e);
			} catch (IllegalAccessException e) {
				throw new ServletException(e);
			} catch (NoSuchFieldException e) {
				throw new ServletException(e);
			}
		}
	}
	
	
	/*
	 * Adds the specified path to java.library.path
	 */
	private static void addLibraryPath(String pathToAdd) 
			throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException {
	    
		final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
	    usrPathsField.setAccessible(true);
	 
	    // Get array of paths
	    final String[] paths = (String[])usrPathsField.get(null);
	 
	    // Check if the path to add is already present
	    for(String path : paths) {
	        if(path.equals(pathToAdd))
	            return;
	    }
	 
	    // Add the new path
	    final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
	    newPaths[newPaths.length-1] = pathToAdd;
	    usrPathsField.set(null, newPaths);
	}
	
	
	/*
	 * Return the set of axioms evaluated to true (entailed)
	 */
	public List<QueryAnswer> getQueryAnswers() throws OWLOntologyCreationException, ServletException {
		Justifications just = new Justifications();
		OWLReasoner reasoner = createReasoner(reasonerName, ont, false);
		List<QueryAnswer> answers = new ArrayList<QueryAnswer>();
		
		// Process each query
		for(OWLAxiom a : queries) {
			println("---------------------------------------");
			println("Query: " + getManchesterSyntax(a));
			println("---------------------------------------");
			println("Finding candidate answers...\n");

			long start = bean.getCurrentThreadCpuTime();

			CandidateWitnesses cands = getCandidateWitnessAxioms(a);

			Map<OWLAxiom,OWLClassExpression> candidateWits = cands.getCandidates();
			String variable = cands.getVariable();

			long end = bean.getCurrentThreadCpuTime();
			double secs = (end-start)/1000000;
			println("done [" + secs + " ms]");

			// Rule out vacuous witnesses by checking whether 0 |= ax
			Set<OWLAxiom> set = new HashSet<OWLAxiom>();
			Set<OWLEntity> sig = new HashSet<OWLEntity>();

			for(OWLAxiom ax : candidateWits.keySet()) {
				sig.addAll(ax.getSignature());
			}

			for(OWLEntity e : sig) {
				set.add(df.getOWLDeclarationAxiom(e));
			}

			OWLOntology empty = man.createOntology(set);
			OWLReasoner emptyReasoner = createReasoner(reasonerName, empty, false);

			Map<OWLAxiom,Set<Explanation<OWLAxiom>>> justMap = new HashMap<OWLAxiom,Set<Explanation<OWLAxiom>>>();

			println("\nVerifying candidate answers...");
			long start2 = bean.getCurrentThreadCpuTime();

			int trueWitsCounter = 0, asserted = 0;
			for(OWLAxiom ax : candidateWits.keySet()) {
				if(!ont.containsAxiom(ax)) {
					if(!emptyReasoner.isEntailed(ax)) {
						if(reasoner.isEntailed(ax)) {
							OWLOntology axMod = star_extractor.extractAsOntology(ax.getSignature(), 
									IRI.create(UUID.randomUUID().toString()));
							Set<Explanation<OWLAxiom>> justs = just.getExplanations(ax, axMod);
							justMap.put(ax, justs);
							trueWitsCounter++;
						}
					}
				}
				else {
					if(!inferredOnly) {
						justMap.put(ax, new HashSet<Explanation<OWLAxiom>>());
						asserted++;
					}
				}
			}

			print("\tFound " + trueWitsCounter + " answers");
			if(!inferredOnly && asserted > 0) 
				print(" (of which " + asserted + " are asserted)");

			long end2 = bean.getCurrentThreadCpuTime();
			double secs2 = (end2-start2)/1000000;
			println("\ndone [" + secs2 + " ms]");

			QueryAnswer qa = new QueryAnswer(a, variable, justMap, candidateWits);
			double total = secs + secs2;
			qa.setQueryTime(total);
			answers.add(qa);

			emptyReasoner.dispose(); emptyReasoner = null;
			man.removeOntology(empty); empty = null;
		}
		
		return answers;
	}
	
	
	/*
	 * Get the set of candidate witness axioms, where variables are replaced
	 * with expressions over the Grammar Diff grammar.
	 */
	public CandidateWitnesses getCandidateWitnessAxioms(OWLAxiom ax) throws OWLOntologyCreationException {
		
		// Maps candidate witness axioms to the corresponding replaced class or property expression
		Map<OWLAxiom,OWLClassExpression> witnessAxs = new HashMap<OWLAxiom,OWLClassExpression>();

		// Drill down the query to find variables
		AxiomTreeBuilder builder = new AxiomTreeBuilder();
		AxiomTree tree = builder.getAxiomTree(ax);
		
		println("LHS");
		TreeNodeInfo LHS = checkTree(tree.getChild(0));
		boolean fixedLHS = LHS.isFixed();
		
		println("RHS");
		TreeNodeInfo RHS = checkTree(tree.getChild(1));
		boolean fixedRHS = RHS.isFixed();
	
		// vars
		String variable = null;
		OWLClassExpression fixedCe = null;
		OWLOntology module = null;
		TreeNodeInfo toReplace = null;
		Set<OWLClassExpression> fillers = null;
		
		if(fixedLHS) {
			variable = getManchesterSyntax(RHS.getVariable());
			fixedCe = LHS.getClassExpression();
			toReplace = RHS;
			
			// Extract bot-module for class expression
			Set<OWLEntity> modSig = new HashSet<OWLEntity>(); modSig.addAll(LHS.getSignature());
			module = bot_extractor.extractAsOntology(modSig, IRI.create(UUID.randomUUID().toString()));
		}
		else if(fixedRHS) {
			if(ont.getSignature().containsAll(RHS.getSignature())) {
				variable = getManchesterSyntax(LHS.getVariable());
				fixedCe = RHS.getClassExpression();
				toReplace = LHS;
				
				// Extract top-module for class expression
				Set<OWLEntity> modSig = new HashSet<OWLEntity>(); modSig.addAll(RHS.getSignature());
				module = top_extractor.extractAsOntology(modSig, IRI.create(UUID.randomUUID().toString()));
			}
		}
		
		Set<OWLObjectProperty> roles = module.getObjectPropertiesInSignature();
		
		if(module.getLogicalAxiomCount() > 0)
			fillers = getAllCandidates(fixedCe, module, tree, toReplace, roles, true);
		else
			println("\tEmpty module");

		if(fillers != null && !fillers.isEmpty())
			witnessAxs = replaceVariables(tree, toReplace, fillers, roles);
		
		return new CandidateWitnesses(variable, witnessAxs);
	}
	
	
	/* 
	 * Traverse an axiom tree to find whether it is bounded (i.e. contains no variables)
	 */
	private TreeNodeInfo checkTree(AxiomTree tree) {		
		OWLClassExpression ce = null;
		TreeNodeInfo nodeInfo = null;
		
		AxiomTreeNode node = tree.getUserObject();
		if(node.getObject() != null) { 
			if(node.getObject() instanceof OWLClass) {
				OWLClass c = (OWLClass) node.getObject(); ce = c;
				println("  Class: " + getManchesterSyntax(c));
				
				nodeInfo = new TreeNodeInfo(ce);
				nodeInfo.setFixed(true);
				
				if(c.getIRI().toString().contains("http://semanticweb.org/variable")) {
					println("    Variable: " + getManchesterSyntax(c));
					nodeInfo.setFixed(false);
					nodeInfo.addClassVariable(c, tree);
				}
			}
		}
		else if(node.getClassExpression() != null) {
			OWLClassExpression c = (OWLClassExpression) node.getClassExpression(); ce = c;
			nodeInfo = new TreeNodeInfo(ce);
			nodeInfo.setFixed(true);
			
			println("  ClassExpression: " + getManchesterSyntax(ce));
			checkClassExpression(tree, nodeInfo);
		}
		
		return nodeInfo;
	}
	
	
	/*
	 * Traverse a class expression tree to pinpoint variables
	 */
	private void checkClassExpression(AxiomTree tree, TreeNodeInfo nodeInfo) {
		for(AxiomTree t : tree.getChildTrees()) {
			AxiomTreeNode node = t.getUserObject();
			if(node.getObject() != null) {
				OWLObject obj = node.getObject();
				if(obj instanceof OWLClass) {
					OWLClass c = (OWLClass)obj;
					if(c.getIRI().toString().contains("http://semanticweb.org/variable")) {
						println("    Variable: " + getManchesterSyntax(c));
						nodeInfo.setFixed(false);
						nodeInfo.addClassVariable(c, t);
					}
				}
				else if(obj instanceof OWLObjectProperty) {
					OWLObjectProperty prop = (OWLObjectProperty)obj;
					if(prop.getIRI().toString().contains("http://semanticweb.org/variable")) {
						println("    Variable: " + getManchesterSyntax(prop));
						nodeInfo.setFixed(false);
						nodeInfo.addRoleVariable(prop, t);
					}
				}
			}
			else if(node.getClassExpression() != null) {
				checkClassExpression(t, nodeInfo);
			}
		}
	}


	/*
	 * Given the original query axiom tree, the node info object for left- or right-hand side of the query axiom,
	 * replace the given variables with the supplied fillers, and return the set of new axioms formed over these
	 * new assignments 
	 */
	private Map<OWLAxiom,OWLClassExpression> replaceVariables(AxiomTree axTree, TreeNodeInfo nodeInfo, 
			Set<OWLClassExpression> fillers, Set<OWLObjectProperty> roleFillers) {
		
		Map<OWLAxiom,OWLClassExpression> witnessAxs = new HashMap<OWLAxiom,OWLClassExpression>();
		
		// Concept variable or concept and role variables (same procedure for both)
		if(nodeInfo.hasConceptVariable() && !nodeInfo.hasRoleVariable()) {
			OWLClass classVariable = nodeInfo.getConceptVariable();
			println("\n  Found " + fillers.size() + " possible fillers for variable " + getManchesterSyntax(classVariable));
			AxiomTree toReplace = nodeInfo.getAxiomTree(classVariable);

			for(OWLClassExpression ce : fillers) {
//				PairMapping mapping = new PairMapping();
//				mapping.add(new TreePair(ce, t));
//
//				AxiomTree copy = axTree.deepCopyWithReplacement(mapping);
				witnessAxs.put(replaceVariable(axTree, toReplace, ce, nodeInfo), ce);
			}
		}
		else if(nodeInfo.hasRoleVariable()) {
			OWLObjectProperty roleVariable = nodeInfo.getRoleVariable();
			println("\n\tFound " + roleFillers.size() + " possible fillers for variable " + getManchesterSyntax(roleVariable));
			AxiomTree toReplace = nodeInfo.getAxiomTree(roleVariable);
			
			for(OWLObjectProperty p : roleFillers) {
				if(nodeInfo.hasConceptVariable()) {
					OWLClass classVariable = nodeInfo.getConceptVariable();
					AxiomTree t = nodeInfo.getAxiomTree(classVariable);
					
					println("\n\tFound " + fillers.size() + " possible fillers for variable " + getManchesterSyntax(classVariable));
					
					TreePair propFiller = new TreePair(p, toReplace);
					
					for(OWLClassExpression ce : fillers) {
						PairMapping mapping = new PairMapping();
						mapping.add(new TreePair(ce, t));
						mapping.add(propFiller);

						AxiomTree copy = axTree.deepCopyWithReplacement(mapping);
						witnessAxs.put(copy.asOWLAxiom(),ce);
					}
				}
				else { //TODO
//					PairMapping mapping = new PairMapping();
//					mapping.add(propFiller);
//
//					AxiomTree copy = axTree.deepCopyWithReplacement(mapping);
//					witnessAxs.put(replaceVariable(axTree, toReplace, p, nodeInfo), p);
				}
			}
		}
		
		return witnessAxs;
	}
	
	
	/*
	 * Given the original axiom tree (axTree), replace the given axiom tree (toReplace) with the given OWL object
	 */
	private OWLAxiom replaceVariable(AxiomTree axTree, AxiomTree toReplace, OWLObject obj, TreeNodeInfo nodeInfo) {
		PairMapping mapping = new PairMapping();
		mapping.add(new TreePair(obj, toReplace));

		AxiomTree copy = axTree.deepCopyWithReplacement(mapping);
		return copy.asOWLAxiom();
	}
	
	
	/*
	 * Get all candidate witnesses
	 */
	private Set<OWLClassExpression> getAllCandidates(OWLClassExpression c, OWLOntology ont, AxiomTree tree, TreeNodeInfo treeNodeInfo, 
			Set<OWLObjectProperty> roles, boolean lhs) {
		Set<OWLClassExpression> cands = new HashSet<OWLClassExpression>();
		
		Set<OWLClassExpression> scs = getSubConcepts(ont);
		
		System.out.println("------------------------------");
		for(OWLClassExpression sc : scs) {
			System.out.println("\tSC: " + getManchesterSyntax(sc));
		}
		System.out.println("------------------------------");
		
		cands.addAll(scs);									// subconcepts
		if(extendedGrammar) {
			cands.addAll(getR1candidates(c, lhs, ont, scs, tree, treeNodeInfo));	// disjunction or conjunction
			if(!roles.isEmpty()) {
				cands.addAll(getR2candidates(roles, scs));		// existential restrictions
				cands.addAll(getR3candidates(roles, scs));		// universal restrictions
			}
			cands.addAll(getR4candidates(scs));					// complement of
		}
		
		cands.remove(c);
		
		return cands;
	}
	
	
	/*
	 * Get candidate witnesses of type: (sc or sc) || (sc and sc) depending
	 *  on whether the variable is on the left- or right-hand side
	 */
	private Set<OWLClassExpression> getR1candidates(OWLClassExpression c, boolean lhs, OWLOntology ont, Set<OWLClassExpression> scs, 
			AxiomTree tree, TreeNodeInfo treeNodeInfo) {
		
		AxiomTree toReplace = null;
		
		if(treeNodeInfo.hasConceptVariable())
			toReplace = treeNodeInfo.getAxiomTree(treeNodeInfo.getConceptVariable());
		else if(treeNodeInfo.hasRoleVariable())
			toReplace = treeNodeInfo.getAxiomTree(treeNodeInfo.getRoleVariable());
		
		Set<OWLClassExpression> cands = new HashSet<OWLClassExpression>();
		Object[] arr = scs.toArray();
		int arrSize = arr.length;

		OWLReasoner reasoner = createReasoner("Pellet", ont, false);

		for(int i = 0; i < arrSize; i++) {
			OWLClassExpression ce = (OWLClassExpression) arr[i];
			if(!c.equals(ce)) {
				OWLAxiom test1 = replaceVariable(tree, toReplace, ce, treeNodeInfo);
//				if(lhs)
//					test1 = df.getOWLSubClassOfAxiom(c, ce);
//				else
//					test1 = df.getOWLSubClassOfAxiom(ce, c);

				if(!reasoner.isEntailed(test1)) {
					for(int j = i+1; j < arrSize-1; j++) {
						OWLClassExpression ce2 = (OWLClassExpression) arr[j];
						if(!ce2.equals(c) && !ce2.equals(ce)) {
							OWLAxiom test2 = replaceVariable(tree, toReplace, ce2, treeNodeInfo);
//							if(lhs)
//								test2 = df.getOWLSubClassOfAxiom(c, ce2);
//							else
//								test2 = df.getOWLSubClassOfAxiom(ce2, c);

							if(!reasoner.isEntailed(test2)) {
								Set<OWLClassExpression> testWitness = new HashSet<OWLClassExpression>();
								testWitness.add(ce);
								testWitness.add(ce2);

								if(lhs)
									cands.add(df.getOWLObjectUnionOf(testWitness));
								else
									cands.add(df.getOWLObjectIntersectionOf(testWitness));
							}
						}
					}
				}
			}
		}
		
		if(verbose && !cands.isEmpty()) {
			if(lhs)
				printSet("R1 - Disjunctions", cands);
			else 
				printSet("R1 - Conjunctions", cands);
		}
		
		return cands;
	}
	
	
	/*
	 * Get candidate witnesses of type: r some sc
	 */
	private Set<OWLClassExpression> getR2candidates(Set<OWLObjectProperty> roles, Set<OWLClassExpression> scs) {
		Set<OWLClassExpression> cands = new HashSet<OWLClassExpression>();
		for(OWLClassExpression ce : scs) {
			for(OWLObjectProperty r : roles) {
				cands.add(df.getOWLObjectSomeValuesFrom(r, ce));
			}
		}
		if(verbose && !cands.isEmpty()) printSet("R2 - Existential Restrictions", cands);
		return cands;
	}
	
	
	/*
	 * Get candidate witnesses of type: r only sc
	 */
	private Set<OWLClassExpression> getR3candidates(Set<OWLObjectProperty> roles, Set<OWLClassExpression> scs) {
		Set<OWLClassExpression> cands = new HashSet<OWLClassExpression>();
		for(OWLClassExpression ce : scs) {
			for(OWLObjectProperty r : roles) {
				cands.add(df.getOWLObjectAllValuesFrom(r, ce));
			}
		}
		if(verbose && !cands.isEmpty()) printSet("R3 - Universal Restrictions", cands);
		return cands;
	}
	
	
	/*
	 * Get candidate witnesses of type: r some sc
	 */
	private Set<OWLClassExpression> getR4candidates(Set<OWLClassExpression> scs) {
		Set<OWLClassExpression> cands = new HashSet<OWLClassExpression>();
		for(OWLClassExpression ce : scs) {
			cands.add(df.getOWLObjectComplementOf(ce)); 
		}
		if(verbose && !cands.isEmpty()) printSet("R4 - Complement Of", cands);
		return cands;
	}
	
	
	/*
	 * Get sub-concepts of an ontology
	 */
	private Set<OWLClassExpression> getSubConcepts(OWLOntology ont) {
		Set<OWLClassExpression> sc = new HashSet<OWLClassExpression>();
		Set<OWLLogicalAxiom> axs = ont.getLogicalAxioms();
		for(OWLAxiom ax : axs) {
			Set<OWLClassExpression> ax_sc = ax.getNestedClassExpressions();
			for(OWLClassExpression ce : ax_sc) {
				if(!sc.contains(ce) && !ce.isOWLThing() && !ce.isOWLNothing()) {
					sc.add(ce);
					getSubConcepts(ce, sc);
				}
			}
		}
		return sc;
	}


	/*
	 * Recursively get subconcepts 
	 */
	private void getSubConcepts(OWLClassExpression ce, Set<OWLClassExpression> sc) {
		if(ce.getNestedClassExpressions().size() > 0) {
			for(OWLClassExpression c : ce.getNestedClassExpressions()) {
				if(!sc.contains(c) && !c.isOWLThing() && !c.isOWLNothing()) {
					sc.add(c);
					getSubConcepts(c, sc);
				}
			}
		}
	}
	
	
	/*
	 * Create instance of the mentioned reasoner
	 */
	private OWLReasoner createReasoner(String name, OWLOntology ont, boolean monitor) {
		OWLReasonerConfiguration config = null;
		if(monitor)
			config = new SimpleConfiguration(new ConsoleProgressMonitor());
		else 
			config = new SimpleConfiguration();

		OWLReasonerFactory reasonerFactory = null;
		OWLReasoner reasoner = null;

		if(name.equals("Fact")) {
			reasoner = new FaCTPlusPlusReasoner(ont, config, BufferingMode.BUFFERING);			
		}
		else if(name.equals("Pellet")) {
			reasonerFactory = new PelletReasonerFactory();
			reasoner = reasonerFactory.createReasoner(ont, config); 
		}
		else if(name.equals("JFact")) {
			reasoner = new JFactReasoner(ont, config, BufferingMode.BUFFERING);
		}
		else if(name.equals("Hermit")) {
			reasoner = new Reasoner(ont);
		}
		else {
			throw new RuntimeErrorException(new Error("Unknown reasoner: " + name + 
					"\tAccepted reasoners: Fact | Pellet | JFact | Hermit"));
		}

		return reasoner; 
	}
	
	
	/*
	 * Get Manchester syntax of an OWL object
	 */
	private String getManchesterSyntax(OWLObject obj) {
		SimpleShortFormProvider fp = new SimpleShortFormProvider();
		StringWriter wr = new StringWriter();

		ManchesterOWLSyntaxObjectRenderer render = new ManchesterOWLSyntaxObjectRenderer(wr, fp);
		render.setUseWrapping(false);
		obj.accept(render);

		String str = wr.getBuffer().toString();

		return str;
	}
	
	
	/*
	 * Print set of objects in Manchester syntax
	 */
	private void printSet(String desc, Set<? extends OWLObject> set) {
		println("------------------------------------------------");
		println(desc + " (" + set.size() + " elements)");
		println("------------------------------------------------");
		for(OWLObject obj : set) {
			println("\t" + getManchesterSyntax(obj));
		}
	}
	
	
	/*
	 * Print 
	 */
	private void print(String s) {
		System.out.print(s);
	}
	
	
	/*
	 * Print line
	 */
	private void println(String s) {
		System.out.println(s);
	}
}
