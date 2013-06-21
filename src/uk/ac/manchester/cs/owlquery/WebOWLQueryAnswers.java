package uk.ac.manchester.cs.owlquery;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author: 	Rafael Gonçalves<br>
 *          The University Of Manchester<br>
 *          Information Management Group<br>
 * Date: 	25-Jul-2012<br><br>
 */

@WebServlet("/answers")
public class WebOWLQueryAnswers extends HttpServlet {

	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) 
			throws ServletException, IOException {
		getServletContext().getRequestDispatcher("/answers.jsp").forward(httpServletRequest, httpServletResponse); 
	}


	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) 
			throws ServletException, IOException {
		getServletContext().getRequestDispatcher("/answers.jsp").forward(httpServletRequest, httpServletResponse);
	}
}
