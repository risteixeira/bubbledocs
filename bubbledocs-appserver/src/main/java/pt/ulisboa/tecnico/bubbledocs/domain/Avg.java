package pt.ulisboa.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class Avg extends Avg_Base {
    
    public Avg() {
        super();
    }

	@Override
	public Integer getValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete() {
		deleteDomainObject();
		
	}

	@Override
	public Element exportToXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
