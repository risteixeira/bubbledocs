package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class EmptyUsernameException extends BubbleException {


	private static final long serialVersionUID = 1L;
	
	@Override
	public String getMessage() {
		return "This username is empty";
	}
}
