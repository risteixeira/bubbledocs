package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class LoginBubbleDocsException extends BubbleException {

	private static final long serialVersionUID = 1L;
	
	public LoginBubbleDocsException() {}


	public String getMessage() {
		return "The username or password is incorrect";
	}

	
}
