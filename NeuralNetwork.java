import java.util.Arrays;
import java.util.Random;

public class NeuralNetwork {
    private double[][] weights1;
    private double[] bias1;
    private double[][] weights2;
    private double[] bias2;

    public NeuralNetwork(double[] weights) {
        if (weights == null) {
            initializeRandomWeights();
        } else {
            setWeightsFromChromosome(weights);
        }
    }

    private void initializeRandomWeights() {
        Random random = new Random();
        weights1 = new double[9][9];
        bias1 = new double[9];
        weights2 = new double[9][9];
        bias2 = new double[9];
    
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
        if (weights.length != 180) {
            throw new IllegalArgumentException("Expected 180 weights, but got " + weights.length);
        }
        
        int index = 0;
        weights1 = new double[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                weights1[i][j] = weights[index++];
            }
        }
        
        bias1 = Arrays.copyOfRange(weights, index, index + 9);
        index += 9;
        
        weights2 = new double[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                weights2[i][j] = weights[index++];
            }
        }
        
        bias2 = Arrays.copyOfRange(weights, index, index + 9);
    }

    public double[] getChromosome() {
        double[] chromosome = new double[180];
        int index = 0;
        
        for (double[] row : weights1) {
            for (double weight : row) {
                chromosome[index++] = weight;
            }
        }
        System.arraycopy(bias1, 0, chromosome, index, 9);
        index += 9;
        
        for (double[] row : weights2) {
            for (double weight : row) {
                chromosome[index++] = weight;
            }
        }
        System.arraycopy(bias2, 0, chromosome, index, 9);
        
        return chromosome;
    }

    public double[] forward(int[] boardState) {
        double[] hiddenLayerOutput = calculateHiddenLayerOutput(boardState);
        return calculateOutputLayerOutput(hiddenLayerOutput);
    }

    private double[] calculateHiddenLayerOutput(int[] boardState) {
        double[] hiddenLayerInput = new double[9];
        for (int i = 0; i < 9; i++) {
            hiddenLayerInput[i] = bias1[i];
            for (int j = 0; j < 9; j++) {
                hiddenLayerInput[i] += boardState[j] * weights1[j][i];
            }
        }

        double[] hiddenLayerOutput = new double[9];
        for (int i = 0; i < 9; i++) {
            hiddenLayerOutput[i] = Math.max(0, hiddenLayerInput[i]);
        }
        return hiddenLayerOutput;
    }

    private double[] calculateOutputLayerOutput(double[] hiddenLayerOutput) {
        double[] outputLayerInput = new double[9];
        for (int i = 0; i < 9; i++) {
            outputLayerInput[i] = bias2[i];
            for (int j = 0; j < 9; j++) {
                outputLayerInput[i] += hiddenLayerOutput[j] * weights2[j][i];
            }
        }
        return outputLayerInput;
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
        double[] allWeights = getChromosome();
        String format = "%.4f";
        int index = 0;
        
        while (index < allWeights.length) {
            System.out.print("[ ");
            for (int i = 0; i < 9 && index < allWeights.length; i++, index++) {
                System.out.printf(format + " ", allWeights[index]);
            }
            System.out.println("]");
        }
    }
}
