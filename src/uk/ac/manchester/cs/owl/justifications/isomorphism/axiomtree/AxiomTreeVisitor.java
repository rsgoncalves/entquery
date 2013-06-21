package uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree;

import org.semanticweb.owlapi.model.*;

/**
 * Created by
 * User: Samantha Bail
 * Date: 29/01/2012
 * Time: 00:35
 * The University of Manchester
 */


public class AxiomTreeVisitor implements
        OWLAxiomVisitor {

    private AxiomTree axiomTree = null;

    public AxiomTree getTree() {
        return axiomTree;
    }


    public void visit(OWLEquivalentClassesAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);
        for (OWLClassExpression ce : axiom.getClassExpressions()) {
            ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
            ce.accept(v);
            axiomTree.addChild(v.getTree());
        }
    }


    public void visit(OWLSubClassOfAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);

        // get LHS as tree and add to the root
        ClassExpressionTreeVisitor lhs = new ClassExpressionTreeVisitor();
        axiom.getSubClass().accept(lhs);
        axiomTree.addChild(lhs.getTree());

        // get RHS as tree and add to the root
        ClassExpressionTreeVisitor rhs = new ClassExpressionTreeVisitor();
        axiom.getSuperClass().accept(rhs);
        axiomTree.addChild(rhs.getTree());
    }


    public void visit(OWLInverseObjectPropertiesAxiom axiom) throws IllegalStateException {

        System.out.println("INVERSE" + axiom);
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);

        // get LHS as tree and add to the root
        ClassExpressionTreeVisitor lhs = new ClassExpressionTreeVisitor();
        axiom.getFirstProperty().accept(lhs);
        axiomTree.addChild(lhs.getTree());

        // get RHS as tree and add to the root
        ClassExpressionTreeVisitor rhs = new ClassExpressionTreeVisitor();
        axiom.getSecondProperty().accept(rhs);
        axiomTree.addChild(rhs.getTree());
    }


    public void visit(OWLSubDataPropertyOfAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);

        // get LHS as tree and add to the root
        ClassExpressionTreeVisitor lhs = new ClassExpressionTreeVisitor();
        axiom.getSubProperty().accept(lhs);
        axiomTree.addChild(lhs.getTree());

        // get RHS as tree and add to the root
        ClassExpressionTreeVisitor rhs = new ClassExpressionTreeVisitor();
        axiom.getSuperProperty().accept(rhs);
        axiomTree.addChild(rhs.getTree());
    }


    public void visit(OWLDisjointClassesAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);
        for (OWLClassExpression ce : axiom.getClassExpressions()) {
            ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
            ce.accept(v);
            axiomTree.addChild(v.getTree());
        }
    }


    public void visit(OWLDataPropertyDomainAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);

        ClassExpressionTreeVisitor p = new ClassExpressionTreeVisitor();
        axiom.getProperty().accept(p);
        axiomTree.addChild(p.getTree());

        ClassExpressionTreeVisitor c = new ClassExpressionTreeVisitor();
        axiom.getDomain().accept(c);
        axiomTree.addChild(c.getTree());

    }


    public void visit(OWLObjectPropertyDomainAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);

        ClassExpressionTreeVisitor p = new ClassExpressionTreeVisitor();
        axiom.getProperty().accept(p);
        axiomTree.addChild(p.getTree());

        ClassExpressionTreeVisitor c = new ClassExpressionTreeVisitor();
        axiom.getDomain().accept(c);
        axiomTree.addChild(c.getTree());
    }


    public void visit(OWLEquivalentObjectPropertiesAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);
        for (OWLObjectProperty ce : axiom.getObjectPropertiesInSignature()) {
            ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
            ce.accept(v);
            axiomTree.addChild(v.getTree());
        }
    }


    public void visit(OWLDataPropertyRangeAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);

        ClassExpressionTreeVisitor p = new ClassExpressionTreeVisitor();
        axiom.getProperty().accept(p);
        axiomTree.addChild(p.getTree());

        ClassExpressionTreeVisitor c = new ClassExpressionTreeVisitor();
        axiom.getRange().accept(c);
        axiomTree.addChild(c.getTree());
    }


    public void visit(OWLFunctionalDataPropertyAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);
        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        axiom.getProperty().accept(v);
        axiomTree.addChild(v.getTree());
    }


    public void visit(OWLEquivalentDataPropertiesAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);
        for (OWLDataProperty ce : axiom.getDataPropertiesInSignature()) {
            ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
            ce.accept(v);
            axiomTree.addChild(v.getTree());
        }
    }


    public void visit(OWLDisjointDataPropertiesAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);
        for (OWLDataProperty ce : axiom.getDataPropertiesInSignature()) {
            ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
            ce.accept(v);
            axiomTree.addChild(v.getTree());
        }
    }

    public void visit(OWLDisjointObjectPropertiesAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);
        for (OWLObjectProperty ce : axiom.getObjectPropertiesInSignature()) {
            ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
            ce.accept(v);
            axiomTree.addChild(v.getTree());
        }
    }


    public void visit(OWLObjectPropertyRangeAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);

        ClassExpressionTreeVisitor p = new ClassExpressionTreeVisitor();
        axiom.getProperty().accept(p);
        axiomTree.addChild(p.getTree());

        ClassExpressionTreeVisitor c = new ClassExpressionTreeVisitor();
        axiom.getRange().accept(c);
        axiomTree.addChild(c.getTree());
    }


    public void visit(OWLFunctionalObjectPropertyAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);
        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        axiom.getProperty().accept(v);
        axiomTree.addChild(v.getTree());
    }


    public void visit(OWLSubObjectPropertyOfAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);

        // get LHS as tree and add to the root
        ClassExpressionTreeVisitor lhs = new ClassExpressionTreeVisitor();
        axiom.getSubProperty().accept(lhs);
        axiomTree.addChild(lhs.getTree());

        // get RHS as tree and add to the root
        ClassExpressionTreeVisitor rhs = new ClassExpressionTreeVisitor();
        axiom.getSuperProperty().accept(rhs);
        axiomTree.addChild(rhs.getTree());
    }

    public void visit(OWLDisjointUnionAxiom axiom) throws IllegalStateException {

        // TODO: this is not quite right I suppose since that means it would match a disjoint axiom
        OWLDisjointClassesAxiom disjointClassesAxiom = axiom.getOWLDisjointClassesAxiom();
        disjointClassesAxiom.accept(this);
    }


    public void visit(OWLTransitiveObjectPropertyAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);

        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        axiom.getProperty().accept(v);
        axiomTree.addChild(v.getTree());
    }

    public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);

        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        axiom.getProperty().accept(v);
        axiomTree.addChild(v.getTree());
    }

    public void visit(OWLSubPropertyChainOfAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);

        // get property chain as tree and add to the root
        for (OWLPropertyExpression property : axiom.getPropertyChain()) {
            ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
            property.accept(v);
            axiomTree.addChild(v.getTree());
        }
        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        axiom.getSuperProperty().accept(v);
        axiomTree.addChild(v.getTree());

    }


    public void visit(OWLAsymmetricObjectPropertyAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);

        // get LHS as tree and add to the root
        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        axiom.getProperty().accept(v);
        axiomTree.addChild(v.getTree());
    }

    public void visit(OWLReflexiveObjectPropertyAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);

        // get LHS as tree and add to the root
        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        axiom.getProperty().accept(v);
        axiomTree.addChild(v.getTree());
    }


    /**
     * FROM HERE ON DOWNWARDS THE AXIOM TYPES HAVE NOT BEEN IMPLEMENTED
     */


    public void visit(OWLObjectPropertyAssertionAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLDifferentIndividualsAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLDeclarationAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLAnnotationAssertionAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLSubAnnotationPropertyOfAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLAnnotationPropertyDomainAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLAnnotationPropertyRangeAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLHasKeyAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLDatatypeDefinitionAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);

        ClassExpressionTreeVisitor p = new ClassExpressionTreeVisitor();
        axiom.getDatatype().accept(p);
        axiomTree.addChild(p.getTree());

        ClassExpressionTreeVisitor c = new ClassExpressionTreeVisitor();
        axiom.getDataRange().accept(c);
        axiomTree.addChild(c.getTree());
    }

    public void visit(SWRLRule rule) {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLSymmetricObjectPropertyAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);

        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        axiom.getProperty().accept(v);
        axiomTree.addChild(v.getTree());
    }

    public void visit(OWLClassAssertionAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLDataPropertyAssertionAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) throws IllegalStateException {
        AxiomTreeNode root = new AxiomTreeNode(axiom.getAxiomType());
        axiomTree = new AxiomTree(root);

        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        axiom.getProperty().accept(v);
        axiomTree.addChild(v.getTree());
    }

    public void visit(OWLSameIndividualAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

}
