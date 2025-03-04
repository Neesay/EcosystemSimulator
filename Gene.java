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

    // Gene properties are stored as proper numeric types.
    public int BREEDING_AGE;
    public int MAX_AGE;
    public double BREEDING_PROBABILITY;
    public double DISEASE_PROBABILITY;
    public int MAX_LITTER_SIZE;
    public int MAX_FOOD_VALUE;
    public double METABOLISM;

    private String geneString; // The formatted gene string

    public Gene() {
        // Default values can be set here if needed.
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
        this.BREEDING_AGE = breedingAge;
        this.MAX_AGE = maxAge;
        this.BREEDING_PROBABILITY = breedingProbability;
        this.MAX_LITTER_SIZE = maxLitterSize;
        this.DISEASE_PROBABILITY = diseaseProbability;
        this.MAX_FOOD_VALUE = maxFoodValue;
        this.METABOLISM = metabolism;
        createGeneString();
    }

    public Gene(Animal parent1, Animal parent2) {
        // Combine genes from the two parents.
        this.BREEDING_AGE = mutate(parent1.getBreedingAgeFromGene());
        this.MAX_AGE = mutate(parent1.getLifeSpanFromGene());
        this.BREEDING_PROBABILITY = mutate(parent1.getBreedingProbabilityFromGene(), 0.01);
        this.MAX_LITTER_SIZE = mutate(parent2.getLitterSizeFromGene());
        this.DISEASE_PROBABILITY = mutate(parent2.getDiseaseProbabilityFromGene(), 0.01);
        this.METABOLISM = mutate(parent2.getMetabolismFromGene(), 0.01);
        
        createGeneString();
    }
    
    // Helper method for integer genes.
    private int mutate(int value) {
        if (rand.nextDouble() < 0.20) {
            // 20% chance to change: add or subtract 1.
            return value + (rand.nextDouble() < 0.50 ? 1 : -1);
        }
        return value;
    }
    
    // Helper method for double genes, where delta is the mutation step (e.g. 0.01).
    private double mutate(double value, double delta) {
        if (rand.nextDouble() < 0.20) {
            return value + (rand.nextDouble() < 0.50 ? delta : -delta);
        }
        return value;
    }
    

    /**
     * Creates a 14-digit gene string using the current values.
     */
    public void createGeneString() {
        // 1) Breeding Age: 2 digits
        String breedingAgeStr = String.format("%02d", BREEDING_AGE);

        // 2) Life Span (MAX_AGE): 3 digits
        String lifeSpanStr = String.format("%03d", MAX_AGE);

        // 3) Breeding Probability: 2 digits (multiplied by 100)
        String breedingProbStr = String.format("%02d", (int) (BREEDING_PROBABILITY * 100));

        // 4) Litter Size: 2 digits
        String litterSizeStr = String.format("%02d", MAX_LITTER_SIZE);

        // 5) Disease Probability: 2 digits (multiplied by 100)
        String diseaseProbStr = String.format("%02d", (int) (DISEASE_PROBABILITY * 100));

        // 6) Metabolism: 3 digits (multiplied by 100)
        String metabolismStr = String.format("%03d", (int) (METABOLISM * 100));

        // Concatenate into a single 14-digit string.
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
}
