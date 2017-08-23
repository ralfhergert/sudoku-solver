package de.ralfhergert.sudoku.solver;

/**
 * This listener catches state event from fields.
 */
public interface FieldValueChangedListener<Type> {

	void valueChanged(Field<Type> field);
}
