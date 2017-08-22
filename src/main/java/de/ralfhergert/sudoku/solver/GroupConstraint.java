package de.ralfhergert.sudoku.solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A group constraint stretches over multiple fields and ensures that every
 * field in the group has a different value assigned.
 */
public class GroupConstraint<Symbol> implements DefinedStateListener<Symbol> {

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

    public void update() {
        for (Symbol symbol : symbols) {
            List<Field<Symbol>> potentialFields = new ArrayList<Field<Symbol>>();
            for (Field<Symbol> field : fields) {
                if (field.isPossible(symbol)) {
                    if (field.isDefined()) {
                        break; // skip this field because it already defines this i
                    }
                    potentialFields.add(field);
                }
            }

            if (potentialFields.size() == 1) {
                potentialFields.get(0).setValue(symbol);
            }
        }
    }

    public void stateChanged(Field<Symbol> field) {
		// ensure that no other field in this constraint group has this value.
		if (field.isDefined()) { // check first that the firing field is defined now.
			final Symbol definedSymbol = field.getDefined();
			for (Field<Symbol> otherField : fields) {
				if (otherField != field) {
					otherField.removePossibility(definedSymbol);
				}
			}
		}

        //update();
    }

    public boolean containsAll(Collection<Field<Symbol>> fields) {
        for (Field<Symbol> field : fields) {
            if (!this.fields.contains(field)) {
                 return false;
            }
        }
        return true;
    }

    public List<Field<Symbol>> getFields() {
        return fields;
    }

	@Override
	public String toString() {
		return "GroupConstraint{" +
				"name='" + name + '\'' +
				", fields=" + fields +
				'}';
	}
}
