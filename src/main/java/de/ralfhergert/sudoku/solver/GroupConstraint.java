package de.ralfhergert.sudoku.solver;

import java.util.*;

/**
 * A group constraint stretches over multiple fields and ensures that every
 * field in the group has a different value assigned.
 */
public class GroupConstraint<Symbol> implements FieldValueChangedListener<Symbol> {

	private final String name;
	protected final Collection<Symbol> symbols;
	protected List<Field<Symbol>> fields = new ArrayList<>();

	public GroupConstraint(String name, Collection<Symbol> symbols) {
		this.name = name;
		this.symbols = symbols;
	}

	public void add(Field<Symbol> field) {
		fields.add(field);
		field.addListener(this);
		field.addGroup(this);
	}

	public boolean isValid() {
		Collection<Symbol> symbols = new ArrayList<>(this.symbols);
		// check for defined fields first. No symbol should be assigned twice.
		for (Field<Symbol> field : fields) {
			if (field.isDefined()) {
				if (symbols.contains(field.getDefined())) {
					symbols.remove(field.getDefined());
				} else {
					return false; // this symbol is already assigned by another field
				}
			}
		}
		// now check for the undefined fields
		for (Field<Symbol> field : fields) {
			if (!field.isDefined()) {
				Set<Symbol> possibilities = field.getPossibilities();
				possibilities.removeAll(symbols);
				if (!possibilities.isEmpty()) {
					return false; // this field assumed a value as possibility which was already assigned.
				}
			}
		}
		return true;
	}

	/**
	 * This method is called when a field in this group changes its value.
	 */
	public void valueChanged(Field<Symbol> field) {
		// update all other fields in this group.
		if (field.hasValue()) {
			final Symbol definedSymbol = field.getValue();
			for (Field<Symbol> otherField : fields) {
				if (otherField != field) {
					otherField.removePossibility(definedSymbol);
				}
			}
		}
	}

	public List<Field<Symbol>> getFields() {
		return fields;
	}

	public List<Field<Symbol>> getFieldsWithPossibility(Symbol symbol) {
		final List<Field<Symbol>> foundFields = new ArrayList<>();
		for (Field<Symbol> field : fields) {
			if (field.isPossible(symbol)) {
				foundFields.add(field);
			}
		}
		return foundFields;
	}

	@Override
	public String toString() {
		return "GroupConstraint{" +
				"name='" + name + '\'' +
				", fields=" + fields +
				'}';
	}
}
