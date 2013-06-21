package uk.ac.manchester.cs.owlquery;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import uk.ac.manchester.cs.owl.justifications.isomorphism.axiomtree.AxiomTree;

/**
 * @author Rafael S. Goncalves <br/>
 * Information Management Group (IMG) <br/>
 * School of Computer Science <br/>
 * University of Manchester <br/>
 */
public class Variable {

	private OWLObject obj;
	private AxiomTree tree;
	
	public Variable(OWLObject obj, AxiomTree tree) {
		this.obj = obj;
		this.tree = tree;
	}
	
	public OWLObject getVariable() {
		return obj;
	}
	
	public AxiomTree getAxiomTree() {
		return tree;
	}
	
	public OWLClass getVariableAsOWLClass() {
		if(isConceptVariable())
			return (OWLClass) obj;
		else
			return null;
	}
	
	public OWLObjectProperty getVariableAsOWLObjectProperty() {
		if(isRoleVariable())
			return (OWLObjectProperty) obj;
		else
			return null;
	}
	
	public boolean isConceptVariable() {
		if(obj instanceof OWLClass)
			return true;
		else 
			return false;
	}
	
	public boolean isRoleVariable() {
		if(obj instanceof OWLObjectProperty)
			return true;
		else 
			return false;
	}
}
