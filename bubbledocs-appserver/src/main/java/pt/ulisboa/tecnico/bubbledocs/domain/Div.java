package pt.ulisboa.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleDividedByZeroException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CellDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;

public class Div extends Div_Base {

	Integer result = 0;

	public Div() {
		super();
	}

	public Div(Content c1, Content c2) throws InvalidContentException {
		super();

		if (c1 instanceof Function)
			throw new InvalidContentException(c1.toString());
		else if (c2 instanceof Function)
			throw new InvalidContentException(c2.toString());
		else {
			setContent1(c1);
			setContent2(c2);
		}
	}

	public Integer getValue() throws BubbleDividedByZeroException, CellDoesNotExistException {
		if (getContent1() != null && getContent2() != null)
			if (getContent2().getValue() != 0)
				return getContent1().getValue() / getContent2().getValue();
			else
				throw new BubbleDividedByZeroException();
		return null;
	}

	@Override
	public void delete() {
		this.setContent1(null);
		this.setContent2(null);
		deleteDomainObject();

	}

	@Override
	public Element exportToXML() {
		Element element = new Element("content");
		element.setAttribute("type", "div");
		element.addContent(this.getContent1().exportToXML());
		element.addContent(this.getContent2().exportToXML());

		return element;
	}

	@Override
	public String getDescription() {
		return "Div(" + this.getContent1().getDescription() + "," + this.getContent2().getDescription() + ")";
	}
}
