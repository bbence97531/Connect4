package hu.nye.service;

import hu.nye.model.Board;
import hu.nye.model.Player;
import hu.nye.util.DatabaseUtil;
import hu.nye.util.HighScoreUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameService {
    private final Board board;
    private final Player human;
    private final Player computer;
    private final Scanner scanner;
    private final Random random;
    private boolean exitFlag;

    public GameService(Board board, Player human, Player computer, Scanner scanner) {
        this.board = board;
        this.human = human;
        this.computer = computer;
        this.scanner = scanner;
        this.random = new Random();
        this.exitFlag = false;
    }

    public void startGame() {
        board.printBoard();
        boolean humanTurn = true;

        while (!exitFlag) {
            if (humanTurn) {
                System.out.println(human.getName() + " játéka (" + human.getDisc() + ")");
                String input = scanner.nextLine().trim().toLowerCase();
                if (input.equals("mentés")) {
                    saveAndExit();
                    break;
                } else if (input.equals("kilépés")) {
                    exitWithoutSaving();
                    break;
                }
                int column = getMove(input);
                if (column == -1) {
                    continue;
                }
                boolean success = board.dropDisc(column, human.getDisc());
                if (!success) {
                    System.out.println("Ez az oszlop tele van. Próbálj meg egy másikat.");
                    continue;
                }
                board.printBoard();
                if (board.checkWin(human.getDisc())) {
                    System.out.println(human.getName() + " nyert!");
                    HighScoreUtil.incrementScore(human.getName());
                    break;
                }
            } else {
                System.out.println(computer.getName() + " játéka (" + computer.getDisc() + ")");
                int column = getComputerMove();
                System.out.println(computer.getName() + " az oszlopba dobta a korongot: " + (char) ('A' + column));
                board.dropDisc(column, computer.getDisc());
                board.printBoard();
                if (board.checkWin(computer.getDisc())) {
                    System.out.println(computer.getName() + " nyert!");
                    HighScoreUtil.incrementScore(computer.getName());
                    break;
                }
            }

            if (board.isFull()) {
                System.out.println("Döntetlen! A tábla tele van.");
                break;
            }

            humanTurn = !humanTurn;
        }
    }

    private int getMove(String input) {
        if (input.length() != 1) {
            System.out.println("Kérlek, adj meg egyetlen betűt.");
            return -1;
        }
        char colChar = input.charAt(0);
        int column = colChar - 'a';
        if (column < 0 || column >= board.getColumns()) {
            System.out.println("Érvénytelen oszlop. Próbáld újra.");
            return -1;
        }
        if (board.isColumnFull(column)) {
            System.out.println("Ez az oszlop tele van. Próbálj meg egy másikat.");
            return -1;
        }
        return column;
    }

    private int getComputerMove() {
        List<Integer> availableColumns = new ArrayList<>();
        for (int col = 0; col < board.getColumns(); col++) {
            if (!board.isColumnFull(col)) {
                availableColumns.add(col);
            }
        }
        if (availableColumns.isEmpty()) {
            throw new IllegalStateException("Nincs elérhető oszlop a gép számára.");
        }
        return availableColumns.get(random.nextInt(availableColumns.size()));
    }

    private void saveAndExit() {
        DatabaseUtil.saveBoardToDatabase(board);
        System.out.println("A játékállás mentve lett az adatbázisba. Kilépés.");
        exitFlag = true;
    }

    private void exitWithoutSaving() {
        System.out.println("Kilépés mentés nélkül.");
        exitFlag = true;
    }
}
