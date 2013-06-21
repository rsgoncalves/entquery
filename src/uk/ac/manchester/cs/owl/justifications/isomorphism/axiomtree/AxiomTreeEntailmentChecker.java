package uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by
 * User: Samantha Bail
 * Date: 20/02/2012
 * Time: 15:07
 * The University of Manchester
 */


public class AxiomTreeEntailmentChecker {

    OWLReasonerFactory rf;
    AxiomTreeBuilder atb = new AxiomTreeBuilder();
    private int flag = 0;

    public AxiomTreeEntailmentChecker(OWLReasonerFactory rf) {
        this.rf = rf;
    }

    /**
     * entailment check entry point
     *
     * @param explanation explanation to check
     * @param mapping     mapping to apply
     * @param flag        flag: o1 or o2 of the mapping (find a nicer alternative for that perhaps?
     * @return true if the axioms entail the entailment, false otherwise
     */
    public boolean isEntailed(Explanation<OWLAxiom> explanation, TreeMapping mapping, int flag) {
        this.flag = flag;
        ////Printer.printHeader("Entailment check with mapping");
        ////Printer.print(mapping);
        ////Printer.print(explanation);
        OWLAxiom unifierEntailment = getUnifierEntailment(explanation.getEntailment(), mapping);
        Set<OWLAxiom> unifierAxioms = getUnifierAxioms(explanation, mapping);
        if (unifierAxioms.size() == explanation.getSize()) {
            return isEntailed(unifierEntailment, unifierAxioms);
        }

        return false;

    }

    /**
     * @param explanation the explanation to use
     * @param mapping     the mapping to use for the modification
     * @return a set of axioms with with mapping applied
     */
    private Set<OWLAxiom> getUnifierAxioms(Explanation<OWLAxiom> explanation, TreeMapping mapping) {
        Set<OWLAxiom> unifierAxioms = new HashSet<OWLAxiom>();
        for (OWLAxiom axiom : explanation.getAxioms()) {
            AxiomTree axiomTree = atb.getAxiomTree(axiom);
            AxiomTree unifierAxiomTree = axiomTree.deepCopyWithMapping(mapping, flag);
//           Printer.print("UNIFIER AX TREE");
//            Printer.print(mapping);
//
//           Printer.print(unifierAxiomTree);
            OWLAxiom ax = unifierAxiomTree.asOWLAxiom();
//           //////Printer.print(ax);
            unifierAxioms.add(ax);

        }
        return unifierAxioms;
    }

    /**
     * @param entailment the entailment to modify
     * @param mapping    the mapping to use for the modification
     * @return an axiom with with mapping applied
     */
    private OWLAxiom getUnifierEntailment(OWLAxiom entailment, TreeMapping mapping) {
        AxiomTree entailmentTree = atb.getAxiomTree(entailment);
        AxiomTree unifierEntailmentTree = entailmentTree.deepCopyWithMapping(mapping, flag);
        return unifierEntailmentTree.asOWLAxiom();
    }

    /**
     * performs the final entailment check
     *
     * @param entailment the entailment
     * @param axioms     the set of axioms
     * @return true if axioms entail entailment, false otherwise
     */
    public boolean isEntailed(OWLAxiom entailment, Set<OWLAxiom> axioms) {
        boolean isEntailed = false;
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLOntology o = manager.createOntology();
            List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
            for (OWLAxiom ax : axioms) {
//               ////////Printer.print(ax);
                changes.add(new AddAxiom(o, ax));
            }
            manager.applyChanges(changes);
            OWLReasoner r = rf.createReasoner(o);
            isEntailed = r.isEntailed(entailment);
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        return isEntailed;
    }

}
