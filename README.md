sudoku-solver
=============
Solves any kind of SuDoKu puzzles. Puzzles may vary in size and use custom symbols.

#### Usage for solving a square puzzle with 9x9 fields:
The puzzle to solve is given as non-whitespace-containing parameter, starting with the first row of the puzzle. Row are separated by comma(,) or semicolon(;). Empty fields are represented as dot(.) or hyphen(-). Any empty fields at the end of a row must not be defined. The puzzle

    6 8 . . . . . 7 9
    . . . 1 5 6 . . .
    . 5 . 8 . . . 2 .
    8 . 9 . 6 . 1 . .
    . . 5 . . . 3 . .
    . . 4 . 1 . 8 . 5
    . 7 . . . 9 . 8 .
    . . . 6 3 8 . . .
    3 4 . . . . . 5 6
Reads as follows

    java -jar sudoku-solver.jar 68.....79,...156,.5.8...2,8.9.6.1,..5...3,..4.1.8.5,.7...9.8,...638,34.....56
