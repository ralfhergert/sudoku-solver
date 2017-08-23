package de.ralfhergert.sudoku.solver;

import java.util.*;

/**
 * This class documents a single iteration in the solution process.
 */
public class SolutionIteration<Symbol> {

	private Map<Field<Symbol>,SetFieldToValue<Symbol>> fieldManipulations = new HashMap<>();

	public void addFieldManipulation(SetFieldToValue<Symbol> fieldToValue) {
		fieldManipulations.put(fieldToValue.getField(), fieldToValue);
	}

	public Collection<SetFieldToValue<Symbol>> getFieldManipulations() {
		return fieldManipulations.values();
	}

	@Override
	public String toString() {
		return "SolutionIteration{" +
				"fieldManipulations=" + fieldManipulations +
				'}';
	}
}
