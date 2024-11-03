import java.io.*;

public class PlayerXNeuralNetwork {
    private GameLogic game;

    public PlayerXNeuralNetwork() {
        this.game = new GameLogic();
    }

    public void playGame() {
        double[] chromosome = loadChromosomeFromCsv("best_chromosome.csv");
        
        // Print neural network weights
        NeuralNetwork neuralNetwork = new NeuralNetwork(chromosome);
        game.setNeuralNetwork(chromosome);  // Ensure this method correctly initializes the network
        System.out.println("Neural Network weights:");
        neuralNetwork.printWeights();

        // Start the game
        boolean gameOver = false;

        while (!gameOver) {
            // USER TURN
            System.out.println("Current Board:");
            game.printBoard();
            game.userMove();
            game.printBoard();

            // Check if the game is over
            if (checkGameOver()) {
                gameOver = true;
                break;
            }

            // AI TURN
            // Neural Network plays
            int move = game.neuralNetworkMove();

            if (move == -1) {
                game.printBoard();
                return; // Invalid
            }

            // Check if the game is over after AI move
            if (checkGameOver()) {
                gameOver = true;
                System.out.println("Current Board:");
                game.printBoard();
                break;
            }
        }
    }

    // Check if the game is over
    private boolean checkGameOver() {
        String realResult = game.checkWinner();

        if (!realResult.equals("Temjogo")) {
            game.printBoard();
            System.out.println("Real game result: " + realResult);
            return true;  // Game is over
        }
        return false;  // Game continues
    }

    public double[] loadChromosomeFromCsv(String fileName) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            if (line != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    
        String[] values = sb.toString().split(",");
        double[] chromosome = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            chromosome[i] = Double.parseDouble(values[i]);
        }
        System.out.println(chromosome);
        return chromosome;
    }

    // Main method
    public static void main(String[] args) {
        PlayerXNeuralNetwork game = new PlayerXNeuralNetwork();
        game.playGame();
    }
}
