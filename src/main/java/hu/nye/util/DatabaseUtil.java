package hu.nye.util;

import hu.nye.model.Board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/connect4";
    private static final String USER = "root";
    private static final String PASS = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found", e);
        }
    }

    public static Board loadBoardFromDatabase(int gameId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT state FROM game_state WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, gameId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String state = rs.getString("state");
                return stringToBoard(state);
            }
        } catch (SQLException e) {
            System.out.println("Error loading game state: " + e.getMessage());
        }
        return null;
    }

    public static void saveBoardToDatabase(Board board) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String state = boardToString(board);
            String query = "INSERT INTO game_state (state) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, state);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving game state: " + e.getMessage());
        }
    }

    private static String boardToString(Board board) {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getColumns(); col++) {
                sb.append(board.getGrid()[row][col]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static Board stringToBoard(String state) {
        String[] rows = state.split("\n");
        int rowCount = rows.length;
        int colCount = rows[0].length();
        Board board = new Board(rowCount, colCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                char c = rows[row].charAt(col);
                if (c == 'Y' || c == 'R') {
                    board.dropDisc(col, c);
                }
            }
        }
        return board;
    }
}
