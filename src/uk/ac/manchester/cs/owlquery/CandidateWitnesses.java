package uk.ac.manchester.cs.owlquery;

import java.util.Map;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;

/**
 * @author Rafael S. Goncalves <br/>
 * Information Management Group (IMG) <br/>
 * School of Computer Science <br/>
 * University of Manchester <br/>
 */
public class CandidateWitnesses {

	private String variable;
	private Map<OWLAxiom,OWLClassExpression> cands;
	
	public CandidateWitnesses(String variable, Map<OWLAxiom,OWLClassExpression> cands) {
		this.variable = variable;
		this.cands = cands;
	}
	
	public Map<OWLAxiom,OWLClassExpression> getCandidates() {
		return cands;
	}
	
	public String getVariable() {
		return variable;
	}
}
