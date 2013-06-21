package uk.ac.manchester.cs.owlquery;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationException;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.api.ExplanationGeneratorFactory;
import org.semanticweb.owl.explanation.api.ExplanationManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;
import uk.ac.manchester.cs.owlapi.modularity.ModuleType;
import uk.ac.manchester.cs.owlapi.modularity.SyntacticLocalityModuleExtractor;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

/**
 * Author: 	Rafael Gonçalves<br>
 *          The University Of Manchester<br>
 *          Information Management Group<br>
 * Date: 	25-Jul-2012<br><br>
 */

public class Justifications {

	private Set<Set<Explanation<OWLAxiom>>> allExps;

	/*
	 * Constructor
	 */
	public Justifications() {}

	
	/*
	 * Get all justifications for a given set of entailments
	 */
	public void getJustifications(Set<OWLAxiom> entailments, OWLOntology toExtractFrom) throws OWLOntologyCreationException, ServletException {		
		allExps = new HashSet<Set<Explanation<OWLAxiom>>>();
		
		// For each entailment extract a module for its signature and obtain all justifications
		for(OWLAxiom ent : entailments) {
			if(ent.isLogicalAxiom()) {
				// Extract module for signature of each axiom
				Set<OWLEntity> sig = ent.getSignature();
				OWLOntology module = extractModule(sig, toExtractFrom);

				Set<Explanation<OWLAxiom>> exps = null;

				// Get set of justifications
				exps = getExplanations(ent, module);

				if(exps.size() > 0)
					allExps.add(exps);
			}
		}
	}
	
	
	/*
	 * Get the set of justifications from an ontology for a given axiom
	 */
	public Set<Explanation<OWLAxiom>> getExplanations(OWLAxiom ax, OWLOntology ont) throws ServletException {
		// Initialise reasoner (FaCt++)
		OWLReasonerFactory rf = new PelletReasonerFactory();
		OWLReasoner reasoner = rf.createReasoner(ont, new SimpleConfiguration());

		// Initialise explanation generator
		ExplanationGeneratorFactory<OWLAxiom> genFac = ExplanationManager.createExplanationGeneratorFactory(rf);
		ExplanationGenerator<OWLAxiom> exGen = genFac.createExplanationGenerator(ont);

		Set<Explanation<OWLAxiom>> regExps = null;

		if (reasoner.isConsistent()) {
			try {
				// Get explanations (max value as 2nd parameter)
				regExps = exGen.getExplanations(ax, 5);
			} catch (ExplanationException e) {
				throw new ServletException(e);
			} catch (VirtualMachineError e) {
				throw new ServletException(e);
			}
		}

		reasoner.dispose();

		return regExps;
	}
	
	
	/*
	 * Get the set of justifications from an ontology for a given axiom
	 */
	public Set<Explanation<OWLAxiom>> getExplanations(OWLAxiom ax, OWLOntology ont, OWLReasoner reasoner) throws ServletException {
		// Initialise reasoner (FaCt++)
		OWLReasonerFactory rf = new FaCTPlusPlusReasonerFactory();

		// Initialise explanation generator
		ExplanationGeneratorFactory<OWLAxiom> genFac = ExplanationManager.createExplanationGeneratorFactory(rf);
		ExplanationGenerator<OWLAxiom> exGen = genFac.createExplanationGenerator(ont);

		Set<Explanation<OWLAxiom>> regExps = null;

		if (reasoner.isConsistent()) {
			try {
				// Get explanations (max value as 2nd parameter)
				regExps = exGen.getExplanations(ax, 5);
			} catch (ExplanationException e) {
				throw new ServletException(e);
			} catch (VirtualMachineError e) {
				throw new ServletException(e);
			}
		}

		reasoner.dispose();

		return regExps;
	}

	
	/*
	 * Extract a top-bot-star locality-based module for a given set of
	 * terms, from the mentioned ontology
	 */
	public OWLOntology extractModule(Set<OWLEntity> ents, OWLOntology ont) throws OWLOntologyCreationException {
		SyntacticLocalityModuleExtractor modExtractor =
			new SyntacticLocalityModuleExtractor(ont.getOWLOntologyManager(), ont, ModuleType.STAR);
		
		return modExtractor.extractAsOntology(ents, IRI.generateDocumentIRI());
	}
	
	
	/*
	 * Get the set of all justications for one of the inferred/redundant sets
	 */
	public Set<Set<Explanation<OWLAxiom>>> getAllExplanations() {
		return allExps;
	}
}