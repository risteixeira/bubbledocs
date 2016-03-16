package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class BubbleImportXMLException extends BubbleException {

	private static final long serialVersionUID = 1L;
	private String _xmlSnippet;

	public BubbleImportXMLException(String xmlSnippet) {
		this._xmlSnippet = xmlSnippet;
	}

	public String getXMLSnippet() {
		return this._xmlSnippet;
	}
	
	@Override
	public String getMessage() {
		return "Can't import do XML";
	}
	
}
