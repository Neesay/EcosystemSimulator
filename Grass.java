import java.util.*;
import javafx.scene.paint.Color; 
import static java.lang.Math.min;

/**
 * A simple model of grass.
 * Grass ages, dies, and now also reproduces (spreads).
 */
public class Grass extends Animal {
    private static final int MAX_AGEING = 5;
    private static final Random rand = Randomizer.getRandom();
    private int age;

    private int actCounter = 0;

    public Grass(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGEING + 1);
        }
    }
    
    public int getAge() {
        return age;
    }

    public void act(List<Animal> newGrass) {
        actCounter++;
        if(actCounter % 5 != 0) {
            return;
        }
        if(getField() == null) {
            return;
        }
        incrementAge();
        spread(newGrass);
    }

    private void incrementAge() {
        age++;
    }
    
    @Override
    public int getFoodValue() {
        return min(age, MAX_AGEING);
    }
    
    /**
     * Spreading mechanism: With a set probability, produce new grass
     * in all free adjacent locations.
     */
    private void spread(List<Animal> newGrass) {
        double reproductionProbability = 0.01;
        if(rand.nextDouble() < reproductionProbability) {
            List<Location> free = getField().getFreeAdjacentLocations(getLocation());
            for(Location loc : free) {
                Grass offspring = new Grass(false, getField(), loc, getColor());
                newGrass.add(offspring);
                getField().place(offspring, loc);
            }
        }
    }
}
