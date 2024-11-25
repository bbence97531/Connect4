package hu.nye.model;

import java.util.Arrays;

public class Board {
    private final int rows;
    private final int columns;
    private final char[][] grid;

    public Board(int rows, int columns) {
        if (columns < 4 || columns > 12 || rows < 4 || rows > 12) {
            throw new IllegalArgumentException("A sorok és oszlopok számának 4 és 12 között kell lennie.");
        }
        this.rows = rows;
        this.columns = columns;
        this.grid = new char[rows][columns];
        for (char[] row : grid) {
            Arrays.fill(row, ' ');
        }
    }

    public boolean isColumnFull(int column) {
        return grid[0][column] != ' ';
    }

    public boolean dropDisc(int column, char disc) {
        if (column < 0 || column >= columns) {
            return false;
        }
        for (int row = rows - 1; row >= 0; row--) {
            if (grid[row][column] == ' ') {
                grid[row][column] = disc;
                return true;
            }
        }
        return false;
    }

    public boolean checkWin(char disc) {
        // Vízszintes ellenőrzés
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col <= columns - 4; col++) {
                if (grid[row][col] == disc &&
                        grid[row][col + 1] == disc &&
                        grid[row][col + 2] == disc &&
                        grid[row][col + 3] == disc) {
                    return true;
                }
            }
        }

        // Függőleges ellenőrzés
        for (int col = 0; col < columns; col++) {
            for (int row = 0; row <= rows - 4; row++) {
                if (grid[row][col] == disc &&
                        grid[row + 1][col] == disc &&
                        grid[row + 2][col] == disc &&
                        grid[row + 3][col] == disc) {
                    return true;
                }
            }
        }

        // Átlós (balról fent) ellenőrzés
        for (int row = 0; row <= rows - 4; row++) {
            for (int col = 0; col <= columns - 4; col++) {
                if (grid[row][col] == disc &&
                        grid[row + 1][col + 1] == disc &&
                        grid[row + 2][col + 2] == disc &&
                        grid[row + 3][col + 3] == disc) {
                    return true;
                }
            }
        }

        // Átlós (jobbról fent) ellenőrzés
        for (int row = 0; row <= rows - 4; row++) {
            for (int col = 3; col < columns; col++) {
                if (grid[row][col] == disc &&
                        grid[row + 1][col - 1] == disc &&
                        grid[row + 2][col - 2] == disc &&
                        grid[row + 3][col - 3] == disc) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isFull() {
        for (int col = 0; col < columns; col++) {
            if (!isColumnFull(col)) {
                return false;
            }
        }
        return true;
    }

    public void printBoard() {
        // Oszlopok számozása betűkkel
        System.out.print("");
        for (int col = 0; col < columns; col++) {
            System.out.print(" " + (char) ('A' + col));
        }
        System.out.println();

        // Táblázat kirajzolása
        for (int row = 0; row < rows; row++) {
            System.out.print("|");
            for (int col = 0; col < columns; col++) {
                System.out.print(grid[row][col] + "|");
            }
            System.out.println();
        }

        // Alul vonalak
        for (int col = 0; col < columns; col++) {
            System.out.print("--");
        }
        System.out.println("-");
    }

    public char[][] getGrid() {
        return grid;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    //Value Object
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        if (rows != board.rows) return false;
        if (columns != board.columns) return false;
        for (int row = 0; row < rows; row++) {
            if (!Arrays.equals(grid[row], board.grid[row])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = rows;
        result = 31 * result + columns;
        for (char[] row : grid) {
            result = 31 * result + Arrays.hashCode(row);
        }
        return result;
    }
}