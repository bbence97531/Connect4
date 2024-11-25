package hu.nye.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class HighScoreUtil {
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

    public static void incrementScore(String playerName) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT score FROM high_scores WHERE player_name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, playerName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int currentScore = rs.getInt("score");
                String updateQuery = "UPDATE high_scores SET score = ? WHERE player_name = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, currentScore + 1);
                updateStmt.setString(2, playerName);
                updateStmt.executeUpdate();
            } else {
                String insertQuery = "INSERT INTO high_scores (player_name, score) VALUES (?, 1)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, playerName);
                insertStmt.setInt(2, 1);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error updating high score: " + e.getMessage());
        }
    }

    public static void printHighScores() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT * FROM high_scores ORDER BY score DESC LIMIT 10";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.println(rs.getString("player_name") + ": " + rs.getInt("score"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving high scores: " + e.getMessage());
        }
    }
}
