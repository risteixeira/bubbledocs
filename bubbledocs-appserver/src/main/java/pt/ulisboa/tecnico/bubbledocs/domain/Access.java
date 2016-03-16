package pt.ulisboa.tecnico.bubbledocs.domain;

import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;

public class Access extends Access_Base {
    
    public Access() {
        super();
    }
    
    public Access(String perm) throws UnauthorizedOperationException{
    	super();
    	if (perm.equals("write") || perm.equals("read")) {
    		this.set_permission(perm);
    	}

    	throw new UnauthorizedOperationException(perm);
    }
    
    public String permissions() {
    	return this.get_permission();
    }
    
    public void delete() {
		set_permission(null);
    	deleteDomainObject();
    }
    
}
