package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;

public abstract class BubbleDocsIntegrator {

	public final void execute() throws BubbleException {
		
		dispatch();
	}
	
	protected abstract void dispatch() throws BubbleException;
}
