<%@ page import="org.semanticweb.owlapi.util.VersionInfo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.html"%>

<script lang="JavaScript">
function JumpToIt(frm) {
    var newPage = frm.url.options[frm.url.selectedIndex].value;
    if (newPage != "None") {
        location.href=newPage;
    }
}
function setSampleURLs() {
	ont.value = "http://www.rsgoncalves.com/entquery/ont.owl";
	queries.value = "A SubClassOf: r some ?x\n?y EquivalentTo: C\nE SubClassOf: ?z";
}
</script>

<body>
<div class="content">
	<h1>&nbsp;</h1>
	<h1>Querying OWL 2 ontologies</h1>
	<h3>&nbsp;&nbsp;&nbsp;Prototype query processor for entailment regimes beyond SPARQL 1.1</h3>
    <form action="webowlquery" method="post" enctype="multipart/form-data">
        <div class="box">
        	Try me: <input type="button" value="Use sample input" onclick="setSampleURLs()"><br/>
        	<br/>
            <h3>Source Ontology</h3>
            <p>Browse for your ontology <b>file</b>, or enter a <b>URL</b> for the ontology to be queried.</p>
            <label>
                <textarea rows="3" cols="120" name="ont" id="ont"></textarea>
            </label>
            <label>
            	<br/>
            	<input type="file" name="ontfile"> 
            </label>
            <br/><br/><br/>
            <h3>Query</h3>
            <p>
                Browse for a query <b>file</b> (an ontology where variables are encoded as entity names under the name-<br/>
                space <i><b>http://semanticweb.org/variable</b></i>. Alternatively, use the text area to formulate a query<br/>
               (in <b>Manchester syntax</b>) with entity names as declared in the input, and variables as, e.g. '?x'.<br/>
            </p>
            <label>
               	<textarea rows="5" cols="120" name="queries" id="queries"></textarea>
            </label>
            <label>
            	<br/>
            	<input type="file" name="queryfile"> 
            </label>
        </div>
        <div class="box">
			Reasoner: <select name="reasoner">
				<!-- <option value="Fact">FaCT++</option> -->
				<option value="Pellet">Pellet</option>
				<option value="Hermit">HermiT</option>
				<option value="JFact">JFact</option>
			</select><br/>
			<input type="checkbox" name="inferredOnly" value="true"> Inferred answers only<br/>
			<input type="checkbox" name="extendedGrammar" value="true"> Use extended grammar (replace variables with: C and D, C or D, r some C, r only C, not C)<br/><br/>
            <input type="submit" value="Execute Query">
		</div>
    </form><br/>
</div>

<div class="box">
	<small>The query processor requires one side of the query axiom to be bounded, e.g.:<br/>
	<ul>
		<li>
			<i><b>?x SubClassOf A</b></i><br/>
		</li>
		<li>
			<i><b>B SubClassOf p some ?y</b></i>
		</li>
	</ul>
	</small>
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
