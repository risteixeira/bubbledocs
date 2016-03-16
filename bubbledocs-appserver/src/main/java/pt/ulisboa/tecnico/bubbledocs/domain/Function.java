package pt.ulisboa.tecnico.bubbledocs.domain;


import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleDividedByZeroException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CellDoesNotExistException;



public abstract class Function extends Function_Base {
    
    public Function() {
        super();
    }
    
	@Override
	public Integer getValue() throws BubbleDividedByZeroException, CellDoesNotExistException {
		return null;
	}
    
}
