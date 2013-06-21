package uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree;

import java.util.ArrayList;

/**
 * Created by
 * User: Samantha Bail
 * Date: 02/02/2012
 * Time: 20:23
 * The University of Manchester
 */


public class PairMapping implements Cloneable {

    ArrayList<TreePair> pairs = new ArrayList<TreePair>();

    public PairMapping() {

    }

    public void add(TreePair t) {
        pairs.add(t);
    }

    public TreePair getPairForEx(AxiomTree o) {
        for (TreePair t : pairs) {
            if (t.getEx().sameContent(o)) {
                return t;
            }
        }
        return null;
    }



    public ArrayList<TreePair> getPairs() {
        return pairs;
    }

    @Override
    public String toString() {
        if (pairs.isEmpty()) {
            return "( empty mapping )\n";
        }
        StringBuilder sb = new StringBuilder();
        for (TreePair t : pairs) {
            sb.append(t.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public Object clone() {
        try {
            PairMapping copy = (PairMapping) super.clone();
            copy.pairs = new ArrayList<TreePair>(this.pairs);
            return copy;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PairMapping)) return false;

        PairMapping that = (PairMapping) o;

        if (pairs != null ? !pairs.equals(that.pairs) : that.pairs != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return pairs != null ? pairs.hashCode() : 0;
    }

    public PairMapping deepCopy() {
        return (PairMapping) this.clone();
    }


}
