package pt.ulisboa.tecnico.bubbledocs.domain;

import org.jdom2.Element;
import org.jdom2.DataConversionException;

import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleDividedByZeroException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleImportXMLException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CellIsProtectedException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;


public class Cell extends Cell_Base {

    public Cell() {
        super();
    }
    
    public Cell(Integer line, Integer column){
    	super();
    	this.set_line(line);
    	this.set_column(column);
    	this.setContent(null);
    }
    
    public Cell(Integer line, Integer column, Content content){
    	super();
    	this.set_line(line);
    	this.set_column(column);
    	this.setContent(content);
    }
    
    public boolean cellProtection() throws CellIsProtectedException{
    	if(this.get_protected() == false)
    		return false;
    	throw new CellIsProtectedException();
    }
    
	public void importFromXML(Element cellElement)
			throws DataConversionException, BubbleImportXMLException,
			BubbleDividedByZeroException, InvalidContentException {
		try{
    	set_line(cellElement.getAttribute("line").getIntValue());
    	set_column(cellElement.getAttribute("column").getIntValue());}
    	catch(DataConversionException e ){
    		 throw new BubbleImportXMLException("");
    	}
    	
    	ContentFactory factory = new ContentFactory();
    	Element content = cellElement.getChild("content");
    	
    	if(content != null) {
    		Content c = factory.getContent(content);
    		setContent(c);
    	}

    }

    public Element exportToXML() throws BubbleDividedByZeroException{
    	Element element = new Element("cell");
    	element.setAttribute("line", Integer.toString(get_line()));
    	element.setAttribute("column", Integer.toString(get_column()));

    	if(this.getContent()!= null){
    		Element contentElement = this.getContent().exportToXML();
    			
    		element.addContent(contentElement);
    	}
    	
    	return element;
    }
    
    public void delete() {
		this.setContent(null);
    	deleteDomainObject();
    }
}
