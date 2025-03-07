import java.util.Random;

/**
 * Representing genes of animals with methods for different situations.
 *
 * Example usage:
 *   Gene gene = new Gene(5, 100, 0.50, 3, 0.10, 20, 1.25);
 *   System.out.println("Gene String: " + gene.getGeneString());
 *   System.out.println("Breeding Age: " + gene.getBreedingAgeFromGene());
 *
 * @author Yaseen Alam and Ulvis Turkers
 * @version 03/03/2025
 */
public class Gene {
    private static final Random rand = Randomizer.getRandom();

    public int BREEDING_AGE;
    public int MAX_AGE;
    public double BREEDING_PROBABILITY;
    public double DISEASE_PROBABILITY;
    public int MAX_LITTER_SIZE;
    public int MAX_FOOD_VALUE;
    public double METABOLISM;

    private String geneString; // The formatted gene string

    public Gene() {
    }

    /**
     * Constructs a Gene object with the specified values.
     *
     * @param breedingAge the breeding age (expected to be between 0 and 99)
     * @param maxAge the maximum age (expected to be between 0 and 999)
     * @param breedingProbability the breeding probability (0.0 - 1.0)
     * @param maxLitterSize the maximum litter size (expected to be between 0 and 99)
     * @param diseaseProbability the disease probability (0.0 - 1.0)
     * @param maxFoodValue the maximum food value (not used in gene string but stored)
     * @param metabolism the metabolism value
     */
    public Gene(int breedingAge, int maxAge, double breedingProbability,
                int maxLitterSize, double diseaseProbability, int maxFoodValue,
                double metabolism) {
        this.BREEDING_AGE = clampInt(breedingAge, 12, 90);
        this.MAX_AGE = clampInt(maxAge, 10, 120);
        this.BREEDING_PROBABILITY = clampDouble(breedingProbability, 0.0, 0.50);
        this.MAX_LITTER_SIZE = clampInt(maxLitterSize, 1, 12);
        this.DISEASE_PROBABILITY = clampDouble(diseaseProbability, 0.0, 0.50);
        this.MAX_FOOD_VALUE = maxFoodValue;
        this.METABOLISM = clampDouble(metabolism, 0.25, 1.0);
        createGeneString();
    }

    public Gene(Animal parent1, Animal parent2) {
        this.BREEDING_AGE = mutate(parent1.getBreedingAgeFromGene(), 12, 90);
        this.MAX_AGE = mutate(parent1.getLifeSpanFromGene(), 10, 120);
        this.BREEDING_PROBABILITY = mutate(parent1.getBreedingProbabilityFromGene(), 0.0, 0.50, 0.01);
        this.MAX_LITTER_SIZE = mutate(parent2.getLitterSizeFromGene(), 1, 12);
        this.DISEASE_PROBABILITY = mutate(parent2.getDiseaseProbabilityFromGene(), 0.0, 0.50, 0.01);
        this.METABOLISM = mutate(parent2.getMetabolismFromGene(), 0.25, 1.0, 0.01);
        this.MAX_FOOD_VALUE = Math.max(1, parent1.getFoodValue());

        createGeneString();
    }

    private int mutate(int value, int min, int max) {
        int result = value;
        if (rand.nextDouble() < 0.20) {
            result = value + (rand.nextDouble() < 0.50 ? 1 : -1);
        }
        return clampInt(result, min, max);
    }

    private double mutate(double value, double min, double max, double delta) {
        double result = value;
        if (rand.nextDouble() < 0.20) {
            result = value + (rand.nextDouble() < 0.50 ? delta : -delta);
        }
        return clampDouble(result, min, max);
    }

    /**
     * Creates a 14-digit gene string using the current values.
     */
    public void createGeneString() {
        String breedingAgeStr = String.format("%02d", BREEDING_AGE);
        String lifeSpanStr = String.format("%03d", MAX_AGE);
        String breedingProbStr = String.format("%02d", (int) (BREEDING_PROBABILITY * 100));
        String litterSizeStr = String.format("%02d", MAX_LITTER_SIZE);
        String diseaseProbStr = String.format("%02d", (int) (DISEASE_PROBABILITY * 100));
        String metabolismStr = String.format("%03d", (int) (METABOLISM * 100));

        geneString = breedingAgeStr + lifeSpanStr + breedingProbStr
                + litterSizeStr + diseaseProbStr + metabolismStr;
    }

    /**
     * Returns the full gene string.
     *
     * @return the gene string
     */
    public String getGeneString(){
        return geneString;
    }

    /**
     * Extracts the breeding age from the gene string.
     *
     * @return the breeding age as an integer
     */
    public int getBreedingAgeFromGene() {
        return Integer.parseInt(geneString.substring(0, 2));
    }

    /**
     * Extracts the life span (maximum age) from the gene string.
     *
     * @return the life span as an integer
     */
    public int getLifeSpanFromGene() {
        return Integer.parseInt(geneString.substring(2, 5));
    }

    /**
     * Extracts the breeding probability from the gene string.
     *
     * @return the breeding probability as a double
     */
    public double getBreedingProbabilityFromGene() {
        int probValue = Integer.parseInt(geneString.substring(5, 7));
        return probValue / 100.0;
    }

    /**
     * Extracts the litter size from the gene string.
     *
     * @return the litter size as an integer
     */
    public int getLitterSizeFromGene() {
        if(geneString == null) {
            createGeneString();
        }
        return Integer.parseInt(geneString.substring(7, 9));
    }


    /**
     * Extracts the disease probability from the gene string.
     *
     * @return the disease probability as a double
     */
    public double getDiseaseProbabilityFromGene() {
        int diseaseValue = Integer.parseInt(geneString.substring(9, 11));
        return diseaseValue / 100.0;
    }

    /**
     * Extracts the metabolism value from the gene string.
     *
     * @return the metabolism value as a double
     */
    public double getMetabolismFromGene() {
        int metabolismValue = Integer.parseInt(geneString.substring(11, 14));
        return metabolismValue / 100.0;
    }

    public static int clampInt(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    public static double clampDouble(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }
}
