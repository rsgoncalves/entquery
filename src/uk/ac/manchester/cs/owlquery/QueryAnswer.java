package uk.ac.manchester.cs.owlquery;

import java.io.StringWriter;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxObjectRenderer;

/**
 * @author Rafael S. Goncalves <br/>
 * Information Management Group (IMG) <br/>
 * School of Computer Science <br/>
 * University of Manchester <br/>
 */
public class QueryAnswer {

	private Map<OWLAxiom,Set<Explanation<OWLAxiom>>> justs;
	private Map<OWLAxiom,OWLClassExpression> ax_ce_map;
	private OWLAxiom query;
	private double queryTime;
	private String variable;
	
	
	/*
	 * Constructor
	 */
	public QueryAnswer(OWLAxiom query, String variable, Map<OWLAxiom,Set<Explanation<OWLAxiom>>> justs, Map<OWLAxiom,OWLClassExpression> ax_ce_map) {
		this.query = query;
		this.justs = justs;
		this.ax_ce_map = ax_ce_map;
		this.variable = variable;
	}
	
	
	/*
	 * Get query axiom
	 */
	public OWLAxiom getQuery() {
		return query;
	}
	
	
	/*
	 * Get query answers (axioms)
	 */
	public Set<OWLAxiom> getAnswers() {
		return justs.keySet();
	}
	
	
	/*
	 * Get variable
	 */
	public String getVariable() {
		return variable;
	}
	
	
	/*
	 * Get variable replacement
	 */
	public OWLClassExpression getVariableReplacement(OWLAxiom ax) {
		return ax_ce_map.get(ax);
	}
	
	
	/*
	 * Get justifications for an axiom
	 */
	public Set<Explanation<OWLAxiom>> getJustifications(OWLAxiom ax) {
		if(justs.containsKey(ax))
			return justs.get(ax);
		else
			return null;
	}
	
	
	/*
	 * Get query execution time
	 */
	public double getQueryTime() {
		return queryTime;
	}
	
	
	/*
	 * Set query time
	 */
	public void setQueryTime(double d) {
		queryTime = d;
	}
	
	@Override
	public String toString() {
		String out = "\n---------------------------------------\n" + "Query: " + getManchesterSyntax(query) +
				"\n---------------------------------------\n";
		if(!justs.isEmpty()) {
			out += " Answers (" + justs.size() + "):\n";
			int counter = 1;
			for(OWLAxiom ax : justs.keySet()) {
				out += "  (" + counter + ") " + getManchesterSyntax(ax) + "\n";
				
				Set<Explanation<OWLAxiom>> exps = justs.get(ax);
				if(!exps.isEmpty()) {
					int justCounter = 1;
					for(Explanation<OWLAxiom> exp : justs.get(ax)) {
						out += "\tJustification " + justCounter + ":\n";
						OWLAxiom ent = exp.getEntailment();
						for(OWLAxiom exp_ax : exp.getAxioms()) {
							if(exp_ax != ent)
								out += "\t   " + getManchesterSyntax(exp_ax) + "\n";
							else 
								out += "\t   [asserted] " + getManchesterSyntax(exp_ax) + "\n";
						}
						justCounter ++;
					}
					counter++;
				}
				else {
					out += "\tAsserted axiom\n";
					counter++;
				}
			}
		}
		else
			out += " No entailments found.";
		
		return out;
	}
	
	
	/*
	 * Get Manchester syntax rendering of an object
	 */
	public String getManchesterSyntax(OWLObject obj) {
		SimpleShortFormProvider fp = new SimpleShortFormProvider();
		StringWriter wr = new StringWriter();

		ManchesterOWLSyntaxObjectRenderer render = new ManchesterOWLSyntaxObjectRenderer(wr, fp);
		render.setUseWrapping(false);
		obj.accept(render);

		String str = wr.getBuffer().toString();

		return str;
	}
}
