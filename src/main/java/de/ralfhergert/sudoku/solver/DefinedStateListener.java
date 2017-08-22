package de.ralfhergert.sudoku.solver;

/**
 * This listener catches state event from fields.
 */
public interface DefinedStateListener<Type> {

    void stateChanged(Field<Type> field);
}
