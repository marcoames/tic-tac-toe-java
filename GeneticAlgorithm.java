import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    private static final Random random = new Random(42);

    public static double evaluate(Chromosome chromosome, Training training) {
        String[] difficulties = {"EASY", "MEDIUM", "HARD"};
        int[] weights = {1, 2, 3};
        double totalScore = 0;

        for (int i = 0; i < difficulties.length; i++) {
            double difficultyScore = 0;
            for (int j = 0; j < 5; j++) {
                difficultyScore += training.playGame(chromosome.getGenes(), difficulties[i]);
            }
            totalScore += (weights[i] * difficultyScore) / 5;
        }
        return totalScore;
    }

    public static Chromosome mutate(Chromosome chromosome, double mutationRate) {
        double[] mutatedGenes = chromosome.getGenes().clone();
        for (int i = 0; i < mutatedGenes.length; i++) {
            mutatedGenes[i] += mutationRate * random.nextGaussian();
            mutatedGenes[i] = Math.max(-1, Math.min(1, mutatedGenes[i])); // Clipping
        }
        return new Chromosome(mutatedGenes, 0.0);  // Score 0 inicial para novo cromossomo
    }

    public static Chromosome crossover(Chromosome chromosome1, Chromosome chromosome2) {
        double[] genes1 = chromosome1.getGenes();
        double[] genes2 = chromosome2.getGenes();
        int crossoverPoint = random.nextInt(genes1.length - 1) + 1;
        double[] newGenes = new double[genes1.length];

        System.arraycopy(genes1, 0, newGenes, 0, crossoverPoint);
        System.arraycopy(genes2, crossoverPoint, newGenes, crossoverPoint, genes2.length - crossoverPoint);

        return new Chromosome(newGenes, 0.0);  // Score 0 inicial para novo cromossomo
    }

    public static Chromosome tournamentSelection(List<Chromosome> population, int tournamentSize) {
        List<Chromosome> tournament = new ArrayList<>();
        Collections.shuffle(population, random);
        
        for (int i = 0; i < tournamentSize; i++) {
            tournament.add(population.get(i));
        }

        return Collections.max(tournament, Comparator.comparingDouble(Chromosome::getScore));
    }

    public static void geneticAlgorithm(int populationSize, int generations, double mutationRate, int tournamentSize) throws Exception {
        List<Chromosome> population = new ArrayList<>();
        Training training = new Training();

        // Inicializar população
        for (int i = 0; i < populationSize; i++) {
            double[] genes = random.doubles(180, -1, 1).toArray();
            population.add(new Chromosome(genes, 0.0));
        }

        List<Double> bestScores = new ArrayList<>();
        Chromosome globalBestChromosome = null;
        double globalBestScore = -Double.MAX_VALUE;

        for (int generation = 1; generation <= generations; generation++) {
            System.out.printf("\nGeneration %d/%d%n", generation, generations);

            // Avaliação
            for (Chromosome chromosome : population) {
                double score = evaluate(chromosome, training);
                chromosome.setScore(score);
            }

            // Ordenar população e encontrar o melhor
            population.sort(Comparator.comparingDouble(Chromosome::getScore).reversed());

            for (Chromosome c : population) {
                System.out.println(c.getScore());
            }

            Chromosome bestChromosome = population.get(0);
            double bestScore = bestChromosome.getScore();
            bestScores.add(bestScore);

            System.out.printf("Score: %.2f%n", bestScore);

            if (bestScore > globalBestScore) {
                globalBestScore = bestScore;
                globalBestChromosome = bestChromosome;
            }

            // Nova população
            List<Chromosome> newPopulation = new ArrayList<>();
            newPopulation.add(bestChromosome);  // Elitismo

            while (newPopulation.size() < populationSize) {
                Chromosome parent1 = tournamentSelection(population, tournamentSize);
                Chromosome parent2 = tournamentSelection(population, tournamentSize);
                Chromosome child = crossover(parent1, parent2);
                child = mutate(child, mutationRate);
                double childScore = evaluate(child, training);
                child.setScore(childScore);
                newPopulation.add(child);
            }

            population = newPopulation;
        }

        // Salvamento e exibição dos melhores resultados
        System.out.printf("\nGlobal Best Score: %.2f%n", globalBestScore);
        saveBestChromosomeToCsv("best_chromosome.csv", globalBestChromosome.getGenes());

        System.out.printf("Best neural network %d saved to 'best_chromosome.pkl'.%n", globalBestChromosome.hashCode());

        NeuralNetwork nn = new NeuralNetwork(globalBestChromosome.getGenes());
        nn.printWeights();
    }

    public static void saveBestChromosomeToCsv(String fileName, double[] chromosome) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (double gene : chromosome) {
                writer.write(Double.toString(gene));
                writer.write(",");  // Separate values with commas
            }
            writer.newLine();
            System.out.printf("Best neural network saved to '%s'.%n", fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        random.setSeed(42);

        geneticAlgorithm(100, 100, 0.1, 3);
    }
}
