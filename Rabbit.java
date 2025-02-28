import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color; 

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes, Michael Kölling and Jeffery Raphael
 * @version 2025.02.10
 */

public class Rabbit extends Animal {

    // Instance variables representing rabbit properties.
    private int BREEDING_AGE;           
    private int MAX_AGE;                
    private double BREEDING_PROBABILITY;  
    private double DISEASE_PROBABILITY;   
    private int MAX_LITTER_SIZE;        
    private int MAX_FOOD_VALUE;         
    private double metabolism;          

    // Other instance variables.
    private int age;
    private boolean disease = false;
    private int life_left;
    private int foodLevel;

    // Random number generator.
    private static final Random rand = Randomizer.getRandom();
    
    /**
     * Create a new rabbit. A rabbit may be created with age zero (a newborn) or with a random age.
     *
     * When randomAge is true, the rabbit's properties are assigned random values within defined ranges:
     *   BREEDING_AGE: [3, 8)  -> 3 to 7 inclusive
     *   MAX_AGE: [33, 48)     -> 33 to 47 inclusive
     *   BREEDING_PROBABILITY: [0.06, 0.12)
     *   DISEASE_PROBABILITY: BREEDING_PROBABILITY - 0.02
     *   MAX_LITTER_SIZE: [3, 6)  -> 3 to 5 inclusive
     *   MAX_FOOD_VALUE: [8, 11)  -> 8 to 10 inclusive
     *   metabolism: [0.25, 1.0)
     * 
     * @param randomAge If true, the rabbit will have a random age and properties.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param col The color representing the rabbit.
     */
    public Rabbit(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        if(randomAge) {
            // Randomly assign properties.
            BREEDING_AGE = rand.nextInt(3, 8);  // Values: 3 to 7.
            MAX_AGE = rand.nextInt(33, 48);       // Values: 33 to 47.
            BREEDING_PROBABILITY = rand.nextDouble(0.06, 0.12);  // Between 0.06 and 0.12.
            DISEASE_PROBABILITY = BREEDING_PROBABILITY - 0.02;
            MAX_LITTER_SIZE = rand.nextInt(3, 6); // Values: 3, 4, or 5.
            MAX_FOOD_VALUE = rand.nextInt(8, 11); // Values: 8, 9, or 10.
            metabolism = rand.nextDouble(0.25, 1.0);  // Between 0.25 and 1.0.

            // Compute dependent properties.
            life_left = MAX_AGE / 10;
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(MAX_FOOD_VALUE);
        } else {
            // Use default base values.
            BREEDING_AGE = 5;
            MAX_AGE = 40;
            BREEDING_PROBABILITY = 0.09;
            DISEASE_PROBABILITY = 0.07; // 0.09 - 0.02.
            MAX_LITTER_SIZE = 4;
            MAX_FOOD_VALUE = 9;
            metabolism = 1.0;
            
            life_left = MAX_AGE / 10;
            age = 0;
            foodLevel = MAX_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newRabbits A list to return newly born rabbits.
     */
    public void act(List<Animal> newRabbits) {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newRabbits);            
            // Try to move into a free location.
            Location newLocation = getField().getFreeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
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
     * Increase the age.
     * This could result in the rabbit's death.
     */
    private void incrementAge() {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Decrease the wolf's food level to simulate hunger.
     * If the food level reaches zero, the wolf dies.
     */
    private void incrementHunger() {
        foodLevel -= 1 + METABOLISM;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    private void giveBirth(List<Animal> newRabbits) {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Rabbit young = new Rabbit(false, field, loc, getColor());
            newRabbits.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed() {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A rabbit can breed if it has reached the breeding age.
     * @return true if the rabbit can breed, false otherwise.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }
    
    public int getFoodValue() {
        return MAX_FOOD_VALUE;
    }
}