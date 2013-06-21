package uk.ac.manchester.cs.owlquery;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Rafael S. Goncalves <br/>
 * Information Management Group (IMG) <br/>
 * School of Computer Science <br/>
 * University of Manchester <br/>
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
