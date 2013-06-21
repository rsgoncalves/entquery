package uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree;

import java.util.ArrayList;

/**
 * Created by
 * User: Samantha Bail
 * Date: 02/02/2012
 * Time: 20:23
 * The University of Manchester
 */


public class TreeMapping implements Cloneable {

    ArrayList<TreeTriple> triples = new ArrayList<TreeTriple>();

    public TreeMapping() {

    }

    public void add(TreeTriple t) {
        triples.add(t);
    }

    public TreeTriple getTripleFor01(AxiomTree o) {
        for (TreeTriple t : triples) {
            if (t.getO1().sameContent(o)) {
                return t;
            }
        }
        return null;
    }

    public TreeTriple getTripleFor02(AxiomTree o) {
        for (TreeTriple t : triples) {
            if (t.getO2().sameContent(o)) {
                return t;
            }
        }
        return null;
    }

    public TreeTriple getTripleMatchingO1(AxiomTree o) {
        for (TreeTriple t : triples) {
            if (t.getO1().contains(o) || o.contains(t.getO1())) {
                return t;
            }
        }
        return null;
    }

    public TreeTriple getTripleMatchingO2(AxiomTree o) {
        for (TreeTriple t : triples) {
            if (t.getO2().contains(o) || o.contains(t.getO2())) {
                return t;
            }
        }
        return null;
    }

    public boolean containsO1(AxiomTree o) {
        for (TreeTriple t : triples) {
            if (t.getO1().equals(o)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsO2(AxiomTree o) {
        for (TreeTriple t : triples) {
            if (t.getO2().equals(o)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<TreeTriple> getTriples() {
        return triples;
    }

    @Override
    public String toString() {
        if (triples.isEmpty()) {
            return "( empty mapping )\n";
        }
        StringBuilder sb = new StringBuilder();
        for (TreeTriple t : triples) {
            sb.append(t.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public Object clone() {
        try {
            TreeMapping copy = (TreeMapping) super.clone();
            copy.triples = new ArrayList<TreeTriple>(this.triples);
            return copy;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreeMapping)) return false;

        TreeMapping that = (TreeMapping) o;

        if (triples != null ? !triples.equals(that.triples) : that.triples != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return triples != null ? triples.hashCode() : 0;
    }

    public TreeMapping deepCopy() {
        return (TreeMapping) this.clone();
    }


}
