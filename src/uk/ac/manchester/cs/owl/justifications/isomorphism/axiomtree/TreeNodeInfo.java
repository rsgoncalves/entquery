package uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import uk.ac.manchester.cs.owlquery.Variable;

/**
 * @author Rafael S. Goncalves <br/>
 * Information Management Group (IMG) <br/>
 * School of Computer Science <br/>
 * University of Manchester <br/>
 */
public class TreeNodeInfo {

	private boolean isFixed;
	private OWLClassExpression ce;
	private Set<AxiomTree> subtrees;
	private Variable conceptVariable, roleVariable;
	
	public TreeNodeInfo(OWLClassExpression ce) {
		this.ce = ce;
		conceptVariable = null;
		roleVariable = null;
		subtrees = new HashSet<AxiomTree>();
	}
	
	
	/*
	 * Add methods
	 */
	public void addSubTree(AxiomTree t) {
		subtrees.add(t);
	}
	
	public void addClassVariable(OWLClass c, AxiomTree t) {
		conceptVariable = new Variable(c, t);
	}
	
	public void addRoleVariable(OWLObjectProperty op, AxiomTree t) {
		roleVariable = new Variable(op, t);
	}
	
	
	/*
	 * Get methods
	 */
	public Set<OWLEntity> getSignature() {
		return ce.getSignature();
	}
	
	public OWLObject getVariable() {
		if(conceptVariable != null) return conceptVariable.getVariable();
		else if(roleVariable != null) return roleVariable.getVariable();
		else return null;
	}
	
	public OWLObjectProperty getRoleVariable() {
		return roleVariable.getVariableAsOWLObjectProperty();
	}
	
	public OWLClass getConceptVariable() {
		return conceptVariable.getVariableAsOWLClass();
	}
	
	
	public AxiomTree getAxiomTree(OWLClass c) {
		return conceptVariable.getAxiomTree();
	}
	
	public AxiomTree getAxiomTree(OWLObjectProperty p) {
		return roleVariable.getAxiomTree();
	}
	
	
	public Set<AxiomTree> getSubTrees() {
		return subtrees;
	}
	
	public OWLClassExpression getClassExpression() {
		return ce;
	}
	
	public OWLClass getAsOWLClass() {
		if(isOWLClass())
			return ce.asOWLClass();
		else
			return null;
	}
	
	
	public boolean hasRoleVariable() {
		if(roleVariable != null)
			return true;
		else
			return false;
	}
	
	
	public boolean hasConceptVariable() {
		if(conceptVariable != null)
			return true;
		else
			return false;
	}
	
	public void setFixed(boolean fixed) {
		isFixed = fixed;
	}
	
	public boolean isFixed() {
		return isFixed;
	}
	
	public boolean isOWLClass() {
		if(ce instanceof OWLClass)
			return true;
		else
			return false;
	}
}
