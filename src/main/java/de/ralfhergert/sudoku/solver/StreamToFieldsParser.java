package de.ralfhergert.sudoku.solver;

import java.io.IOException;
import java.io.InputStream;

/**
 * This parser parses an {@link java.io.InputStream} an sets a board.
 */
public class StreamToFieldsParser {

    public static void parse(SudokuBoard<Integer> board, InputStream stream) throws IOException {
        int row = 0;
        int col = 0;

        int chr;
        while ((chr = stream.read()) != -1) {
            char character = (char)chr;
            if (character == '.' || character == '-') {
                col++;
            } else if (character == ',' || character == ';') {
                row++;
                col = 0;
            } else if (character >= '1' && character <= '9') {
                board.setFieldValue(row, col, Integer.parseInt(String.valueOf(character)));
                col++;
            }
        }
    }
}
