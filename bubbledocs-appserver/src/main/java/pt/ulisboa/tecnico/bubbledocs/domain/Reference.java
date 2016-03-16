package pt.ulisboa.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleDividedByZeroException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CellDoesNotExistException;

public class Reference extends Reference_Base {
	
	public Reference(Cell c){
		super();
		this.setCellRef(c);
		this.set_line(this.getCellRef().get_line());
		this.set_column(this.getCellRef().get_column());
	}
	
	public Reference(Integer line, Integer column){
		super();
		this.set_line(line);
		this.set_column(column);
	}
	
	public String getDescription(){
		return this.get_line() + ";" + this.get_column();
	}
	
    public Integer getValue() throws BubbleDividedByZeroException, CellDoesNotExistException{
		this.setCellRef(this.getCell().getSpreadsheet()
				.getCellByLinCol(this.get_line(), this.get_column()));
    	Cell _cell = this.getCellRef();
    	Content _content = _cell.getContent();
    	return _content.getValue();
    }
    
    public String validate() throws CellDoesNotExistException{

		String string = this.getDescription();
		String[] parts = string.split(";");
		
		this.setCellRef(this.getCell().getSpreadsheet()
				.getCellByLinCol(this.get_line(), this.get_column()));

		if (parts[0].equals(null) || parts[1].equals(null))
			return "#VALUE";

		else if (Integer.parseInt(parts[0]) > this.getCell().getSpreadsheet()
				.get_line()
				|| Integer.parseInt(parts[1]) > this.getCell().getSpreadsheet()
						.get_column())
			return "#VALUE";

		else if (this.getCellRef().getContent() == null)
			return "#VALUE";

		else
			return "valid reference";

    }


	@Override
	public void delete() {
		this.setCellRef(null);
		this.setCell(null);
		deleteDomainObject();
		
	}

	@Override
	public Element exportToXML() {
		Element element = new Element("content");
		element.setAttribute("type", "reference");
		element.setAttribute("line", this.get_line().toString());
		element.setAttribute("column", this.get_column().toString());
		
		return element;
	}
    
}
