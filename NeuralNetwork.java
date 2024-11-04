import java.util.Arrays;
import java.util.Random;

/*
    Entrada (9)                  Camada Oculta (9 )                       Saída (9)
    
    boardState[0] ----\          weights1[0][0] ----> (ReLU)              weights2[0][0] ----> saída[0]
    boardState[1] -----\         weights1[1][1] ----> (ReLU)              weights2[1][0] ----> saída[1]
    boardState[2] ------\        weights1[2][2] ----> (ReLU)              weights2[2][0] ----> saída[2]
    boardState[3] -------\       weights1[3][3] ----> (ReLU)              weights2[3][0] ----> saída[3]
    boardState[4] --------\      weights1[4][4] ----> (ReLU)              weights2[4][0] ----> saída[4]
    boardState[5] ---------\     weights1[5][5] ----> (ReLU)              weights2[5][0] ----> saída[5]
    boardState[6] ----------\    weights1[6][6] ----> (ReLU)              weights2[6][0] ----> saída[6]
    boardState[7] -----------\   weights1[7][7] ----> (ReLU)              weights2[7][0] ----> saída[7]
    boardState[8] ------------\  weights1[8][8] ----> (ReLU)              weights2[8][0] ----> saída[8]
    
    Onde:
    
    - boardState: representa o estado do tabuleiro, com cada posição sendo 0, 1 ou 2.
    - weights1[i][j]: são os pesos da camada de entrada para a camada oculta.
    - weights2[i][j]: são os pesos da camada oculta para a camada de saída.
    - bias1 e bias2: são os biases das camadas oculta e de saída.
    - (ReLU): uma função de ativação que aplica ReLU (Rectified Linear Unit) na camada oculta.
    
*/


public class NeuralNetwork {
    private double[][] weights1 = new double[9][9]; // primeira camada de pesos (9x9)
    private double[] bias1 = new double[9];         // bias primeira camada (9 valores)
    private double[][] weights2 = new double[9][9]; // segunda camada de pesos (9x9)
    private double[] bias2 = new double[9];         // bias segunda camada (9 valores)  

    public NeuralNetwork(double[] weights) {
        if (weights == null) {
            initializeRandomWeights();
        } else {
            setWeightsFromChromosome(weights);
        }
    }

    // inicialiiza pesos e bias aleatoriamente entre -1 e 1
    private void initializeRandomWeights() {
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            bias1[i] = random.nextDouble() * 2 - 1;
            bias2[i] = random.nextDouble() * 2 - 1;
            for (int j = 0; j < 9; j++) {
                weights1[i][j] = random.nextDouble() * 2 - 1;
                weights2[i][j] = random.nextDouble() * 2 - 1;
            }
        }
    }

    public void setWeightsFromChromosome(double[] weights) {
        // verifica se cromossomo do tamanho certo
        if (weights.length != 180) throw new IllegalArgumentException("Expected 180 weights, but got " + weights.length);
        
        // index para posicoes do cromossomo
        int index = 0;

        // carrega weights1 (9x9) do cromossomo
        for (int i = 0; i < 9; i++) for (int j = 0; j < 9; j++) weights1[i][j] = weights[index++];
        // carrega bias1 (9 valores) do cromossomo
        bias1 = Arrays.copyOfRange(weights, index, index + 9); index += 9;
        // carrega weights2 (9x9) do cromossomo
        for (int i = 0; i < 9; i++) for (int j = 0; j < 9; j++) weights2[i][j] = weights[index++];
        // carrega bias2 (9 valores) do cromossomo
        bias2 = Arrays.copyOfRange(weights, index, index + 9);
    }

    public double[] getChromosome() {
        double[] chromosome = new double[180];
        int index = 0;
        for (double[] row : weights1) System.arraycopy(row, 0, chromosome, index, 9); index += 81;
        System.arraycopy(bias1, 0, chromosome, index, 9); index += 9;
        for (double[] row : weights2) System.arraycopy(row, 0, chromosome, index, 9); index += 81;
        System.arraycopy(bias2, 0, chromosome, index, 9);
        return chromosome;
    }

    public double[] forward(int[] boardState) {
        double[] hiddenLayerOutput = new double[9];
        for (int i = 0; i < 9; i++) {
            hiddenLayerOutput[i] = bias1[i];
            for (int j = 0; j < 9; j++) hiddenLayerOutput[i] += boardState[j] * weights1[j][i];
            hiddenLayerOutput[i] = Math.max(0, hiddenLayerOutput[i]); // ReLU (funcao de ativacao)
        }

        // array com a saida da rede
        double[] outputLayer = new double[9];
        for (int i = 0; i < 9; i++) {
            outputLayer[i] = bias2[i];
            for (int j = 0; j < 9; j++) outputLayer[i] += hiddenLayerOutput[j] * weights2[j][i];
        }
        return outputLayer;
    }

    public int getMove(int[] board) {
        double[] scores = forward(board);
        int bestMove = -1;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < scores.length; i++) {
            // if (board[i] == 0 && scores[i] > bestScore) { // Only consider empty cells
            if (scores[i] > bestScore) {
                bestScore = scores[i];
                bestMove = i;
            }
        }
        return bestMove;
    }

    public void printWeights() {
        System.out.println("Neural Network Weights:");
        
        // Print weights1 as 9x9 matrix
        System.out.println("Weights1 (Input to Hidden Layer):");
        for (int i = 0; i < 9; i++) {
            System.out.print("[ ");
            for (int j = 0; j < 9; j++) {
                System.out.printf("%.4f ", weights1[i][j]);
            }
            System.out.println("]");
        }
        
        // Print bias1 as a vector
        System.out.println("\nBias1 (Hidden Layer Bias):");
        System.out.print("[ ");
        for (double b : bias1) {
            System.out.printf("%.4f ", b);
        }
        System.out.println("]");
    
        // Print weights2 as 9x9 matrix
        System.out.println("\nWeights2 (Hidden to Output Layer):");
        for (int i = 0; i < 9; i++) {
            System.out.print("[ ");
            for (int j = 0; j < 9; j++) {
                System.out.printf("%.4f ", weights2[i][j]);
            }
            System.out.println("]");
        }
    
        // Print bias2 as a vector
        System.out.println("\nBias2 (Output Layer Bias):");
        System.out.print("[ ");
        for (double b : bias2) {
            System.out.printf("%.4f ", b);
        }
        System.out.println("]");
    }
    

}
