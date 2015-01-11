<%@ page import="org.semanticweb.owlapi.util.VersionInfo"%>
<%@ page import="org.semanticweb.owlapi.util.SimpleShortFormProvider"%>
<%@ page import="org.semanticweb.owlapi.model.OWLAxiom"%>
<%@ page import="org.semanticweb.owlapi.model.OWLClassExpression"%>
<%@ page import="org.semanticweb.owl.explanation.api.Explanation"%>
<%@ page import="uk.ac.manchester.cs.owl.justification2html.*" %>
<%@ page import="uk.ac.manchester.cs.owlquery.QueryAnswer"%>
<%@ page import="uk.ac.manchester.cs.owlquery.WebOWLQuery"%>
<%@ page import="java.io.NotSerializableException" %>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.List"%>  

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="header.html"%>
<script type="text/javascript" src="script.js"></script> 

<body>
<div class="content">
	<h1>&nbsp;</h1>
	<h1>Query Results</h1>
<%
	try {
		List<QueryAnswer> answers = (List<QueryAnswer>) session.getAttribute("answers");

		ManchesterSyntaxConverter converter = new ManchesterSyntaxConverter();
		SimpleShortFormProvider sf = new SimpleShortFormProvider();

		int queryCounter = 1, overallJustCounter = 1;
		for (QueryAnswer a : answers) {
			Set<OWLAxiom> ans = a.getAnswers();

			double queryTime = a.getQueryTime();
			String time = "";

			if (queryTime > 100) {
				queryTime = queryTime / 1000;
				time = queryTime + "s";
			} else
				time = queryTime + "ms";

			String htmlQuery = converter.convertToHTML(a.getQuery(), sf);

			out.println("<hr>");
			out.println("<b>Query " + queryCounter + ":</b> " + htmlQuery + "<br/><br/>"); 
			out.println("<b>Results: </b>" + ans.size() + " answers, " + time + "<br/><br/>");

			if (ans.size() > 0) {
				out.println("<table>");
				out.println("<tr><th>Answer Nr.</th><th> ?" + a.getVariable() + "</th><th>Justifications</th></tr>");
				
				int counter = 1;
				for (OWLAxiom ax : ans) {
					OWLClassExpression ce = a.getVariableReplacement(ax);
					boolean atomic = true;
					if(ce.isAnonymous()) atomic = false;
					
					String htmlAx = converter.convertToHTML(a.getVariableReplacement(ax), sf);
					out.println("<tr><td><b>" + counter + "</b>");
					if(!atomic)
						out.println("<div style=\"display:inline;\" class=\"note\" " +
							"onmouseover=\"tooltip.show('Not acquirable via OPPL or SPARQL-OWL');\" onmouseout=\"tooltip.hide();\"><sup>[*]</sup");
					out.println("</td><td>" + htmlAx + "</td>");

					Set<Explanation<OWLAxiom>> justs = a.getJustifications(ax);
					if (!justs.isEmpty()) {
						int justCounter = 1;
						out.println("<td>");
						for (Explanation<OWLAxiom> exp : justs) { 
							String justId = "j" + overallJustCounter;
							String toggleId = "jTrigger" + overallJustCounter;
								
							out.print("Justification " + justCounter + "&nbsp;<a class=\"note\" onclick=\"toggle('" + justId + "','" + toggleId + 
									"');\"><img id=\"" + toggleId + "\" src=\"images/button-closed.png\"/></a>");

							String html = converter.convertToHTML(exp, sf);
							out.print("<div id=\"j" + overallJustCounter + "\" style=\"display:none\">" + html + "</div><br/>");
							
							justCounter++;
							overallJustCounter++;
						}
						out.println("</td></tr>");
					} else {
						out.println("<td><i>Asserted axiom</i></td>");
					}
					counter++;
				}
				out.println("</table><br/>");
			} else
				out.println("<b>No answers found.</b><br/><br/>");

			queryCounter++;
		}

		out.println("<hr>");
	} catch (NotSerializableException e) {
		e.printStackTrace();
		System.out.println("Serializable Exception");
	}
%>
</div>

<div class="box">
	<small>The code for this web application is hosted <a href="https://github.com/rsgoncalves/entquery" target="_blank">here</a>.</small>
	<p>
		<small>Powered by the <a href="http://owlapi.sourceforge.net/" target="_blank">OWL API</a> 
		<%
			String version = VersionInfo.getVersionInfo().getVersion().trim();
			out.print("v" + version);
		%>, FaCT++ v1.5.3, Pellet v2.2.2, HermiT v1.3.6 and JFact v0.9.
		</small>
	</p>
</div>
</body>
</html>