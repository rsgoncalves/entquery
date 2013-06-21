package uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree;

import org.semanticweb.owlapi.model.OWLObject;

/**
 * Created by
 * User: Samantha Bail
 * Date: 18/05/2012
 * Time: 20:55
 * The University of Manchester
 */


public class TreePair {

      private OWLObject var;
    private AxiomTree ex;

    public OWLObject getVar() {
        return var;
    }

    public AxiomTree getEx() {
        return ex;
    }



    public TreePair(OWLObject var, AxiomTree ex) {
        this.var = var;
        this.ex = ex;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("( ");
        sb.append(var);
        sb.append(", ");
        sb.append(ex);
        sb.append(" )");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreePair)) return false;

        TreePair treePair = (TreePair) o;

        if (!ex.equals(treePair.ex)) return false;
        if (!var.equals(treePair.var)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = var.hashCode();
        result = 31 * result + ex.hashCode();
        return result;
    }
}
