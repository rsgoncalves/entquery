package uk.ac.manchester.cs.owl.justification2html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

public class Tester {

	/**
	 * @param args
	 * @throws OWLOntologyCreationException 
	 */
	public static void main(String[] args) throws OWLOntologyCreationException {
		File file = new File(args[0]);
		System.out.println(file);
		JustificationOntologyParser parser = new JustificationOntologyParser();

		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        
        OWLOntology ont = man.loadOntology(IRI.create("file:" + file.getAbsolutePath()));


		if(ont != null) {
			System.out.println(ont);
			System.out.println("------------------------------------------------------------------------");
			ManchesterSyntaxConverter converter = new ManchesterSyntaxConverter();
			
			for(OWLAxiom ax : ont.getAxioms()) {
				String html = converter.convertToHTML(ax, new SimpleShortFormProvider());
				System.out.println(html);
			}
			
//			System.out.println("------------------------------------------------------------------------");
//			DLSyntaxConverter dlSyntaxConverter = new DLSyntaxConverter();
//			String html2 = dlSyntaxConverter.convertToHTML(expl, new ObfuscatingShortFormProvider());
//			System.out.println(html2);

//			J2HTMLOutput.saveToHTML(html, html, file, 1);
        }
	}

}
