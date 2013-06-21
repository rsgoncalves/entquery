package uk.ac.manchester.cs.owlquery;

/**
 * @author Rafael S. Goncalves <br/>
 * Information Management Group (IMG) <br/>
 * School of Computer Science <br/>
 * University of Manchester <br/>
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
