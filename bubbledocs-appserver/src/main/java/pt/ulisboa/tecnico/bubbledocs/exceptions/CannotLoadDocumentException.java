package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class CannotLoadDocumentException extends BubbleException {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getMessage() {
		return "This document can't be loaded";
	}

}
