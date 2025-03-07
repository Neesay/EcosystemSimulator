/**
 * Provides a counter for a participant in the simulation.
 * This version also tracks cumulative gene values and diseased counts
 * so that average gene values and disease frequency can be computed.
 *
 * @author Yaseen Alam and Ulvis Turkers
 * @version 04/03/2025
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
     * Provides a name for one of the simulation types.
     *
     * @param name A class of animal.
     */
    public Counter(String name) {
        this.name = name;
        reset();
    }

    /**
     * Retrieves the short description of this type.
     *
     * @return The name of this type.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the current count for this type.
     *
     * @return The count as an integer.
     */
    public int getCount() {
        return count;
    }

    /**
     * Increments the counter and adds the animal's gene data.
     *
     * @param animal The animal whose data is to be recorded.
     */
    public void increment(Animal animal) {
        if (animal.gene != null) {
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
    }

    /**
     * Resets the counter and all cumulative values.
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

    /**
     * Computes the average breeding age from the accumulated data.
     *
     * @return The average breeding age as a double.
     */
    public double getAverageBreedingAge() {
        return count == 0 ? 0.0 : (double) totalBreedingAge / count;
    }

    /**
     * Computes the average life span from the accumulated data.
     *
     * @return The average life span as a double.
     */
    public double getAverageLifeSpan() {
        return count == 0 ? 0.0 : (double) totalLifeSpan / count;
    }

    /**
     * Computes the average breeding probability from the accumulated data.
     *
     * @return The average breeding probability as a double.
     */
    public double getAverageBreedingProbability() {
        return count == 0 ? 0.0 : totalBreedingProbability / count;
    }

    /**
     * Computes the average litter size from the accumulated data.
     *
     * @return The average litter size as a double.
     */
    public double getAverageLitterSize() {
        return count == 0 ? 0.0 : (double) totalLitterSize / count;
    }

    /**
     * Computes the average disease probability from the accumulated data.
     *
     * @return The average disease probability as a double.
     */
    public double getAverageDiseaseProbability() {
        return count == 0 ? 0.0 : totalDiseaseProbability / count;
    }

    /**
     * Computes the average metabolism from the accumulated data.
     *
     * @return The average metabolism as a double.
     */
    public double getAverageMetabolism() {
        return count == 0 ? 0.0 : totalMetabolism / count;
    }

    /**
     * Computes the average food value from the accumulated data.
     *
     * @return The average food value as a double.
     */
    public double getAverageFoodValue() {
        return count == 0 ? 0.0 : (double) totalFoodValue / count;
    }

    /**
     * Computes the frequency of diseased animals.
     *
     * @return The disease frequency as a double.
     */
    public double getDiseaseFrequency() {
        return count == 0 ? 0.0 : (double) diseasedCount / count;
    }
}
