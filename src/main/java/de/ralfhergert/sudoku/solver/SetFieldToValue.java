package de.ralfhergert.sudoku.solver;

/**
 * This class is used to document the steps to a solution.
 */
public class SetFieldToValue<Symbol> {

	private Field<Symbol> field;
	private Symbol value;

	public SetFieldToValue(Field<Symbol> field, Symbol value) {
		this.field = field;
		this.value = value;
	}

	public Field<Symbol> getField() {
		return field;
	}

	public Symbol getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "SetFieldToValue{" +
				"field=" + field +
				", value=" + value +
				'}';
	}
}
