package de.ralfhergert.sudoku.solver;

import java.util.*;

/**
 *
 */
public class ArrayGroupConstraints<Symbol> extends GroupConstraint<Symbol> {

	public ArrayGroupConstraints(String name, Collection<Symbol> symbols) {
		super(name, symbols);
	}
}
