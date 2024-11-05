import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

public class GameLogic {
    private int[] board;
    private NeuralNetwork neuralNetwork;
    private boolean randomFlag;

    public GameLogic() {
        // cria o tabuleiro vazio
        this.board = new int[9];
        this.randomFlag = true;
    }

    public void setNeuralNetwork(double[] chromosome) {
        // define uma rede neural para o jogo com o cromossomo 
        this.neuralNetwork = new NeuralNetwork(chromosome);
    }

    public void printBoard() {
        String[] symbols = { " ", "O", "X" };
        String[] displayBoard = Arrays.stream(this.board)
                                      .mapToObj(cell -> symbols[cell])
                                      .toArray(String[]::new);

        System.out.println("\n" +
                displayBoard[0] + " | " + displayBoard[1] + " | " + displayBoard[2] + "\n" +
                "--+---+--\n" +
                displayBoard[3] + " | " + displayBoard[4] + " | " + displayBoard[5] + "\n" +
                "--+---+--\n" +
                displayBoard[6] + " | " + displayBoard[7] + " | " + displayBoard[8] + "\n");
    }

    public int neuralNetworkMove() {
        List<Integer> availableMoves = getAvailableMoves();
        int move = neuralNetwork.getMove(this.board);

        if (!availableMoves.contains(move)) {
            // System.out.println("Rede jogou em " + (move + 1) + " jogada invalida");
            return -1;
        } else {
            this.board[move] = 2;
            return move;
        }
    }

    public int network_move_minimax(){
        int bestMove = network_getBestMoveMinimax();
        return bestMove;
    }

    public int network_getBestMoveMinimax() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;
        for (int i = 0; i < 9; i++) {
            if (this.board[i] == 0) {
                this.board[i] = 2; // marca com "X"
                int score = network_minimax(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                this.board[i] = 0;
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }
        return bestMove;
    }

    private int network_minimax(int depth, boolean isMaximizing, int alpha, int beta) {
        String result = checkWinner();
        if (result.equals("Xganha")) return 1;
        if (result.equals("Oganha")) return -1;
        if (result.equals("Empate")) return 0;

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (this.board[i] == 0) {
                    this.board[i] = 2; // marca com "X"
                    int eval = computer_minimax(depth + 1, false, alpha, beta);
                    this.board[i] = 0;
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (this.board[i] == 0) {
                    this.board[i] = 1; // marca com "O"
                    int eval = network_minimax(depth + 1, true, alpha, beta);
                    this.board[i] = 0;
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) break;
                }
            }
            return minEval;
        }
    }


    public void userMove() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(Arrays.toString(this.board));
            System.out.print("Escolha sua posição (1-9): ");
            int move = scanner.nextInt() - 1;
            if (move >= 0 && move < 9 && this.board[move] == 0) {
                this.board[move] = 1;
                break;
            } else {
                System.out.println("Posição já ocupada ou inválida. Escolha outra.");
            }
        }
    }

    public void computerMove(String difficulty) {
        if (difficulty.equals("EASY")) { // joga minimax 25% das vezes
            if (Math.random() < 0.75) {
                computerMoveRandom();
            } else {
                computerMoveMinimax();
            }
        } else if (difficulty.equals("MEDIUM")) { // alterna entre random e minimax
            if (this.randomFlag) { 
                computerMoveRandom();
            } else {
                computerMoveMinimax();
            }
            this.randomFlag = !randomFlag;
        } else if (difficulty.equals("HARD")) { // sempre joga minimax
            computerMoveMinimax();
        }
    }

    private void computerMoveRandom() {
        List<Integer> availableMoves = getAvailableMoves();
        int move = availableMoves.get(new Random().nextInt(availableMoves.size()));
        this.board[move] = 1;
    }

    private void computerMoveMinimax() {
        int bestMove = computer_getBestMoveMinimax();
        this.board[bestMove] = 1;
    }

    private int computer_getBestMoveMinimax() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;
        for (int i = 0; i < 9; i++) {
            if (this.board[i] == 0) {
                this.board[i] = 1; // marca com "O"
                int score = computer_minimax(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                this.board[i] = 0;
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }
        return bestMove;
    }

    private int computer_minimax(int depth, boolean isMaximizing, int alpha, int beta) {
        String result = checkWinner();
        if (result.equals("Xganha")) return -1;
        if (result.equals("Oganha")) return 1;
        if (result.equals("Empate")) return 0;

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (this.board[i] == 0) {
                    this.board[i] = 1; // marca com "O"
                    int eval = computer_minimax(depth + 1, false, alpha, beta);
                    this.board[i] = 0;
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (this.board[i] == 0) {
                    this.board[i] = 2; // marca com "X"
                    int eval = computer_minimax(depth + 1, true, alpha, beta);
                    this.board[i] = 0; 
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) break;
                }
            }
            return minEval;
        }
    }

    public String checkWinner() {
        int[][] winningCombinations = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
        };

        for (int[] combo : winningCombinations) {
            if (this.board[combo[0]] == this.board[combo[1]] &&
                this.board[combo[1]] == this.board[combo[2]] &&
                this.board[combo[0]] != 0) {
                return (this.board[combo[0]] == 2) ? "Xganha" : "Oganha";
            }
        }

        for (int cell : this.board) {
            if (cell == 0) return "Temjogo";
        }
        return "Empate";
    }

    private List<Integer> getAvailableMoves() {
        return IntStream.range(0, this.board.length) 
                        .filter(i -> this.board[i] == 0) 
                        .boxed() 
                        .toList();
    }

}
