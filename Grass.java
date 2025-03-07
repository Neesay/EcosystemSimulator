import java.util.*;
import javafx.scene.paint.Color;
import static java.lang.Math.min;

/**
 * A simple model of grass.
 * Grass ages, dies, and reproduces (spreads) with controlled probability.
 *
 * @author Yaseen Alam and Ulvis Turkers
 * @version 04/03/2025
 */
public class Grass extends Animal {
    // Maximum age before the grass dies
    private static final int MAX_AGEING = 5;
    // The standard colour of grass
    private static final Color GRASS_COLOR = Color.DARKSEAGREEN;
    // Random instance for generating random values
    private static final Random rand = Randomizer.getRandom();
    // Current age of the grass
    private int age;
    // Counter to track the number of acts performed
    private int actCounter = 0;

    /**
     * Constructs a new Grass object.
     *
     * @param randomAge if true, the grass will have a random starting age up to MAX_AGEING.
     * @param field     The field in which the grass is located.
     * @param location  The location of the grass in the field.
     * @param col       The colour of the grass.
     */
    public Grass(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        age = 0;
        // Initialise age randomly if required.
        if (randomAge) {
            // If randomAge is enabled, set the age to a random value up to MAX_AGEING
            age = rand.nextInt(MAX_AGEING + 1);
        }
    }

    /**
     * Retrieves the current age of the grass.
     *
     * @return The age of the grass as an integer.
     */
    public int getAge() {
        return age;
    }

    /**
     * Defines the behaviour of the grass for a single simulation step.
     * The grass only acts every 5 steps (controlled by actCounter).
     * When acting, it increments its age and attempts to spread.
     *
     * @param newGrass A list to collect newly grown grass instances.
     */
    @Override
    public void act(List<Animal> newGrass) {
        actCounter++;
        // Only act every 5 steps
        if (actCounter % 5 != 0) {
            return;
        }
        // Ensure that the field and location are valid
        if (getField() == null || getLocation() == null) {
            return;
        }
        // Increment the age of the grass
        incrementAge();
        // Attempt to spread (reproduce) new grass
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
     * Retrieves the food value provided by the grass.
     * The food value is the minimum of the current age and MAX_AGEING.
     *
     * @return The food value as an integer.
     */
    @Override
    public int getFoodValue() {
        return min(age, MAX_AGEING);
    }

    /**
     * Spreads the grass to adjacent free cells with a controlled probability.
     * With a 10% chance, new grass is produced in adjacent cells that are completely empty.
     *
     * @param newGrass A list to collect newly grown grass instances.
     */
    private void spread(List<Animal> newGrass) {
        // Guard against invalid field or location
        if (getField() == null || getLocation() == null) {
            return;
        }
        double reproductionProbability = 0.1; // 10% chance to reproduce
        if (rand.nextDouble() < reproductionProbability) {
            // Get all free adjacent locations
            List<Location> free = getField().getFreeAdjacentLocations(getLocation());
            for (Location loc : free) {
                // Only spread if the adjacent location is truly empty
                if (getField().getObjectAt(loc) == null) {
                    Grass offspring = new Grass(false, getField(), loc, getColor());
                    newGrass.add(offspring);
                    getField().place(offspring, loc);
                }
            }
        }
    }
}
