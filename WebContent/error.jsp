<%@ page import="org.semanticweb.owlapi.util.VersionInfo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" errorPage="/ErrorDisplay" import="java.io.*" %>
<%@ include file="header.html" %>

<body>
<div class="content">
	<h1>&nbsp;</h1>
    <h1>Error</h1>
    <hr>
	<h3>The following error occurred during execution:</h3>
        <% 
        if(exception.getCause() != null) {
        	out.println(exception.toString() + ", caused by: <br>");
        	out.println(exception.getCause() + "<br>");
        }
        else
        	out.println(exception.toString() + "<br>");
        %>
        <br>
        <hr>
        <b>If the error persists (where applicable after fixing the problem), please contact the administrator.<br> 
        For bug reports include the error message, a copy of the stack trace below, and the necessary input to replicate the issue.</b>
        <br>
        <%
        /* out.println("<!--"); */
       	StringWriter sw = new StringWriter();
       	PrintWriter pw = new PrintWriter(sw);
       	exception.printStackTrace(pw);
       	out.println("<br>");
		out.print(sw);
      	sw.close();
		pw.close();
       		/* out.println("-->"); */
        %>
        <br><hr><br>
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