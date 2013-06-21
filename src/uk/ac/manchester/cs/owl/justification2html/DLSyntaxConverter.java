package uk.ac.manchester.cs.owl.justification2html;

import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.util.ShortFormProvider;
import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 29-Oct-2010
 */
public class DLSyntaxConverter extends AbstractJustification2Html {

    private Map<String, String> map = new HashMap<String, String>();

    public DLSyntaxConverter() {

    }

    @Override
    protected OWLObjectRenderer getObjectRenderer(ShortFormProvider sfp) {
        DLSyntaxObjectRenderer renderer = new DLSyntaxObjectRenderer();
        renderer.setShortFormProvider(sfp);
        return renderer;
    }

    @Override
    protected String getKeyword(String tok) {
        if (tok.length() == 1) {

            for(DLSyntaxToUnicode syntax : DLSyntaxToUnicode.values()) {
                if(syntax.toString().equals(tok)) {
                    return "<span>&#" + ((int) tok.charAt(0)) + ";</span>";
                    //return "<span>&#" + Character.codePointAt(tok.toCharArray(), 0) + ";</span>";
                }
            }
        }
        return tok;
    }
}
