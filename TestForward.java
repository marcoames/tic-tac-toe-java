import java.util.Arrays;

public class TestForward {
    public static void main(String[] args) {
        // Exemplo de board
        int[] boardState = {
            0, 1, 2,  
            0, 2, 1,
            1, 0, 2
        };

        System.out.println("Board State:");
        System.out.println(Arrays.toString(boardState));

        // cria rede com cromossomo vazio (pesos aleatorios)
        NeuralNetwork neuralNetwork = new NeuralNetwork(null);

        // neuralNetwork.printWeights();

        double[] output = neuralNetwork.forward(boardState);

        System.out.println("\nOutput Layer Values:");
        System.out.println(Arrays.toString(output));
    }
}
