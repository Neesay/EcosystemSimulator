import java.util.*;
import javafx.scene.paint.Color; 
import static java.lang.Math.min;
/**
 * A simple model of grass.
 * Grass age, and die.
 * 
 * @author Yaseen A.
 * @version 26.02.2025
 */

public class Grass extends Animal{

    private static final int MAX_AGEING = 3;
    private static final int MAX_LITTER_SIZE = 3;
    private static final Color color = Color.DARKSEAGREEN;
    private static final Random rand = Randomizer.getRandom();
    private static int age;
    private static final int FOOD_VALUE = Math.min(age, MAX_AGEING);

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Grass(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        age = 0;

        if(randomAge) {
            age = rand.nextInt(MAX_AGEING + 1);
        }
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newRabbits A list to return newly born rabbits.
     */
    public void act(List<Animal> newGrass) {
        incrementAge();
        //giveBirth(newGrass);  
    }

    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    private void incrementAge() {
        age++;
    }
    
    /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    private void giveBirth(List<Animal> newGrass) {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = rand.nextInt(MAX_LITTER_SIZE);
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Grass young = new Grass(false, field, loc, color);
            newGrass.add(young);
        }
    }

    /**
     * A rabbit can breed if it has reached the breeding age.
     * @return true if the rabbit can breed, false otherwise.
     */
    private boolean canBreed() {
        return age >= MAX_AGEING;
    }
    
    @Override
    public int getFoodValue() {
        return FOOD_VALUE;
    }
}