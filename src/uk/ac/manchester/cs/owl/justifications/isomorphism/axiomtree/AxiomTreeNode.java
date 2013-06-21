package uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.util.OWLObjectDuplicator;

/**
 * Created by
 * User: Samantha Bail
 * Date: 29/01/2012
 * Time: 00:36
 * The University of Manchester
 */



public class AxiomTreeNode implements Cloneable {

    public enum NodeType {
        AXIOM,
        OBJECT,
        CE,
        CARD,
        EXPLANATION
    }

    private AxiomType axiomType;
    private NodeType nodeType;
    private ClassExpressionType ceType;
    private OWLClassExpression ce; // RG: added
    private OWLObject object;
    private int cardinality;

    public AxiomTreeNode(AxiomType axiomType) {
        this.axiomType = axiomType;
        this.nodeType = NodeType.AXIOM;
    }

    public AxiomTreeNode(ClassExpressionType ceType, OWLClassExpression ce) {
        this.ceType = ceType;
        this.nodeType = NodeType.CE;
        this.ce = ce;
    }

    public AxiomTreeNode(OWLObject object) {
        this.object = object;
        this.nodeType = NodeType.OBJECT;
    }

    public AxiomTreeNode(int cardinality) {
        this.cardinality = cardinality;
        this.nodeType = NodeType.CARD;
    }

    public AxiomTreeNode() {
        this.nodeType = NodeType.EXPLANATION;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        AxiomTreeNode copy = (AxiomTreeNode) super.clone();
        copy.nodeType = this.nodeType;
        switch (this.nodeType) {
            case AXIOM:
                copy.axiomType = this.axiomType;
                break;
            case OBJECT:
                OWLObjectDuplicator dup = new OWLObjectDuplicator(OWLManager.getOWLDataFactory());
                copy.object = dup.duplicateObject(this.object);
                break;
            case CE:
                copy.ceType = this.ceType;
                break;
            case CARD:
                copy.cardinality = this.cardinality;
                break;
        }
        return copy;
    }

    public AxiomType getAxiomType() {
        return axiomType;
    }

    public OWLObject getObject() {
        return object;
    }

    public int getCardinality() {
        return cardinality;
    }

    public ClassExpressionType getCeType() {
        return ceType;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public OWLClassExpression getClassExpression() {
    	return ce;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof AxiomTreeNode)) return false;

        AxiomTreeNode node = (AxiomTreeNode) o;

        if (!node.getNodeType().equals(this.nodeType)) {
            return false;
        }
        // then check by nodetype
        switch (node.nodeType) {
            case AXIOM:
                return (node.getAxiomType().equals(this.axiomType));
            case OBJECT:
                return (node.getObject().equals(this.object));
            case CE:
                return (node.getCeType().equals(this.getCeType()));
            case CARD:
                return (node.getCardinality() == this.cardinality);
        }
        return false;

    }

    @Override
    public int hashCode() {
        int result = axiomType != null ? axiomType.hashCode() : 0;
        result = 31 * result + (nodeType != null ? nodeType.hashCode() : 0);
        result = 31 * result + (ceType != null ? ceType.hashCode() : 0);
        result = 31 * result + (object != null ? object.hashCode() : 0);
        result = 31 * result + cardinality;
        return result;
    }

    @Override
    public String toString() {
        switch (nodeType) {
            case AXIOM:
                return axiomType.toString();
            case OBJECT:
                return object.toString();
            case CE:
                return ceType.toString();
            case CARD:
                return String.valueOf(cardinality);
            default:
                return "Explanation";
        }

    }


}
