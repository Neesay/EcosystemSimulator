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

    public Grass(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        age = 0;
        if (randomAge) {
            age = rand.nextInt(MAX_AGEING + 1);
        }
    }

    public int getAge() {
        return age;
    }

    @Override
    public void act(List<Animal> newGrass) {
        actCounter++;
        // Only act every 25 steps to slow down reproduction.
        if (actCounter % 25 != 0) {
            return;
        }
        // Check that field and location are not null
        if (getField() == null || getLocation() == null) {
            return;
        }
        incrementAge();
        spread(newGrass);
    }

    private void incrementAge() {
        age++;
        if (age > MAX_AGEING) {
            setDead();
        }
    }

    @Override
    public int getFoodValue() {
        return min(age, MAX_AGEING);
    }

    /**
     * Spreading mechanism: with a low probability, produce new grass
     * only in adjacent cells that are completely empty.
     */
    private void spread(List<Animal> newGrass) {
        // Guard against null field or location
        if (getField() == null || getLocation() == null) {
            return;
        }
        double reproductionProbability = 0.1; // 1% chance to reproduce
        if (rand.nextDouble() < reproductionProbability) {
            List<Location> free = getField().getFreeAdjacentLocations(getLocation());
            for (Location loc : free) {
                if (getField().getObjectAt(loc) == null) {
                    Grass offspring = new Grass(false, getField(), loc, getColor());
                    newGrass.add(offspring);
                    getField().place(offspring, loc);
                }
            }
        }
    }
}
