package uk.ac.manchester.cs.owlquery;

import java.util.Map;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;

/**
 * Author: 	Rafael Gonçalves<br>
 *          The University Of Manchester<br>
 *          Information Management Group<br>
 * Date: 	25-Jul-2012<br><br>
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
