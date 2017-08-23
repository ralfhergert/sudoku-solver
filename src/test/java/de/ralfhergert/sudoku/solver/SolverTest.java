package de.ralfhergert.sudoku.solver;

import org.junit.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Ensure that the {@link Solver} works correctly.
 */
public class SolverTest {

	@Ignore
	public void solveBoard(final String boardDefinition, final boolean expectToBeSolved) throws IOException {
		final SudokuBoard<Integer> board = SudokuBoard.create9by9SudokuBoard();

		// parse the given parameter as board
		StreamToFieldsParser.parse(board, new ByteArrayInputStream(boardDefinition.getBytes()));

		Solver<Integer> solver = new Solver<>(board);
		solver.solve();

		System.out.println();
		board.printToStream(System.out);

		Assert.assertEquals("board should be valid", expectToBeSolved, board.isValid());
		Assert.assertEquals("board should be solved", expectToBeSolved, board.isSolved());
	}

	@Test
	public void testSolveExample1() throws IOException {
		solveBoard("68.....79,...156,.5.8...2,8.9.6.1,..5...3,..4.1.8.5,.7...9.8,...638,34.....56", true);
	}

	@Test
	public void testSolveInvalidExample1() throws IOException {
		solveBoard("8..2,.6..3.2,.9...5,...4..5.3,2.7.6.9.1,5.1..7,...9..48,..4.5..2,.....3..7", false);
	}

	@Test
	public void testSolveExample3() throws IOException {
		solveBoard("....7...1,..8....42,.14..3,4....5.8,..1.2.3,.6.7....9,...9..45,85....6,2...6", true);
	}
}
