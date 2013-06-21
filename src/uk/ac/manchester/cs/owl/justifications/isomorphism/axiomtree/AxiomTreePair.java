package uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree;

/**
 * Created by
 * User: Samantha Bail
 * Date: 03/02/2012
 * Time: 12:24
 * The University of Manchester
 */


public class AxiomTreePair {

    private AxiomTree tree1;
    private AxiomTree tree2;

    public AxiomTreePair(AxiomTree tree1, AxiomTree tree2) {
        this.tree1 = tree1;
        this.tree2 = tree2;
    }

    public AxiomTree getTree1() {
        return tree1;
    }

    public AxiomTree getTree2() {
        return tree2;
    }

    @Override
    public int hashCode() {
        int result = tree1 != null ? tree1.hashCode() : 0;
        result = 31 * result + (tree2 != null ? tree2.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        AxiomTreePair that = (AxiomTreePair) o;
        return that.getTree1().equals(this.getTree1()) && that.getTree2().equals(this.getTree2());
    }

}
