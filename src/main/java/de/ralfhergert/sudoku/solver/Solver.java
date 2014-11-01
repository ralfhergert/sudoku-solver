package de.ralfhergert.sudoku.solver;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * This class reads and solves a SuDoKu.
 */
public class Solver {

    public static void main(String args[]) throws IOException {
		final SudokuBoard<Integer> board = new SudokuBoard<Integer>();

		// parse the given parameter as board
		if (args.length > 0) {
			StreamToFieldsParser.parse(board, new ByteArrayInputStream(args[0].getBytes()));
		}

		board.printToStream(System.out);
    }
}
