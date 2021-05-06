package logic;

import board.Board;
import board.BoardFactory;
import player.ComputerPlayer;
import player.Player;
import utilities.Display;
import utilities.Input;

public class Game {
    Player player = new Player();
    Player opponent = new Player();
    ComputerPlayer ai;
    Input input = new Input();
    BoardFactory bf = new BoardFactory();
    boolean isTest = false;

    public void mainGame() {
        Board board1 = new Board();
        Board board2 = new Board();
        this.player = new Player(input.getString("Player 1, what is your name? "), board1, 1, "🟥");
        this.opponent = new Player(input.getString("Player 2, what is your name? "), board2, 2, "🟦");
        this.ai = new ComputerPlayer("AI", board2);

        String placementType = input.getString("Please choose: (m)anual or (r)andom ship placement: ");
        switch (placementType) {
            case "m":
                manualGameplay(board1, board2);
                break;
            case "t":
                testGameplay(board1, board2);
                isTest = true;
                break;
            default:
                randomGameplay(board1, board2);
                break;
        }

        board1.setBoardVisibility(true);
        board2.setBoardVisibility(true);
        Display.printTwoBoards(board1, board2, isTest);

        while(player.isAlive()&&opponent.isAlive()) {
            shootingPhase(this.player, this.opponent, board1, board2);
        }
        victory(player, opponent);
    }

    private void swapPlayer(Player player, Player opponent) {
        if (this.player == player) {
            this.player = opponent;
            this.opponent = player;
        } else {
            this.player = player;
            this.opponent = opponent;
        }
    }

    public void PlayerVsEasyAI() {
        while ((player.isAlive() && ai.isAlive())) {
            int[] shot = input.convertPlacement(input.getString(String.format("Time for shooting, %s! GET'EM!!!!", player.name)));
            while (!player.validShot(shot, ai)) {
                shot = input.convertPlacement(input.getString(String.format("Time for shooting, %s! GET'EM!!!!", player.name)));
            }
            player.handleShot(shot, ai);
            int[] aiShot = ai.ComputerPlayerEasy();
            ai.handleAIShot(aiShot, player);
            Display.printTwoBoards(player.board, ai.board, isTest);
        }
    }

    public void PlayerVsNormalAI() {
        while ((player.isAlive() && ai.isAlive())) {
            int[] shot = input.convertPlacement(input.getString(String.format("Time for shooting, %s! GET'EM!!!!", player.name)));
            while (!player.validShot(shot, ai)) {
                shot = input.convertPlacement(input.getString(String.format("Time for shooting, %s! GET'EM!!!!", player.name)));
            }
            player.handleShot(shot, ai);
            ai.determineDirection();
            int[] aiShot = ai.shootByDirection(player.board);
            ai.handleAIShot(aiShot, player);
            Display.printTwoBoards(player.board, ai.board, isTest);
        }
    }
      
    private void victory(Player player, Player opponent) {
        String victoryShout;
        if (player.isAlive()) {
            victoryShout = player.name + " has won! GLORY!";
        } else {
            victoryShout = opponent.name + " has won!";
        }
        Display.shout(victoryShout);
    }

    private void randomGameplay(Board board1, Board board2) {
        Display.printSingleBoard(board1);
        for (int j = 0; j < 2; j++) {
            for (int i = 5; i > 1; i--) {
                if (j == 0) {
                    bf.randomPlacement(player, i);
                    Display.printSingleBoard(board1);
                } else {
                    bf.randomPlacement(opponent, i);
                    Display.printSingleBoard(board2);
                }
            }
        }
    }

    private void manualGameplay(Board board1, Board board2) {
        for (int j = 0; j < 2; j++) {
            for (int i = 5; i > 1; i--) {
                if (j == 0) {
                    Display.clear();
                    Display.printSingleBoard(board1);
                    bf.manualPlacement(player, i);
                    Display.printSingleBoard(board1);
                    if (i == 2) {
                        Display.clear();
                    }
                } else {
                    Display.clear();
                    Display.printSingleBoard(board2);
                    bf.manualPlacement(opponent, i);
                    Display.printSingleBoard(board2);
                    if (i == 2) {
                        Display.clear();
                    }
                }
            }
        }
    }

    private void testGameplay(Board board1, Board board2) {

        Display.printSingleBoard(board1);
        bf.randomPlacement(player, 2);
        bf.randomPlacement(opponent, 2);
        bf.randomPlacement(player, 2);
        bf.randomPlacement(opponent, 2);
        Display.printSingleBoard(board1);
        Display.printSingleBoard(board2);
    }

    private void shootingPhase(Player player, Player opponent, Board board1, Board board2) {
            int[] shot = input.convertPlacement(input.getString(String.format("Time for shooting, %s! GET'EM!!!!", player.name)));
            while (!player.validShot(shot, opponent)) {
                shot = input.convertPlacement(input.getString(String.format("Time for shooting, %s! GET'EM!!!!", player.name)));
              }
            player.handleShot(shot, opponent);
            Display.printTwoBoards(board1, board2, isTest);
            swapPlayer(this.player, this.opponent);
    }
}
