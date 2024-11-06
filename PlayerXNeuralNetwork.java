import java.io.*;

public class PlayerXNeuralNetwork {
    private GameLogic game;

    public PlayerXNeuralNetwork() {
        this.game = new GameLogic();
    }

    public void playGame() {
        double[] chromosome = loadChromosomeFromCsv("best_chromosome.csv");
        
        // define uma rede neural para o jogo com o cromossomo importado
        NeuralNetwork neuralNetwork = new NeuralNetwork(chromosome);
        game.setNeuralNetwork(chromosome);
        neuralNetwork.printWeights();

        
        boolean gameOver = false;

        while (!gameOver) {

            // usuario joga
            System.out.println("Current Board:");
            game.printBoard();
            game.userMove();
            game.printBoard();

            // verifica se jogo acabou
            if (checkGameOver()) {
                gameOver = true;
                break;
            }
 
            // rede joga
            int move = game.neuralNetworkMove();

            if (move == -1) {
                game.printBoard();
                return; // movimento invalido
            }

            // verifica se jogo acabou
            if (checkGameOver()) {
                gameOver = true;
                System.out.println("Current Board:");
                game.printBoard();
                break;
            }
        }
    }

    private boolean checkGameOver() {
        String realResult = game.checkWinner();

        if (!realResult.equals("Temjogo")) {
            game.printBoard();
            System.out.println("Game result: " + realResult);
            return true;  
        }
        return false; 
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

    public static void main(String[] args) {
        PlayerXNeuralNetwork game = new PlayerXNeuralNetwork();
        game.playGame();
    }
}
