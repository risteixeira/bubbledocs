package pt.ulisboa.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleDividedByZeroException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CellDoesNotExistException;



public abstract class Content extends Content_Base {

	public abstract Integer getValue() throws BubbleDividedByZeroException, CellDoesNotExistException;
	
	public abstract void delete();

	public abstract Element exportToXML();
	
	public abstract String getDescription();

	
}
