package uk.ac.manchester.cs.owlquery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.RuntimeErrorException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxInlineAxiomParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.io.IRIDocumentSource;
import org.semanticweb.owlapi.io.StreamDocumentSource;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.UnloadableImportException;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxObjectRenderer;

/**
 * Author: 	Rafael Gonçalves<br>
 *          The University Of Manchester<br>
 *          Information Management Group<br>
 * Date: 	25-Jul-2012<br><br>
 */

/**
 * Servlet implementation class WebOWLQuery
 */
@WebServlet("/webowlquery")
public class WebOWLQuery extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Set<OWLAxiom> ont;
	private List<OWLAxiom> queries;
	private boolean inferredOnly, extendedGrammar;
	private String reasonerName;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WebOWLQuery() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getQueryAnswers(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getQueryAnswers(request, response);
	}


	private void getQueryAnswers(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		PrintWriter pw = response.getWriter();
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();

		try {
			processRequest(pw, man, request, response);
		} catch (FileUploadException e) {
			throw new ServletException(e);
		} catch (OWLOntologyCreationException e) {
			throw new ServletException(e);
		} catch (ParserException e) {
			throw new ServletException(e);
		} 

		if(ont != null && queries != null) {
			try {
				OWLOntology sourceOnt = man.createOntology(ont);

				QueryProcessor proc = new QueryProcessor(sourceOnt, queries, reasonerName, inferredOnly, extendedGrammar, false);

				List<QueryAnswer> answers = proc.getQueryAnswers();
				request.getSession().setAttribute("answers", answers);
				response.sendRedirect("answers");
			} catch (OWLOntologyCreationException e) {
				throw new ServletException(e);
			}
		}
	}


	/*
	 * Load Ontology
	 */
	private void processRequest(PrintWriter pw, OWLOntologyManager man, HttpServletRequest request, HttpServletResponse response) 
			throws IOException, FileUploadException, OWLOntologyCreationException, ParserException, ServletException {

		OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
		config.setLoadAnnotationAxioms(false);

		try {
			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			@SuppressWarnings("unchecked")
			List<FileItem> items = upload.parseRequest(request);

			// Process the uploaded items
			Iterator<FileItem> iter = items.iterator();

			String ont1 = null, ont2 = null;
			OWLOntology ontFile = null, queryFile = null;
			
			inferredOnly = false; extendedGrammar = false;
			
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				String name = item.getFieldName();
				
				if(name.equals("inferredOnly")) {
					System.out.println("Inferred Only: " + item.getString());
					
					if(item.getString().equals("true"))
						inferredOnly = true;
//					else
//						inferredOnly = false;
				}
				else if(name.equals("reasoner")) {
					reasonerName = item.getString();
					System.out.println("Reasoner: " + reasonerName); 
				}
				else if(name.equals("extendedGrammar")) {
					String gram = item.getString();
					System.out.println("Extended Grammar: " + gram);
					
					if(gram.equals("true")) {
						extendedGrammar = true;
						System.out.println("Using extended grammar");
					}
//					else {
//						extendedGrammar = false;
//						System.out.println("Using basic grammar");
//					}
				}
				else if(name.equals("ont")) {
					ont1 = item.getString();
					if(!ont1.equals("")) {
						IRI physicalIRI = getPhysicalIRI(ont1.trim());
						if (physicalIRI != null)
							ontFile = man.loadOntologyFromOntologyDocument(new IRIDocumentSource(physicalIRI), config);
						else
							ontFile = man.loadOntologyFromOntologyDocument(new StringDocumentSource(ont1.trim()), config);

						if(ontFile != null) {
							ont = ontFile.getAxioms();
							processImports(man, ontFile, ont);
						}
						else
							throw new RuntimeErrorException(new Error("Unable to load source ontology. Ensure that the given input is correct and accessible: " + ont1));
					}
				}
				else if(name.equals("ontfile")) {
					InputStream file1Stream = item.getInputStream();
					if(file1Stream != null && file1Stream.available() != 0) {
						ontFile = man.loadOntologyFromOntologyDocument(new StreamDocumentSource(file1Stream), config);
						if(ontFile != null) {
							ont = ontFile.getAxioms();
							processImports(man, ontFile, ont);
						}
						else
							throw new RuntimeErrorException(new Error("Unable to load source ontology. Ensure that the given input is correct and accessible: " + ont1));
					}
				}
				else if(name.equals("queries")) {
					ont2 = item.getString();
					queries = new ArrayList<OWLAxiom>();
					if(!ont2.equals("")) {
						BufferedReader reader = new BufferedReader(new StringReader(ont2));
						String q = null;
						while((q = reader.readLine()) != null) {
							queries.add(processQuery(ontFile, q));
						}
						printQueries();
					}
				}
				else if(name.equals("queryfile")) {
					InputStream file2Stream = item.getInputStream();
					if(file2Stream != null  && file2Stream.available() != 0) {
						queryFile = man.loadOntologyFromOntologyDocument(new StreamDocumentSource(file2Stream), config);
						if(queryFile != null) {
							queries.addAll(queryFile.getAxioms());
							processImports(man, queryFile, queries);
						}
						printQueries();
					}
				}
			}
		} catch (UnloadableImportException e) {
			throw new ServletException(e);
//			("Unable to process imports closure. Ensure that all IRIs of imported ontologies are " +
//					"accessible via that same IRI. E.g.,\nin the case of a local imported ontology, make sure that the IRI " +
//					"in the imports declaration points to the absolute file path, preceded by 'file:'.\n" +
//					"\nThis error occurs when the servlet is unable to resolve imported ontologies.\n");
		} catch (OWLOntologyCreationException e) {
			throw new ServletException(e);
		} catch (FileUploadException e) {
			throw new ServletException(e);
		}
	}

	
	/*
	 * Get Physical IRI
	 */
	private static IRI getPhysicalIRI(String input) {
		IRI physicalIRI = IRI.create(input);

		if (physicalIRI.isAbsolute()) {
			return physicalIRI;
		}
		else {
			return null;
		}
	}
	
	
	/*
	 * Process Imports	
	 */
	private void processImports(OWLOntologyManager man, OWLOntology ont, Collection<OWLAxiom> axSet) {
		Set<OWLOntology> imports = ont.getImports();
		for(OWLOntology o : imports) {
			axSet.addAll(o.getLogicalAxioms());
			man.removeOntology(o);
		}
		man.removeOntology(ont);
	}
	
	
	/*
	 * Process individual queries
	 */
	private OWLAxiom processQuery(OWLOntology ont, String query) throws ParserException {
		OWLAxiom ax = null;
		
		SimpleShortFormProvider sf = new SimpleShortFormProvider();
		OWLOntologyManager man = ont.getOWLOntologyManager(); 
		OWLDataFactory df = man.getOWLDataFactory();
		OWLClass variable = null;
		
		if(query.contains("?")) {
			String var = query.substring(query.indexOf("?"), query.indexOf("?")+2);
			variable = df.getOWLClass(IRI.create("http://semanticweb.org/variable#" + var.replace("?", "")));
			man.addAxiom(ont, df.getOWLDeclarationAxiom(variable));
			query = query.replace(var, sf.getShortForm(variable));
		}
		
		Set<OWLOntology> ontSet = new HashSet<OWLOntology>();
		ontSet.add(ont);
		
		ManchesterOWLSyntaxInlineAxiomParser parser = new ManchesterOWLSyntaxInlineAxiomParser(df, 
				new ShortFormEntityChecker(new BidirectionalShortFormProviderAdapter(man, ontSet, sf)));
		
		ax = parser.parse(query);
		
		if(ax == null) {
			System.out.println("BAD QUERY");
			throw new RuntimeException("Malformed query: " + query);
		}
		else
			return ax;
	}
	
	
	/*
	 * Print queries
	 */
	private void printQueries() {
		if(queries != null) {
			System.out.println("---------------------------------------\n" +
					"Parsed Queries (" + queries.size() + ")\n");
			
			int counter = 1;
			for(OWLAxiom ax : queries) {
				System.out.println(counter + ") " + getManchesterSyntax(ax));
				counter++;
			}
			System.out.println("");
		}
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
}
