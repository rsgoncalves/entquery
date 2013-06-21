package uk.ac.manchester.cs.owl.justification2html;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.util.ShortFormProvider;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 29-Oct-2010
 */
public interface Justification2Html {

    String convertToHTML(Explanation<OWLAxiom> explanation, ShortFormProvider sfp);

}
