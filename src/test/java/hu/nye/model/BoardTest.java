package hu.nye.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        // Initialize the board with standard Connect 4 size (6 rows, 7 columns)
        board = new Board(6, 7);
    }

    @Test
    void testBoardInitialization() {
        // Test that the board is initialized with the correct number of rows and columns
        assertEquals(6, board.getRows(), "Board should have 6 rows");
        assertEquals(7, board.getColumns(), "Board should have 7 columns");

        // Check that the board is initially empty
        assertFalse(board.isFull(), "Board should not be full on initialization");
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getColumns(); col++) {
                assertEquals(' ', board.getGrid()[row][col], "The grid should be empty initially");
            }
        }
    }

    @Test
    void testDropDiscValidMove() {
        // Test dropping a disc in a valid column
        assertTrue(board.dropDisc(0, 'Y'), "Dropping disc in column 0 should succeed");
        assertEquals('Y', board.getGrid()[5][0], "The bottom-most cell in column 0 should contain 'Y'");
    }

    @Test
    void testDropDiscInvalidMove() {
        // Fill column 0 and then attempt to drop another disc
        for (int i = 0; i < 6; i++) {
            board.dropDisc(0, 'Y'); // Fill the column
        }
        assertFalse(board.dropDisc(0, 'R'), "Dropping disc in full column 0 should fail");
    }

    @Test
    void testCheckWinHorizontal() {
        // Simulate a horizontal win
        board.dropDisc(0, 'Y');
        board.dropDisc(1, 'Y');
        board.dropDisc(2, 'Y');
        board.dropDisc(3, 'Y');

        assertTrue(board.checkWin('Y'), "Y should win with a horizontal connection");
    }

    @Test
    void testCheckWinVertical() {
        // Simulate a vertical win
        for (int i = 0; i < 4; i++) {
            board.dropDisc(0, 'Y');
        }

        assertTrue(board.checkWin('Y'), "Y should win with a vertical connection");
    }

    @Test
    void testCheckWinDiagonal() {
        // Simulate a diagonal win: (0,0), (1,1), (2,2), (3,3)
        board.dropDisc(0, 'Y'); // Row 5
        board.dropDisc(1, ' '); // Row 5 (not placing a disc)
        board.dropDisc(1, 'Y'); // Row 4
        board.dropDisc(2, ' '); // Row 5 (not placing a disc)
        board.dropDisc(2, 'Y'); // Row 3
        board.dropDisc(3, 'Y'); // Row 2
        board.dropDisc(3, ' '); // Row 5 (not placing a disc)

        assertTrue(board.checkWin('Y'), "Y should win with a diagonal connection");
    }

    @Test
    void testIsFull() {
        // Fill the board completely
        for (int col = 0; col < board.getColumns(); col++) {
            for (int row = 0; row < board.getRows(); row++) {
                board.dropDisc(col, 'Y'); // Fill all columns
            }
        }
        assertTrue(board.isFull(), "The board should be full after all columns are filled");
    }
}
