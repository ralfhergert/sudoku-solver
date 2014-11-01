package de.ralfhergert.sudoku.solver;

import java.util.*;

/**
 *
 */
public class ArrayGroupConstraints<Symbol> extends GroupConstraint<Symbol> {

	public ArrayGroupConstraints(String name, Collection<Symbol> symbols) {
		super(name, symbols);
	}

	@Override
    public void update() {
        super.update();

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

            if (potentialFields.size() > 1) {
                // collect all group in which this fields are
                Set<GroupConstraint<Symbol>> groups = new HashSet<GroupConstraint<Symbol>>();
                for (Field<Symbol> field : potentialFields) {
                    groups.addAll(field.getGroups());
                }

                // remove this group from the set.
                groups.remove(this);

                // now check whether ALL potential fields are also in another group.
                for (GroupConstraint<Symbol> group : groups) {
                    if (group.containsAll(potentialFields)) {
                        // found group: so remove i from all other remaining fields.
                        for (Field<Symbol> field : group.getFields()) {
                            if (!potentialFields.contains(field)) {
                                field.removePossibility(symbol);
                            }
                        }
                    }
                }
            }
        }
    }
}
