package de.ralfhergert.sudoku.solver;

/** This is a helper class to find the field for a particular row and column. */
public class Position {

	private final int row;
	private final int column;

	Position(int row, int column) {
		this.row = row;
		this.column = column;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Position)) return false;

		Position that = (Position) o;

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
		return "Position{" +
				"row=" + row +
				",col=" + column +
				'}';
	}
}
