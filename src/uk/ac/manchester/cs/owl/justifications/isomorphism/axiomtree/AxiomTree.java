package uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree;

import org.semanticweb.owlapi.model.OWLAxiom;
import uk.ac.manchester.cs.bhig.util.MutableTree;
import uk.ac.manchester.cs.bhig.util.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by
 * User: Samantha Bail
 * Date: 29/01/2012
 * Time: 00:35
 * The University of Manchester
 */


public class AxiomTree extends MutableTree<AxiomTreeNode> implements Cloneable {

    public AxiomTree(AxiomTreeNode rootNode) {
        super(rootNode);
    }

    public AxiomTree() {
        super(new AxiomTreeNode());
    }

    public List<AxiomTree> getChildTrees() {
        ArrayList<AxiomTree> trees = new ArrayList<AxiomTree>();
        List<Tree<AxiomTreeNode>> cs = getChildren();
        for (Tree<AxiomTreeNode> c : cs) {
            trees.add((AxiomTree) c);
        }
        return trees;
    }

    // convenience method... oh so convenient
    public AxiomTree getChild(int i) {
        return getChildTrees().get(i);
    }

    /**
     * converts an axiom tree back into an OWL axiom
     * @return an owl axiom of the tree
     */
    public OWLAxiom asOWLAxiom() {
        AxiomTreeBuilder builder = new AxiomTreeBuilder();
        return builder.asOWLAxiom(this);
    }


    public AxiomTree deepCopy() {
        try {
            AxiomTree result = null;
            return copyTree(this, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private AxiomTree copyTree(AxiomTree source, AxiomTree result) throws CloneNotSupportedException {
        // if the root is null at the beginning, copy the root of teh source tree
        if (result == null) {
            result = new AxiomTree((AxiomTreeNode) source.getUserObject().clone());
        }
        // for each child node of the source, add a copy to the result tree
        for (AxiomTree child : source.getChildTrees()) {
            AxiomTree childCopy = new AxiomTree((AxiomTreeNode) child.getUserObject().clone());
            result.addChild(childCopy);
            // then recurse to add the children of the child nodes
            copyTree(child, childCopy);
        }
        return result;
    }


    public AxiomTree deepCopyWithMapping(TreeMapping m, int flag) {
        try {
            AxiomTree result = null;
            return copyTreeWithMapping(this, result, m, flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AxiomTree deepCopyWithReplacement(PairMapping m) {
        try {
            AxiomTree result = null;
            return copyTreeWithReplacement(this, result, m);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private AxiomTree copyTreeWithMapping(AxiomTree source, AxiomTree result, TreeMapping m, int flag) throws CloneNotSupportedException {
        TreeTriple triple = m.getTripleFor01(source);
        // if the root is null at the beginning, copy the root of teh source tree
        if (flag == 2) {
            triple = m.getTripleFor02(source);
        }
        if (result == null) {
            if (triple != null) {
                result = new AxiomTree(new AxiomTreeNode(triple.getVar()));
            } else {
                result = new AxiomTree((AxiomTreeNode) source.getUserObject().clone());
            }
        }
        // for each child node of the source, add a copy to the result tree
        for (AxiomTree child : source.getChildTrees()) {
            TreeTriple childTriple = m.getTripleFor01(child);
            if (flag == 2) {
                childTriple = m.getTripleFor02(child);
            }
            AxiomTree childCopy;
            if (childTriple != null) {
                childCopy = new AxiomTree(new AxiomTreeNode(childTriple.getVar()));
            } else {
                childCopy = new AxiomTree((AxiomTreeNode) child.getUserObject().clone());
            }
            result.addChild(childCopy);
            // then recurse to add the children of the child nodes
            copyTreeWithMapping(child, childCopy, m, flag);
        }
        return result;
    }

      private AxiomTree copyTreeWithReplacement(AxiomTree source, AxiomTree result, PairMapping m) throws CloneNotSupportedException {
        TreePair pair = m.getPairForEx(source);
        // if the root is null at the beginning, copy the root of teh source tree

        if (result == null) {
            if (pair != null) {
                result = new AxiomTree(new AxiomTreeNode(pair.getVar()));
            } else {
                result = new AxiomTree((AxiomTreeNode) source.getUserObject().clone());
            }
        }
        // for each child node of the source, add a copy to the result tree
        for (AxiomTree child : source.getChildTrees()) {
            TreePair childPair = m.getPairForEx(child);
            AxiomTree childCopy;
            if (childPair != null) {
                childCopy = new AxiomTree(new AxiomTreeNode(childPair.getVar()));
            } else {
                childCopy = new AxiomTree((AxiomTreeNode) child.getUserObject().clone());
            }
            result.addChild(childCopy);
            // then recurse to add the children of the child nodes
            copyTreeWithReplacement(child, childCopy, m);
        }
        return result;
    }

    public boolean sameContent(AxiomTree that) {
//        quick check: same object -> return true
//        if not the same object, compare the trees to check whether they have the same content
        return compareTrees(this, that);

    }

    /**
     * checks whether two trees have the same content
     * (i.e. not necessarily the same object, but the same node content)
     * @param tree
     * @param that the tree to compare to
     * @return true if the nodes have the same content, false otherwise
     */
    private boolean sameContent(AxiomTree tree, AxiomTree that) {
//        quick check: same object -> return true
        if (tree == that) return true;
//        if not the same object, compare the trees to check whether they have the same content
        return compareTrees(tree, that);

    }

    /**
     * compares two trees - returns true if they contain the same nodes
     * @param tree *this* tree
     * @param that the tree to compare to
     * @return returns true if they contain the same nodes, false otherwise
     */
    private boolean compareTrees(AxiomTree tree, AxiomTree that) {
        if (tree == that) return true;
        if (tree.getChildCount() != that.getChildCount()) {
            return false;
        }
        Set<AxiomTreeNode> nodes1 = tree.getUserObjectClosure();
        Set<AxiomTreeNode> nodes2 = that.getUserObjectClosure();
        return (nodes1.equals(nodes2));
    }


    /**
     * returns true if this tree contains t as a subtree
     * @param that the tree to compare to
     * @return true if this contains the tree
     */
    public boolean contains(AxiomTree that) {
        return contains(this, that);
    }

    /**
     * recursive method to check whether *that* tree occurs as a subtree in this tree
     * @param tree this tree
     * @param that the subtree to find
     * @return true if 'that' is contained in this tree
     */
    private boolean contains(AxiomTree tree, AxiomTree that) {
        boolean found = false;
        if (sameContent(tree, that)) {
            found = true;
        } else {
            // search tree for occurrence of t
            for (AxiomTree child : tree.getChildTrees()) {
//                Printer.print("Comparing ");
//                Printer.print(that);
//                Printer.print("to ");
//                Printer.print(child);
                found = contains(child, that);
//                Printer.print("Same: " + found);
                if (found) break;

            }
        }
        return found;
    }


}
