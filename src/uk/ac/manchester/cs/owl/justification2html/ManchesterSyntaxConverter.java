package uk.ac.manchester.cs.owl.justification2html;

import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.util.ShortFormProvider;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxOWLObjectRendererImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 29-Oct-2010
 */
public class ManchesterSyntaxConverter extends AbstractJustification2Html {

    private Map<String, String> map = new HashMap<String, String>();

    public ManchesterSyntaxConverter() {
        this.map = new HashMap<String, String>();
        map.put("some", "rkw");
        map.put("only", "rkw");
        map.put("max", "rkw");
        map.put("min", "rkw");
        map.put("exactly", "rkw");
        map.put("value", "rkw");
        map.put("Self", "rkw");
        map.put("and", "bkw");
        map.put("or", "bkw");
        map.put("not", "bkw");
        map.put("SubClassOf", "axkw");
        map.put("SubPropertyOf", "axkw");
        map.put("EquivalentTo", "axkw");
        map.put("DisjointWith", "axkw");
        map.put("Domain", "axkw");
        map.put("Range", "axkw");
        map.put("Functional", "axkw");
        map.put("InverseFunctional", "axkw");
        map.put("Symmetric", "axkw");
        map.put("Transitive", "axkw");
        map.put("Irreflexive", "axkw");
        map.put("Reflexive", "axkw");
        map.put("InverseOf", "axkw");
        map.put("Type", "axkw");
        map.put("owl:Nothing", "nothing");
    }

    protected String getKeyword(String kw) {
        String cssClass = map.get(kw);
        if(cssClass != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("<span class=\"");
            sb.append(cssClass);
            sb.append("\">");
            sb.append(kw);
            sb.append("</span>");
            return sb.toString();
        }
        else {
            return kw;
        }
    }

    @Override
    protected OWLObjectRenderer getObjectRenderer(ShortFormProvider sfp) {
        ManchesterOWLSyntaxOWLObjectRendererImpl impl = new ManchesterOWLSyntaxOWLObjectRendererImpl();
        impl.setShortFormProvider(sfp);
        return impl;
    }
}
