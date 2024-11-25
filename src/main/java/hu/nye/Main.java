package hu.nye;

import hu.nye.model.Board;
import hu.nye.model.Player;
import hu.nye.service.GameService;
import hu.nye.util.DatabaseUtil;
import hu.nye.util.HighScoreUtil;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Connect 4 Játék");

        // Játékos név bekérése
        System.out.print("Add meg a játékos neved: ");
        String playerName = scanner.nextLine();

        // Tábla méretének beállítása
        int rows = 6;
        int columns = 7;
        // Opció: Beolvasás adatbázisból
        System.out.print("Szeretnél betölteni egy játékállást? (igen/nem): ");
        String loadChoice = scanner.nextLine();
        Board board;
        if (loadChoice.equalsIgnoreCase("igen")) {
            System.out.print("Add meg a játék ID-t: ");
            int gameId = Integer.parseInt(scanner.nextLine());
            board = DatabaseUtil.loadBoardFromDatabase(gameId);
            if (board == null) {
                System.out.println("Nem sikerült betölteni a játékot. Üres táblával kezdünk.");
                board = new Board(rows, columns);
            }
        } else {
            board = new Board(rows, columns);
        }

        // Játékosok inicializálása
        Player human = new Player(playerName, 'Y');
        Player computer = new Player("Gép", 'R');

        // Játék indítása
        GameService gameService = new GameService(board, human, computer, scanner);
        gameService.startGame();

        // Top pontszámok kiírása
        System.out.println("Top pontszámok:");
        HighScoreUtil.printHighScores();

        scanner.close();
    }
}
