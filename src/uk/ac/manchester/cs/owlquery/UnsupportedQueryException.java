package uk.ac.manchester.cs.owlquery;

/**
 * Author: 	Rafael Gonçalves<br>
 *          The University Of Manchester<br>
 *          Information Management Group<br>
 * Date: 	25-Jul-2012<br><br>
 */

public class UnsupportedQueryException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnsupportedQueryException() {
		super();
	}
	
	public UnsupportedQueryException(String msg) {
		super(msg);
	}
	
	public UnsupportedQueryException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public UnsupportedQueryException(Throwable cause) {
		super(cause);
	}
}
