import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    private static final Random random = new Random();

    public static double evaluate(Chromosome chromosome) {
        String[] difficulties = {"EASY", "MEDIUM", "HARD"};
        int[] weights = {1, 2, 3};
        double totalScore = 0;
        int gamesPerDifficulty = 5;

        for (int i = 0; i < difficulties.length; i++) {
            double difficultyScore = 0;
            for (int j = 0; j < gamesPerDifficulty; j++) { // numero de partidas em cada dificuldade
                Training training = new Training();
                difficultyScore += training.playGame(chromosome.getGenes(), difficulties[i]) / gamesPerDifficulty ;
            }
            totalScore += (weights[i] * difficultyScore);
        }
        return totalScore;
    }
    
    public static Chromosome mutate(Chromosome chromosome, double mutationRate) {
        if (random.nextDouble() > mutationRate) {
            return chromosome; // nao muta se o numero aleatorio for maior que a taxa de mutacao
        }
        double[] mutatedGenes = chromosome.getGenes();
        for (int i = 0; i < mutatedGenes.length; i++) {
            mutatedGenes[i] = (2 * random.nextDouble()) - 1; // random entre -1 e 1
        }

        return new Chromosome(mutatedGenes, 0.0);  // score 0 inicial para novo cromossomo
    }

    // crossover usando media
    public static Chromosome crossover(Chromosome chromosome1, Chromosome chromosome2) {
        double[] genes1 = chromosome1.getGenes();
        double[] genes2 = chromosome2.getGenes();
        double[] newGenes = new double[genes1.length];

        for (int i = 0; i < genes1.length; i++) {
            newGenes[i] = (genes1[i] + genes2[i]) / 2.0; // media dos genes correspondentes
        }

        Chromosome child = new Chromosome(newGenes, 0.0); // score nao avaliado inicial para novo cromossomo
        return child;
    }


    public static Chromosome tournamentSelection(List<Chromosome> population, int tournamentSize) {
        List<Chromosome> tournament = new ArrayList<>();
        Collections.shuffle(population, random);
        
        for (int i = 0; i < tournamentSize; i++) {
            tournament.add(population.get(i));
        }

        Chromosome parent = Collections.max(tournament, Comparator.comparingDouble(Chromosome::getScore));
        return parent;
    }

    public static void geneticAlgorithm(int populationSize, int generations, double mutationRate, int tournamentSize) throws Exception {
        List<Chromosome> population = new ArrayList<>();

        // inicializa a populacao com cromossomos aleatorios (vetor 180 elementos)
        for (int i = 0; i < populationSize; i++) {
            double[] genes = random.doubles(180, -1, 1).toArray();
            population.add(new Chromosome(genes, 0.0));
        }

        List<Double> bestScores = new ArrayList<>();
        Chromosome globalBestChromosome = null;
        double globalBestScore = -Double.MAX_VALUE;

        for (int generation = 1; generation <= generations; generation++) {
            // System.out.printf("\nGeneration %d/%d%n", generation, generations);

            // funcao de avaliacao de cada cromossomo na populacao
            for (Chromosome chromosome : population) {
                double score = evaluate(chromosome);
                chromosome.setScore(score);
            }

            // ordena a populacao de maior score para menor
            population.sort(Comparator.comparingDouble(Chromosome::getScore).reversed());

            // exibir os scores
            // for (Chromosome cromosome : population) {
            //     System.out.println(cromosome.getScore());
            // }



            Chromosome bestChromosome = population.get(0);
            double bestScore = bestChromosome.getScore();
            bestScores.add(bestScore);
            
            if (bestScore >= globalBestScore) {
                globalBestScore = bestScore;
                globalBestChromosome = bestChromosome;
            }
            
            // System.out.printf("Generation %d/%d\t BestScore: %.2f \tGlobalBestScore: %.2f%n", generation, generations, bestScore, globalBestScore);

            double averageScore = population.stream().mapToDouble(Chromosome::getScore).average().orElse(0.0);
            System.out.printf("Generation %d/%d\t  AvgScore: %.2f\t  BestScore: %.2f\t  GlobalBestScore: %.2f%n", 
            generation, generations, averageScore, bestScore, globalBestScore);


            // nova populacao
            List<Chromosome> newPopulation = new ArrayList<>();

            // elitismo passa melhor chromossom para a nova populacao
            newPopulation.add(bestChromosome);          

            while (newPopulation.size() < populationSize) {
                Chromosome parent1 = tournamentSelection(population, tournamentSize);
                Chromosome parent2 = tournamentSelection(population, tournamentSize);
                // System.out.println("Parent1: " + parent1.getScore() + " | Parent2: " + parent2.getScore());
                Chromosome child = crossover(parent1, parent2);
                child = mutate(child, mutationRate);
                newPopulation.add(child);
            }

            // atualiza a populacao
            population = newPopulation;
        }

        // Salvamento e exibição dos melhores resultados
        System.out.printf("\nGlobal Best Score: %.2f%n", globalBestScore);
        saveBestChromosomeToCsv("best_chromosome.csv", globalBestChromosome.getGenes());

        NeuralNetwork nn = new NeuralNetwork(globalBestChromosome.getGenes());
        nn.printWeights();

        saveScoresToFile((ArrayList<Double>) bestScores, "best_scores.txt");

    }

    public static void saveBestChromosomeToCsv(String fileName, double[] chromosome) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (double gene : chromosome) {
                writer.write(Double.toString(gene));
                writer.write(",");
            }
            writer.newLine();
            System.out.printf("Best neural network saved to '%s'.%n", fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void saveScoresToFile(ArrayList<Double> bestScores, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Double score : bestScores) {
                writer.write(score.toString() + "\n");
            }
            System.out.println("Scores saved to " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred while saving scores: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        random.setSeed(42);
        geneticAlgorithm(10, 15000, 0.1, 2);
    }
}
