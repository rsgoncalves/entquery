package uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree;

import org.semanticweb.owlapi.model.*;

/**
 * Created by
 * User: Samantha Bail
 * Date: 30/01/2012
 * Time: 00:04
 * The University of Manchester
 */


public class ClassExpressionTreeVisitor implements
        OWLClassExpressionVisitor,
        OWLPropertyExpressionVisitor,
        OWLDataRangeVisitor {

    private AxiomTree ceTree;

    public AxiomTree getTree() {
        return ceTree;
    }

    public void visit(OWLClass ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }
    }

    public void visit(OWLObjectIntersectionOf ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce.getClassExpressionType(), ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }
        for (OWLClassExpression operand : ce.getOperands()) {
            ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
            operand.accept(v);
            ceTree.addChild(v.getTree());
        }
    }

    public void visit(OWLObjectUnionOf ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce.getClassExpressionType(), ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }
        for (OWLClassExpression operand : ce.getOperands()) {
            ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
            operand.accept(v);
            ceTree.addChild(v.getTree());
        }
    }

    public void visit(OWLObjectComplementOf ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce.getClassExpressionType(), ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }

        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        ce.getOperand().accept(v);
        ceTree.addChild(v.getTree());

    }

    public void visit(OWLObjectSomeValuesFrom ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce.getClassExpressionType(), ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }

        AxiomTreeNode property = new AxiomTreeNode(ce.getProperty());
        ceTree.addChild(new AxiomTree(property));

        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        ce.getFiller().accept(v);
        ceTree.addChild(v.getTree());

    }

    public void visit(OWLObjectAllValuesFrom ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce.getClassExpressionType(), ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }

        AxiomTreeNode property = new AxiomTreeNode(ce.getProperty());
        ceTree.addChild(new AxiomTree(property));

        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        ce.getFiller().accept(v);
        ceTree.addChild(v.getTree());
    }

    public void visit(OWLObjectHasValue ce) throws IllegalStateException {
        // TODO: find out what this is (and datahasvalue)
        throw new IllegalStateException();
    }

    public void visit(OWLObjectMinCardinality ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce.getClassExpressionType(), ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }

        AxiomTreeNode cardinality = new AxiomTreeNode(ce.getCardinality());
        ceTree.addChild(new AxiomTree(cardinality));

        AxiomTreeNode property = new AxiomTreeNode(ce.getProperty());
        ceTree.addChild(new AxiomTree(property));

        if (ce.getFiller() != null) {
            ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
            ce.getFiller().accept(v);
            ceTree.addChild(v.getTree());
        }
    }

    public void visit(OWLObjectExactCardinality ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce.getClassExpressionType(), ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }

        AxiomTreeNode cardinality = new AxiomTreeNode(ce.getCardinality());
        ceTree.addChild(new AxiomTree(cardinality));

        AxiomTreeNode property = new AxiomTreeNode(ce.getProperty());
        ceTree.addChild(new AxiomTree(property));

        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        ce.getFiller().accept(v);
        ceTree.addChild(v.getTree());
    }

    public void visit(OWLObjectMaxCardinality ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce.getClassExpressionType(), ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }

        AxiomTreeNode cardinality = new AxiomTreeNode(ce.getCardinality());
        ceTree.addChild(new AxiomTree(cardinality));

        AxiomTreeNode property = new AxiomTreeNode(ce.getProperty());
        ceTree.addChild(new AxiomTree(property));

        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        ce.getFiller().accept(v);
        ceTree.addChild(v.getTree());
    }

    public void visit(OWLObjectHasSelf ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce.getClassExpressionType(), ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }
        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        ce.getProperty().accept(v);
        ceTree.addChild(v.getTree());
    }


    public void visit(OWLDataSomeValuesFrom ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce.getClassExpressionType(), ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }

        AxiomTreeNode property = new AxiomTreeNode(ce.getProperty());
        ceTree.addChild(new AxiomTree(property));

        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        ce.getFiller().accept(v);
        ceTree.addChild(v.getTree());
    }

    public void visit(OWLDataAllValuesFrom ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce.getClassExpressionType(), ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }

        AxiomTreeNode property = new AxiomTreeNode(ce.getProperty());
        ceTree.addChild(new AxiomTree(property));

        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        ce.getFiller().accept(v);
        ceTree.addChild(v.getTree());
    }

    public void visit(OWLDataHasValue ce) throws IllegalStateException {
//        Printer.printHeader("HAS VALUE");
        throw new IllegalStateException();
    }

    public void visit(OWLDataMinCardinality ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce.getClassExpressionType(), ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }

        AxiomTreeNode cardinality = new AxiomTreeNode(ce.getCardinality());
        ceTree.addChild(new AxiomTree(cardinality));

        AxiomTreeNode property = new AxiomTreeNode(ce.getProperty());
        ceTree.addChild(new AxiomTree(property));

        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        ce.getFiller().accept(v);
        ceTree.addChild(v.getTree());
    }

    public void visit(OWLDataExactCardinality ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce.getClassExpressionType(), ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }

        AxiomTreeNode cardinality = new AxiomTreeNode(ce.getCardinality());
        ceTree.addChild(new AxiomTree(cardinality));

        AxiomTreeNode property = new AxiomTreeNode(ce.getProperty());
        ceTree.addChild(new AxiomTree(property));

        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        ce.getFiller().accept(v);
        ceTree.addChild(v.getTree());
    }

    public void visit(OWLDataMaxCardinality ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce.getClassExpressionType(), ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }

        AxiomTreeNode cardinality = new AxiomTreeNode(ce.getCardinality());
        ceTree.addChild(new AxiomTree(cardinality));

        AxiomTreeNode property = new AxiomTreeNode(ce.getProperty());
        ceTree.addChild(new AxiomTree(property));

        ClassExpressionTreeVisitor v = new ClassExpressionTreeVisitor();
        ce.getFiller().accept(v);
        ceTree.addChild(v.getTree());
    }

    public void visit(OWLDatatype datatype) {
        AxiomTreeNode node = new AxiomTreeNode(datatype);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }
    }


    public void visit(OWLDataComplementOf complement) {
        AxiomTreeNode node = new AxiomTreeNode(complement);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }
    }

    public void visit(OWLDataIntersectionOf ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }
    }

    public void visit(OWLDataUnionOf ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }
    }


    public void visit(OWLObjectProperty property) {
        AxiomTreeNode node = new AxiomTreeNode(property);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }
    }

    public void visit(OWLObjectInverseOf property) {
//TODO: this isn't quite right
       AxiomTreeNode node = new AxiomTreeNode(property);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }
    }

    public void visit(OWLDataProperty property) {
        AxiomTreeNode node = new AxiomTreeNode(property);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }
    }

    public void visit(OWLLiteral literal) {
        AxiomTreeNode node = new AxiomTreeNode(literal);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }
    }

    public void visit(OWLFacetRestriction ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }
    }

    public void visit(OWLObjectOneOf ce) throws IllegalStateException {
        // do nothing - we don't consider nominals for now
    }

    public void visit(OWLDatatypeRestriction ce) throws IllegalStateException {
        AxiomTreeNode node = new AxiomTreeNode(ce);
        if (ceTree == null) {
            ceTree = new AxiomTree(node);
        } else {
            ceTree.addChild(new AxiomTree(node));
        }
    }

    public void visit(OWLDataOneOf node) {
        // do nothing for now
    }

}
