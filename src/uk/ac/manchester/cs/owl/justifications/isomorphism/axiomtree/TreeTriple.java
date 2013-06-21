package uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree;

import org.semanticweb.owlapi.model.OWLObject;

/**
 * Created by
 * User: Samantha Bail
 * Date: 02/02/2012
 * Time: 15:35
 * The University of Manchester
 */


public class TreeTriple {

    private OWLObject var;
    private AxiomTree o1;
    private AxiomTree o2;

    public OWLObject getVar() {
        return var;
    }

    public AxiomTree getO1() {
        return o1;
    }

    public AxiomTree getO2() {
        return o2;
    }

    public TreeTriple(OWLObject var, AxiomTree o1, AxiomTree o2) {
        this.var = var;
        this.o1 = o1;
        this.o2 = o2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("( ");
        sb.append(var);
        sb.append(", ");
        sb.append(o1);
        sb.append(", ");
        sb.append(o2);
        sb.append(" )");

        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = var != null ? var.hashCode() : 0;
        result = 31 * result + (o1 != null ? o1.hashCode() : 0);
        result = 31 * result + (o2 != null ? o2.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        TreeTriple that = (TreeTriple) o;
        AxiomTree t1 = that.getO1();
        AxiomTree t2 = that.getO2();
        return (t1.sameContent(this.getO1()) && t2.sameContent(this.getO2()));
    }
}
