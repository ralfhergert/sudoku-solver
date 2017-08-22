package de.ralfhergert.sudoku.solver;

import java.util.*;

/**
 * This is a single field in a Sudoku. It manages all its possibleValues values.
 */
public class Field<Type> {

	/** All values which are possible at the current game state. */
	private Set<Type> possibleValues = new HashSet<>();
	/** All group constraints this filed is enmeshed in. */
	private List<GroupConstraint<Type>> groups = new ArrayList<>();
	/** All listeners which should be informed about a definition event. */
	private List<DefinedStateListener<Type>> listeners = new ArrayList<>();

    public Field(Type... objects) {
		Collections.addAll(possibleValues, objects);
    }

    public boolean isDefined() {
        return possibleValues.size() == 1;
    }

    public Type getDefined() {
        return isDefined() ? possibleValues.iterator().next() : null;
    }

	public void addPossibility(Type i) {
		possibleValues.add(i);
	}

    public void removePossibility(Type i) {
		if (!possibleValues.contains(i)) {
			return; // can not remove value if it is not anymore a possible value of this field.
		}
		if (possibleValues.size() == 1) {
			return; // can not remove the last possible value of this field.
		}
		if (possibleValues.remove(i)) {
            // check if now this field is defined.
            if (possibleValues.size() == 1) {
                fireStateEvent();
            }
        }
    }

    public boolean isPossible(Type t) {
        return possibleValues.contains(t);
    }

    public void setValue(Type value) {
        // remove all other possibilities.
        List<Type> removables = new ArrayList<Type>(possibleValues);
        if (!removables.remove(value)) {
            throw new IllegalArgumentException("given type is not a possible value for this field.");
        }
        for (Type t : removables) {
            removePossibility(t);
        }
    }

    public void addListener(DefinedStateListener<Type> listener) {
        listeners.add(listener);
    }

    public void addGroup(GroupConstraint<Type> group) {
        groups.add(group);
    }

    public List<GroupConstraint<Type>> getGroups() {
        return groups;
    }

    protected void fireStateEvent() {
        for (DefinedStateListener<Type> listener : listeners) {
            listener.stateChanged(this);
        }
    }

	@Override
	public String toString() {
		return "Field{" +
				"values=" + possibleValues +
				'}';
	}
}
