import java.util.HashMap;
import java.util.Set;

/**
 * This class collects and provides detailed statistical data on the state
 * of a field. In addition to counting the number of animals per species, it
 * also tracks average gene values and disease frequency.
 */
public class FieldStats {

    private HashMap<Class, Counter> counters;
    private boolean countsValid;

    /**
     * Construct a FieldStats object.
     */
    public FieldStats() {
        counters = new HashMap<>();
        countsValid = true;
    }

    /**
     * Get detailed statistics of what is in the field.
     * @return A string describing counts and average gene values per species,
     *         and the frequency of disease outbreaks.
     */
    public String getPopulationDetails(Field field) {
        StringBuilder buffer = new StringBuilder();
        if (!countsValid) {
            generateCounts(field);
        }
        Set<Class> keys = counters.keySet();
        buffer.append("\n");
        for (Class key : keys) {
            Counter info = counters.get(key);
            buffer.append(info.getName());
            buffer.append(": Count = ").append(info.getCount());
            buffer.append("| BA: ").append(String.format("%.2f", info.getAverageBreedingAge()));
            buffer.append("| LS = ").append(String.format("%.2f", info.getAverageLifeSpan()));
            buffer.append("| BP = ").append(String.format("%.2f", info.getAverageBreedingProbability()));
            buffer.append("| LS = ").append(String.format("%.2f", info.getAverageLitterSize()));
            buffer.append("| DP = ").append(String.format("%.2f", info.getAverageDiseaseProbability()));
            buffer.append("| M = ").append(String.format("%.2f", info.getAverageMetabolism()));
            buffer.append("| FV = ").append(String.format("%.2f", info.getAverageFoodValue()));
            buffer.append("| DF = ").append(String.format("%.2f", info.getDiseaseFrequency()));
            buffer.append("\n");
        }
        return buffer.toString();
    }

    /**
     * Invalidate the current set of statistics; reset all counts.
     */
    public void reset() {
        countsValid = false;
        for (Counter counter : counters.values()) {
            counter.reset();
        }
    }

    /**
     * Increment the count for a given species, recording its gene data.
     * @param animalClass The class of animal.
     * @param animal The animal instance to record.
     */
    public void incrementCount(Class animalClass, Animal animal) {
        Counter counter = counters.get(animalClass);
        if (counter == null) {
            counter = new Counter(animalClass.getSimpleName());
            counters.put(animalClass, counter);
        }
        counter.increment(animal);
    }

    /**
     * Indicate that the animal counts have been fully updated.
     */
    public void countFinished() {
        countsValid = true;
    }

    /**
     * Determine whether the simulation is still viable.
     * @return true if at least one species is present.
     */
    public boolean isViable(Field field) {
        int nonZero = 0;
        if (!countsValid) {
            generateCounts(field);
        }
        for (Counter counter : counters.values()) {
            if (counter.getCount() > 0) {
                nonZero++;
            }
        }
        return nonZero >= 1;
    }

    /**
     * Generate counts for all animals in the field.
     * @param field The field to generate statistics for.
     */
    private void generateCounts(Field field) {
        reset();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Animal animal = field.getObjectAt(row, col);
                if (animal != null) {
                    incrementCount(animal.getClass(), animal);
                }
            }
        }
        countsValid = true;
    }
}
