package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.Set;

import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.joda.time.LocalDateTime;

import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleDividedByZeroException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleImportXMLException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CellDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidSpreadsheetException;

public class SpreadSheet extends SpreadSheet_Base {

	public SpreadSheet() {
		super();
	}

	public SpreadSheet(Integer id, String sheetname, Integer lines,
			Integer columns, String owner) throws InvalidSpreadsheetException {
		super();

		if (lines <= 0 && columns <= 0)
			throw new InvalidSpreadsheetException(sheetname);

		Integer sheetCountObj = BubbleDocs.getInstance().get_sheetCounter();
		LocalDateTime dateObj = new LocalDateTime();
		String date = dateObj.toString();
		
		this.set_id(id);
		this.set_name(sheetname);
		this.set_line(lines);
		this.set_column(columns);
		this.set_date(date);
		this.set_owner(owner);
		BubbleDocs.getInstance().set_sheetCounter(sheetCountObj++);

	}

	public SpreadSheet(String sheetname, Integer lines, Integer columns,
			String owner) throws InvalidSpreadsheetException {
		super();

		if (lines <= 0 && columns <= 0)
			throw new InvalidSpreadsheetException(sheetname);

		LocalDateTime dateObj = new LocalDateTime();
		String date = dateObj.toString();
		
		this.set_name(sheetname);
		this.set_line(lines);
		this.set_column(columns);
		this.set_owner(owner);
		this.set_id(BubbleDocs.getInstance().get_sheetCounter());
		this.set_date(date);
		BubbleDocs.getInstance().incrSheetCounter();

	}

	public Access getAccessbyUser(String usrname) {

		Set<Access> accessSet = this.getAccessSet();

		for (Access acs : accessSet) {
			if (acs.getUser().get_name().equals(usrname))
				return acs;
		}
		return null;
	}

	public void importFromXML(Element spreadSheetElement, String username)
			throws BubbleDividedByZeroException, InvalidContentException {

		try {
			set_line(spreadSheetElement.getAttribute("lines").getIntValue());
			set_column(spreadSheetElement.getAttribute("columns").getIntValue());
			//set_id(spreadSheetElement.getAttribute("id").getIntValue());
			set_name(spreadSheetElement.getAttribute("name").getValue());
			set_date(spreadSheetElement.getAttribute("date").getValue());
			set_owner(spreadSheetElement.getAttribute("owner").getValue());

			Element cells = spreadSheetElement.getChild("cells");

			for (Element cellsElement : cells.getChildren("cell")) {
				Cell c = new Cell();
				c.importFromXML(cellsElement);
				addCell(c);
			}
		} catch (DataConversionException e) {
			e.printStackTrace();
			System.out.println("Error in importing SpreadSheet from Xml");
		} catch (BubbleImportXMLException e) {
			e.printStackTrace();
		}
	}

	public Element exportToXML() throws BubbleDividedByZeroException {
		Element element = new Element("spreadsheet");

		element.setAttribute("name", get_name());
		element.setAttribute("lines", Integer.toString(get_line()));
		element.setAttribute("columns", Integer.toString(get_column()));
		element.setAttribute("date", get_date());
		element.setAttribute("owner", get_owner());
		//element.setAttribute("id", Integer.toString(get_id()));

		Element cellsElement = new Element("cells");
		element.addContent(cellsElement);

		for (Cell c : getCellSet()) {
			cellsElement.addContent(c.exportToXML());
		}

		return element;

	}

	public void delete() {

		for (Cell item : getCellSet()) {
			removeCell(item);
			item.delete();
		}

		for (Access item : getAccessSet()) {
			removeAccess(item);
			item.delete();
		}
		deleteDomainObject();
	}

	public Cell getCellByLinCol(int line, int column)
			throws CellDoesNotExistException {
		for (Cell c : this.getCellSet()) {
			if (c.get_line() == line && c.get_column() == column) {
				return c;
			}
		}
		throw new CellDoesNotExistException(line+";"+column);
	}

}
