package pt.ulisboa.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class Literal extends Literal_Base {
	
	public Literal(Integer val){
		super();
		this.set_value(val);
	}
	
	public String getDescription(){
		return "" + get_value();
	}
	
    public Integer getValue(){
    	return get_value();
    }

	@Override
	public void delete() {
		deleteDomainObject();
	}

	@Override
	public Element exportToXML() {

		Element element = new Element("content");
		element.setAttribute("type", "literal");
		element.setAttribute("value", this.getValue().toString());
		
		return element;
	}
}
