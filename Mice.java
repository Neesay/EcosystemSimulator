import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color; 


/**
 * A simple model of a mice.
 * Mice age, move, breed, and die. They are a type of prey that
 * breed quickly, have a low food value, and live for a short period.
 * 
 * Breeding details for mice:
 * - Breeding Age: 3 steps
 * - Maximum Age: 20 steps
 * - Breeding Probability: 0.15 (15% chance of breeding when conditions are met)
 * - Maximum Litter Size: 9 offspring per breeding event
 * - Food Value: 4 units
 * 
 * This class is used as part of the predator/prey simulation.
 */

public class Mice extends Animal {

    // Mice-specific constants.
    private static final int BREEDING_AGE = 3;
    private static final int MAX_AGE = 5;
    private static final double BREEDING_PROBABILITY = 0.15;
    private static final double DISEASE_PROBABILITY = BREEDING_PROBABILITY - 0.01;
    private static final int MAX_LITTER_SIZE = 9;
    private static final int MAX_FOOD_VALUE = 4;
    
    // Random number generator for breeding and age initialization.
    private static final Random rand = Randomizer.getRandom();
    
    // The current age of the mice.
    private int age;
    private boolean disease = false;
    private int life_left = MAX_AGE/10;
    private double metabolism; 
    private int foodLevel;

    
    /**
     * Create a new mice. A mice may be created as a newborn or with a random age.
     *
     * @param randomAge If true, the mice will have a random age (useful for populating an initial field).
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param col The color to represent this mice.
     */
    public Mice(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        age = 0;
        
        // If randomAge is true, initialize with a random age up to the maximum age.
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
             // Set the food level to a random value up to the rabbit's food value.
            foodLevel = rand.nextInt(MAX_FOOD_VALUE);
            metabolism = rand.nextDouble(0.25, 1);
        } else {
            age = 0;
        
            // Start with the full food level provided by the rabbit's food value.
            foodLevel = MAX_FOOD_VALUE;
        }
    }
    
    /**
     * Define the behavior of the mice for each simulation step.
     * The mice ages, attempts to breed, and moves to a new location if possible.
     *
     * @param newMice A list to collect newly born mice.
     */
    public void act(List<Animal> newMice) {
        // Increase the age and check for death due to old age.
        incrementAge();
        if(isAlive()) {
            // Attempt to give birth to new mice.
            giveBirth(newMice);            
            // Try to move into a free adjacent location.
            Location newLocation = getField().getFreeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // If no free location is available, the mice dies due to overcrowding.
                setDead();
            }
            
            if (!disease){
                double chance = rand.nextDouble();
                if (chance < DISEASE_PROBABILITY) {
                    disease = true;
                }
                
            } else{
                life_left--;
                if (life_left <= 0){
                    setDead();
                }
            }
        }
    }

    /**
     * Increment the age of the mice.
     * If the age exceeds the maximum allowed age, the mice dies.
     */
    private void incrementAge() {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check if the mice is ready to breed and produce offspring.
     * New offspring are placed in adjacent free locations.
     *
     * @param newMice A list to collect newly born mice.
     */
    private void giveBirth(List<Animal> newMice) {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Mice young = new Mice(false, field, loc, getColor());
            newMice.add(young);
        }
    }
        
    /**
     * Determine the number of births based on the mice's breeding conditions.
     *
     * @return The number of new mice born, or zero if the mice cannot breed.
     */
    private int breed() {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * Check whether the mice is of breeding age.
     *
     * @return true if the mice is old enough to breed, false otherwise.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }
    
    /**
     * Get the food value of the mice.
     * This value is used by predators to determine the nutritional benefit.
     *
     * @return the food value of the mice.
     */
    public int getFoodValue() {
        return MAX_FOOD_VALUE;
    }
}
