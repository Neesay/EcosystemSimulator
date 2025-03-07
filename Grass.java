import java.util.*;
import javafx.scene.paint.Color;
import static java.lang.Math.min;

/**
 * A simple model of grass.
 * Grass ages, dies, and reproduces (spreads) with controlled probability.
 */
public class Grass extends Animal {
    private static final int MAX_AGEING = 5;
    private static final Color GRASS_COLOR = Color.DARKSEAGREEN;
    private static final Random rand = Randomizer.getRandom();
    private int age;
    private int actCounter = 0;

    /**
     * Constructor for Grass.
     * Initializes grass with age 0, or a random age if randomAge is true.
     */
    public Grass(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        age = 0;
        if (randomAge) {
            // If randomAge is enabled, set the age to a random value up to MAX_AGEING
            age = rand.nextInt(MAX_AGEING + 1);
        }
    }

    /**
     * Returns the current age of the grass.
     */
    public int getAge() {
        return age;
    }

    /**
     * The act method is called every simulation step.
     * This method increments an internal counter and only acts every 5 steps.
     * When it acts, it increments the age and attempts to spread (reproduce).
     */
    @Override
    public void act(List<Animal> newGrass) {
        actCounter++;
        // Only act every 5 steps
        if (actCounter % 5 != 0) {
            return;
        }
        // Ensure that the field and location are valid before proceeding
        if (getField() == null || getLocation() == null) {
            return;
        }
        incrementAge();
        spread(newGrass);
    }

    /**
     * Increments the age of the grass.
     * If the age exceeds MAX_AGEING, the grass is marked as dead.
     */
    private void incrementAge() {
        age++;
        if (age > MAX_AGEING) {
            setDead();
        }
    }

    /**
     * Returns the food value of the grass.
     * The food value increases with age up to a maximum of MAX_AGEING.
     */
    @Override
    public int getFoodValue() {
        return min(age, MAX_AGEING);
    }

    /**
     * Spreading mechanism: with a 10% chance, produce new grass
     * in adjacent cells that are completely empty.
     */
    private void spread(List<Animal> newGrass) {
        // Guard against null field or location
        if (getField() == null || getLocation() == null) {
            return;
        }
        double reproductionProbability = 0.1; // 10% chance to reproduce
        if (rand.nextDouble() < reproductionProbability) {
            // Get a list of adjacent free locations
            List<Location> free = getField().getFreeAdjacentLocations(getLocation());
            for (Location loc : free) {
                // Only place offspring if the cell is completely empty
                if (getField().getObjectAt(loc) == null) {
                    Grass offspring = new Grass(false, getField(), loc, getColor());
                    newGrass.add(offspring);
                    getField().place(offspring, loc);
                }
            }
        }
    }
}
