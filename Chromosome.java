public class Chromosome {
    private double[] genes;
    private double score;

    public Chromosome(double[] genes, double score) {
        this.genes = genes;
        this.score = score;
    }

    public double[] getGenes() {
        return genes;
    }

    public void setGenes(double[] genes) {
        this.genes = genes;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
