package uk.ac.manchester.cs.owl.justification2html;

import java.util.StringTokenizer;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.util.ShortFormProvider;

import uk.ac.manchester.cs.bhig.util.Tree;
import uk.ac.manchester.cs.owl.explanation.ordering.DefaultExplanationOrderer;

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 29-Oct-2010
 */
public abstract class AbstractJustification2Html implements Justification2Html {


    protected abstract OWLObjectRenderer getObjectRenderer(ShortFormProvider sfp);

    protected abstract String getKeyword(String tok);

    protected String renderAxiom(OWLObject axiom, ShortFormProvider sfp) {
        OWLObjectRenderer renderer = getObjectRenderer(sfp);
        String rendering = renderer.render(axiom);
        rendering = rendering.replace("\n", " ");
        rendering = rendering.replaceAll("\\s+", " ");
        rendering = rendering.replace(">", "&gt;");
        rendering = rendering.replace("<", "&lt;");
        StringTokenizer tokenizer = new StringTokenizer(rendering, "[]{}() ,", true);
        StringBuilder sb = new StringBuilder();
        while(tokenizer.hasMoreTokens()) {
            String tok = tokenizer.nextToken();
            sb.append(getKeyword(tok));
        }
        return sb.toString();
    }

    public String convertToHTML(Explanation<OWLAxiom> explanation, ShortFormProvider sfp) {
        DefaultExplanationOrderer orderer = new DefaultExplanationOrderer();
        Tree<OWLAxiom> tree = orderer.getOrderedExplanation(explanation.getEntailment(), explanation.getAxioms());
        StringBuilder sb = new StringBuilder();
        render(sb, sfp, tree);
        return sb.toString();
    }
    
    public String convertToHTML(OWLAxiom ax, ShortFormProvider sfp) {
        StringBuilder sb = new StringBuilder();
        render(sb, sfp, ax);
        return sb.toString();
    }
    
    public String convertToHTML(OWLClassExpression ce, ShortFormProvider sfp) {
        StringBuilder sb = new StringBuilder();
        render(sb, sfp, ce);
        return sb.toString();
    }

    private void render(StringBuilder sb, ShortFormProvider sfp, Tree<OWLAxiom> tree) {
        int depth = tree.getPathToRoot().size();
        sb.append("<div class=\"in");
        sb.append(depth);
        sb.append("\">");
        sb.append(renderAxiom(tree.getUserObject(), sfp));
        sb.append("</div>\n");
        for(Tree<OWLAxiom> child : tree.getChildren()) {
            render(sb, sfp, child);
        }
    }
    
    private void render(StringBuilder sb, ShortFormProvider sfp, OWLAxiom ax) {
        sb.append(renderAxiom(ax, sfp));
    }
    
    private void render(StringBuilder sb, ShortFormProvider sfp, OWLClassExpression ce) {
        sb.append(renderAxiom(ce, sfp));
    }
}
