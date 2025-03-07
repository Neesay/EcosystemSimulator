/**
 * Provides a counter for a participant in the simulation.
 * This version also tracks cumulative gene values and diseased counts
 * so that average gene values and disease frequency can be computed.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (extended)
 */
public class Counter {

    private String name;
    private int count;

    // New fields for accumulating gene values and disease counts.
    private int totalBreedingAge;
    private int totalLifeSpan;
    private double totalBreedingProbability;
    private int totalLitterSize;
    private double totalDiseaseProbability;
    private double totalMetabolism;
    private int totalFoodValue;
    private int diseasedCount;

    /**
     * Provide a name for one of the simulation types.
     * @param name A class of animal
     */
    public Counter(String name) {
        this.name = name;
        reset();
    }

    /**
     * @return The short description of this type.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The current count for this type.
     */
    public int getCount() {
        return count;
    }

    /**
     * Increment the counter and add this animal's gene data.
     * @param animal The animal to record.
     */
    public void increment(Animal animal) {
        if (animal.getGeneString() != null) {
            count++;
            totalBreedingAge += animal.getBreedingAgeFromGene();
            totalLifeSpan += animal.getLifeSpanFromGene();
            totalBreedingProbability += animal.getBreedingProbabilityFromGene();
            totalLitterSize += animal.getLitterSizeFromGene();
            totalDiseaseProbability += animal.getDiseaseProbabilityFromGene();
            totalMetabolism += animal.getMetabolismFromGene();
            totalFoodValue += animal.getFoodValue();
            if (animal.isDiseased()) {
                diseasedCount++;
            }
        }
    }

    /**
     * Reset the counter and all cumulative values.
     */
    public void reset() {
        count = 0;
        totalBreedingAge = 0;
        totalLifeSpan = 0;
        totalBreedingProbability = 0.0;
        totalLitterSize = 0;
        totalDiseaseProbability = 0.0;
        totalMetabolism = 0.0;
        totalFoodValue = 0;
        diseasedCount = 0;
    }

    // Getter methods for average values:

    public double getAverageBreedingAge() {
        return count == 0 ? 0.0 : (double) totalBreedingAge / count;
    }

    public double getAverageLifeSpan() {
        return count == 0 ? 0.0 : (double) totalLifeSpan / count;
    }

    public double getAverageBreedingProbability() {
        return count == 0 ? 0.0 : totalBreedingProbability / count;
    }

    public double getAverageLitterSize() {
        return count == 0 ? 0.0 : (double) totalLitterSize / count;
    }

    public double getAverageDiseaseProbability() {
        return count == 0 ? 0.0 : totalDiseaseProbability / count;
    }

    public double getAverageMetabolism() {
        return count == 0 ? 0.0 : totalMetabolism / count;
    }

    public double getAverageFoodValue() {
        return count == 0 ? 0.0 : (double) totalFoodValue / count;
    }

    public double getDiseaseFrequency() {
        return count == 0 ? 0.0 : (double) diseasedCount / count;
    }
}
