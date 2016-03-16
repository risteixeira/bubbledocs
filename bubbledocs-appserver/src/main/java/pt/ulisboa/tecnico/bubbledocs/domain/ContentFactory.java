package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.ArrayList;

import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleImportXMLException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;

import org.jdom2.Element;
import org.jdom2.DataConversionException;

public class ContentFactory {
	
	public Content getContent(Element content) throws BubbleImportXMLException, DataConversionException, InvalidContentException{
		if(content == null) {
			throw new BubbleImportXMLException("Content is NULL");
		}
		
		if(content.getAttribute("type").getValue().equalsIgnoreCase("LITERAL")){
			Integer val = content.getAttribute("value").getIntValue();
			return new Literal(val);
		}
		
		else if(content.getAttribute("type").getValue().equalsIgnoreCase("REFERENCE")){
			Integer line = content.getAttribute("line").getIntValue();
			Integer column = content.getAttribute("column").getIntValue();
			return new Reference(line, column);
		}
		
		else if(content.getAttribute("type").getValue().equalsIgnoreCase("ADD")){
			ArrayList<Content> args = new ArrayList<Content>();
			for(Element item : content.getChildren()) {
				args.add(this.getContent(item));
			}
			return new Add (args.get(0), args.get(1));
		}
		
		else if(content.getAttribute("type").getValue().equalsIgnoreCase("SUB")){
			ArrayList<Content> args = new ArrayList<Content>();
			for(Element item : content.getChildren()) {
				args.add(this.getContent(item));
			}
			return new Sub (args.get(0), args.get(1));
		}
		
		else if(content.getAttribute("type").getValue().equalsIgnoreCase("MUL")){
			ArrayList<Content> args = new ArrayList<Content>();
			for(Element item : content.getChildren()) {
				args.add(this.getContent(item));
			}
			return new Mul (args.get(0), args.get(1));
		}

		else if(content.getAttribute("type").getValue().equalsIgnoreCase("DIV")){
			ArrayList<Content> args = new ArrayList<Content>();
			for(Element item : content.getChildren()) {
				args.add(this.getContent(item));
			}
			return new Div (args.get(0), args.get(1));
		}
		
		return null;
	}
}
