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
	private final Map<FieldIndex,Field<Symbol>> fields = new HashMap<FieldIndex, Field<Symbol>>();
	private final List<GroupConstraint<Symbol>> constraints = new ArrayList<GroupConstraint<Symbol>>();

	/**
	 * Creates a SuDoKu board with a size of 9x9 fields.
	 * Allowed symbols are integers [1,9].
	 */
	public SudokuBoard() {
		this(3, 3, (Collection<Symbol>)Arrays.asList(1,2,3,4,5,6,7,8,9));
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
				Field<Symbol> field = new Field<Symbol>();
				for (Symbol symbol : symbols) {
					field.addPossibility(symbol);
				}
				fields.put(new FieldIndex(row, col), field);
			}
		}

		// create all row constraints.
		for (int row = 0; row < baseRowCount*baseRowCount; row++) {
			GroupConstraint<Symbol> constraint = new GroupConstraint<Symbol>("Constraint for row " + row, symbols);
			for (int col = 0; col < baseColumnCount*baseColumnCount; col++) {
				constraint.add(fields.get(new FieldIndex(row, col)));
			}
			constraints.add(constraint);
		}

		// create all column constraints.
		for (int col = 0; col < baseColumnCount*baseColumnCount; col++) {
			GroupConstraint<Symbol> constraint = new GroupConstraint<Symbol>("Constraint for column " + col, symbols);
			for (int row = 0; row < baseRowCount*baseRowCount; row++) {
				constraint.add(fields.get(new FieldIndex(row, col)));
			}
			constraints.add(constraint);
		}

		// create all array group constraints.
		for (int rowGroup = 0; rowGroup < baseRowCount; rowGroup += 1) {
			for (int colGroup = 0; colGroup < baseColumnCount; colGroup += 1) {
				GroupConstraint<Symbol> constraint = new ArrayGroupConstraints<Symbol>("Array constraint for " + rowGroup + "," + colGroup, symbols);
				for (int row = 0; row < baseRowCount; row++) {
					for (int col = 0; col < baseColumnCount; col++) {
						constraint.add(fields.get(new FieldIndex(rowGroup * 3 + row, colGroup * 3 + col)));
					}
				}
				constraints.add(constraint);
			}
		}
	}

	public void setFieldValue(int row, int col, Symbol value) {
		fields.get(new FieldIndex(row, col)).setValue(value);
	}

	public void printToStream(PrintStream stream) {
		for (int row = 0; row < baseRowCount*baseRowCount; row++) {
			for (int col = 0; col < baseColumnCount*baseColumnCount; col++) {
				Field<Symbol> field = fields.get(new FieldIndex(row, col));
				stream.print(" " + (field.isDefined() ? field.getDefined() : "."));
			}
			stream.println();
		}
	}

	/** This is a helper class to find the field for a particular row and column. */
	private static class FieldIndex {

		private final int row;
		private final int column;

		private FieldIndex(int row, int column) {
			this.row = row;
			this.column = column;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof FieldIndex)) return false;

			FieldIndex that = (FieldIndex) o;

			if (column != that.column) return false;
			if (row != that.row) return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = row;
			result = 31 * result + column;
			return result;
		}

		@Override
		public String toString() {
			return "FieldIndex{" +
					"row=" + row +
					",col=" + column +
					'}';
		}
	}
}
