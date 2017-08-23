package de.ralfhergert.sudoku.solver;

import java.io.PrintStream;
import java.util.*;

/**
 * This class builds a SuDoKu board of the desired size.
 */
public class SudokuBoard<Symbol> {

	private final int baseColumnCount;
	private final int baseRowCount;
	private final Collection<Symbol> symbols;
	private final Map<Position,Field<Symbol>> fields = new HashMap<>();
	private final List<GroupConstraint<Symbol>> constraints = new ArrayList<>();

	/**
	 * Creates a SuDoKu board with a size of 9x9 fields.
	 * Allowed symbols are integers [1,9].
	 */
	public static SudokuBoard<Integer> create9by9SudokuBoard() {
		return new SudokuBoard<>(3, 3, Arrays.asList(1,2,3,4,5,6,7,8,9));
	}

	/**
	 * Create a SuDoKu board of the given base size. For instance a base size of 3x3
	 * results in board of (3x3)x(3x3)=9x9. Also non square field like 2x3 are valid.
	 * @param baseColumnCount base size regarding number of columns
	 * @param baseRowCount base size regarding number of rows
	 * @param symbols all available symbols for this board number of symbol must equal
	 *                {@param baseColumnCount}x{@param baseRowCount}.
	 */
	public SudokuBoard(final int baseColumnCount, final int baseRowCount, Collection<Symbol> symbols) {
		this.baseColumnCount = baseColumnCount;
		this.baseRowCount = baseRowCount;
		if (symbols == null) {
			throw new IllegalArgumentException("no available symbols were given");
		}
		if (symbols.size() != baseColumnCount * baseRowCount) {
			throw new IllegalArgumentException("number of symbols is wrong should be " + baseColumnCount * baseRowCount + " but was " + symbols.size());
		}
		this.symbols = symbols;

		// create all fields.
		for (int row = 0; row < baseRowCount*baseRowCount; row++) {
			for (int col = 0; col < baseColumnCount*baseColumnCount; col++) {
				Field<Symbol> field = new Field<>(new Position(row, col), symbols);
				for (Symbol symbol : symbols) {
					field.addPossibility(symbol);
				}
				fields.put(field.getPosition(), field);
			}
		}

		// create all row constraints.
		for (int row = 0; row < baseRowCount*baseRowCount; row++) {
			GroupConstraint<Symbol> constraint = new GroupConstraint<>("Constraint for row " + row, symbols);
			for (int col = 0; col < baseColumnCount*baseColumnCount; col++) {
				constraint.add(fields.get(new Position(row, col)));
			}
			constraints.add(constraint);
		}

		// create all column constraints.
		for (int col = 0; col < baseColumnCount*baseColumnCount; col++) {
			GroupConstraint<Symbol> constraint = new GroupConstraint<>("Constraint for column " + col, symbols);
			for (int row = 0; row < baseRowCount*baseRowCount; row++) {
				constraint.add(fields.get(new Position(row, col)));
			}
			constraints.add(constraint);
		}

		// create all array group constraints.
		for (int rowGroup = 0; rowGroup < baseRowCount; rowGroup += 1) {
			for (int colGroup = 0; colGroup < baseColumnCount; colGroup += 1) {
				GroupConstraint<Symbol> constraint = new ArrayGroupConstraints<>("Array constraint for " + rowGroup + "," + colGroup, symbols);
				for (int row = 0; row < baseRowCount; row++) {
					for (int col = 0; col < baseColumnCount; col++) {
						constraint.add(fields.get(new Position(rowGroup * 3 + row, colGroup * 3 + col)));
					}
				}
				constraints.add(constraint);
			}
		}
	}

	public void setFieldValue(int row, int col, Symbol value) {
		fields.get(new Position(row, col)).setValue(value);
	}

	public Collection<Field<Symbol>> getFields() {
		return fields.values();
	}

	public List<GroupConstraint<Symbol>> getConstraints() {
		return constraints;
	}

	public Collection<Symbol> getSymbols() {
		return symbols;
	}

	public void printToStream(PrintStream stream) {
		for (int row = 0; row < baseRowCount*baseRowCount; row++) {
			for (int col = 0; col < baseColumnCount*baseColumnCount; col++) {
				Field<Symbol> field = fields.get(new Position(row, col));
				stream.print(" " + (field.hasValue() ? field.getValue() : "."));
			}
			stream.println();
		}
	}

	/**
	 * Check whether the board is valid. Which means no constraint on the board is violated.
	 */
	public boolean isValid() {
		for (GroupConstraint<Symbol> constraint : constraints) {
			if (!constraint.isValid()) {
				return false;
			}
		}
		return true; // if this point is reached then no constraint was violated.
	}

	/**
	 * A board is solved when all fields are defined.
	 * @return returns true when this board is solved.
	 */
	public boolean isSolved() {
		for (Field<Symbol> field : fields.values()) {
			if (!field.hasValue()) {
				return false; // one field found which is not defined.
			}
		}
		return true; // all fields are defined.
	}
}
