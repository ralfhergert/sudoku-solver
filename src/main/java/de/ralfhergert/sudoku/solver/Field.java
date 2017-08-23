package de.ralfhergert.sudoku.solver;

import java.util.*;

/**
 * This is a single field in a Sudoku. It manages all its possibleValues values.
 */
public class Field<Type> {

	private Position position;
	private Type value = null;
	/** All values which are possible at the current game state. */
	private Set<Type> possibleValues = new HashSet<>();
	/** All group constraints this filed is enmeshed in. */
	private List<GroupConstraint<Type>> groups = new ArrayList<>();
	/** All listeners which should be informed about a definition event. */
	private List<FieldValueChangedListener<Type>> listeners = new ArrayList<>();

	public Field(Position position, Collection<Type> objects) {
		this.position = position;
		possibleValues.addAll(objects);
	}

	public Position getPosition() {
		return position;
	}

	public Type getValue() {
		return value;
	}

	public void setValue(Type value) {
		// check if the given value is possible.
		if (!possibleValues.contains(value)) {
			throw new IllegalArgumentException("given value " + value + " is not a possible value for this field " + this);
		}
		this.value = value;
		fireStateEvent();
	}

	public boolean hasValue() {
		return value != null;
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

	public void removePossibilities(Collection<Type> p) {
		for (Type possibleValue : p) {
			removePossibility(possibleValue);
		}
	}

	public void removePossibility(Type i) {
		if (hasValue() && getValue().equals(i)) {
			throw new IllegalArgumentException("given value " + i + " can not be removed from the possible value because it is already the defined value of field " + this);
		}
		if (possibleValues.contains(i)) {
			if (possibleValues.size() == 1) {
				throw new IllegalStateException("can not remove the last possible value of this field");
			}
			possibleValues.remove(i);
		}
	}

	public Set<Type> getPossibilities() {
		return new HashSet<>(possibleValues);
	}

	public boolean isPossible(Type t) {
		return possibleValues.contains(t);
	}

	public void addListener(FieldValueChangedListener<Type> listener) {
		listeners.add(listener);
	}

	public void addGroup(GroupConstraint<Type> group) {
		groups.add(group);
	}

	public List<GroupConstraint<Type>> getGroups() {
		return groups;
	}

	protected void fireStateEvent() {
		for (FieldValueChangedListener<Type> listener : listeners) {
			listener.valueChanged(this);
		}
	}

	@Override
	public String toString() {
		return "Field{position='" + position + "',value=" + value + ",possibleValues=" + possibleValues + '}';
	}
}
