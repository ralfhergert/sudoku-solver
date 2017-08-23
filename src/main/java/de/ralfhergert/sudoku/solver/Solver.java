package de.ralfhergert.sudoku.solver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class reads and solves a SuDoKu.
 */
public class Solver<Symbol> {

	private final SudokuBoard<Symbol> board;
	private final List<SolutionIteration<Symbol>> solutionIterations = new ArrayList<>();

	public Solver(SudokuBoard<Symbol> board) {
		this.board = board;
	}

	public SudokuBoard<Symbol> solve() {
		while (!board.isSolved()) {
			System.out.println();
			board.printToStream(System.out);

			SolutionIteration<Symbol> iteration = new SolutionIteration<>();
			// find all fields which are defined, but not set.
			for (Field<Symbol> field : board.getFields()) {
				if (field.isDefined() && !field.hasValue()) {
					iteration.addFieldManipulation(new SetFieldToValue<>(field, field.getDefined()));
				}
			}
			// search all the groups whether one symbol is unique for one field of the group.
			for (GroupConstraint<Symbol> group : board.getConstraints()) {
				for (Symbol symbol : board.getSymbols()) {
					List<Field<Symbol>> foundFields = new ArrayList<>();
					for (Field<Symbol> field : group.getFields()) {
						if (!field.hasValue() && field.isPossible(symbol)) {
							foundFields.add(field);
						}
					}
					if (foundFields.size() == 1) {
						Field<Symbol> foundField = foundFields.get(0);
						if (!foundField.hasValue()) {
							iteration.addFieldManipulation(new SetFieldToValue<>(foundFields.get(0), symbol));
						}
					}
				}
			}
			// break if no defined fields were found.
			if (iteration.getFieldManipulations().isEmpty()) {
				break;
			}
			// perform all manipulation.
			for (SetFieldToValue<Symbol> manipulation : iteration.getFieldManipulations()) {
				try {
					manipulation.getField().setValue(manipulation.getValue());
				} catch (IllegalStateException e) {
					return board;
				}
			}

			// add this iteration to the solution iterations.
			solutionIterations.add(iteration);
		}
		return board;
	}

	public List<SolutionIteration<Symbol>> getSolutionIterations() {
		return solutionIterations;
	}

	public static void main(String args[]) throws IOException {
		final SudokuBoard<Integer> board = SudokuBoard.create9by9SudokuBoard();

		// parse the given parameter as board
		if (args.length > 0) {
			StreamToFieldsParser.parse(board, new ByteArrayInputStream(args[0].getBytes()));
		}

		new Solver<>(board).solve();

		board.printToStream(System.out);
	}
}
