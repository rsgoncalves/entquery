package uk.ac.manchester.cs.owl.justification2html;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 29-Oct-2010
 */
public class JustificationOntologyParser {

    public static IRI ENTAILMENT_ANNOTATION_PROPERTY_IRI = IRI.create("http://owl.cs.manchester.ac.uk/explanation/vocabulary#entailment");



    public Explanation<OWLAxiom> parser(InputStream is) throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(is);
        Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
        OWLAxiom entailment = null;
        for(OWLAxiom ax : ontology.getAxioms()) {
            OWLDataFactory df = manager.getOWLDataFactory();
            OWLAnnotationProperty entailmentAnnotationProperty = df.getOWLAnnotationProperty(ENTAILMENT_ANNOTATION_PROPERTY_IRI);
            if(!ax.getAnnotations(entailmentAnnotationProperty).isEmpty()) {
                entailment = ax;
            }
            else {
                axioms.add(ax);
            }
        }
        if(entailment == null) {
            throw new RuntimeException("Entailment not found");
        }
        return new Explanation<OWLAxiom>(entailment, axioms);
    }

}
