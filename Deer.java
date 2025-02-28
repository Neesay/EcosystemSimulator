import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;

/**
 * A simple model of a deer.
 * Deer are prey animals that age, move, breed, and eventually die.
 * They breed more slowly compared to rabbits and have a low food value.
 *
 * Deer characteristics:
 * - Breeding age: 10
 * - Maximum age: 80
 * - Breeding probability: 0.10
 * - Maximum litter size: 2
 * - Food value: 18 (derived from Rabbit.getRabbitFoodValue())
 *
 * This class follows a similar structure to the Rabbit class.
 */
public class Deer extends Animal {

    // Constants that define deer behavior and properties.
    private static final int BREEDING_AGE = 10;
    private static final int MAX_AGE = 80;
    private static final double BREEDING_PROBABILITY = 0.10;
    private static final double DISEASE_PROBABILITY = BREEDING_PROBABILITY - 0.02;
    private static final int MAX_LITTER_SIZE = 2;
    private static final int MAX_FOOD_VALUE = 18;
    
    // Random number generator for controlling random events like breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Instance variable to track the age of the deer.
    private int age;
    private boolean disease = false;
    private int life_left = MAX_AGE/10;
    private double metabolism; 
    private int foodLevel;

    /**
     * Create a new deer.
     * If randomAge is true, the deer is assigned a random age (up to its maximum age).
     * Otherwise, it starts as a newborn.
     *
     * @param randomAge If true, initialize the deer with a random age.
     * @param field The field where the deer exists.
     * @param location The initial location of the deer within the field.
     * @param col The color representing the deer.
     */
    public Deer(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(MAX_FOOD_VALUE);
            metabolism = rand.nextDouble(0.25, 1);
        } else {
            age = 0;
        
            // Start with the full food level provided by the rabbit's food value.
            foodLevel = MAX_FOOD_VALUE;
        }
    }
    
    /**
     * Define the behavior of the deer during each simulation step.
     * The deer ages and may breed.
     * Then it attempts to move into a free adjacent location.
     * If no such location is available (overcrowding), the deer dies.
     *
     * @param newDeer A list to store any newly born deer.
     */
    public void act(List<Animal> newDeer) {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newDeer);
            // Try to move into a free adjacent location.
            Location newLocation = getField().getFreeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            } else {
                // Death occurs if the deer cannot move due to overcrowding.
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
     * Increase the deer's age. If the deer exceeds its maximum age, it dies.
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
        foodLevel -= 1 + metabolism;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Allow the deer to give birth to new deer.
     * The number of births is determined by the deer's breeding probability and litter size.
     *
     * @param newDeer A list to add any newly born deer.
     */
    private void giveBirth(List<Animal> newDeer) {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Deer young = new Deer(false, field, loc, getColor());
            newDeer.add(young);
        }
    }
    
    /**
     * Determine the number of births based on breeding conditions.
     * A deer will produce a number of offspring if it has reached breeding age
     * and a random chance (based on BREEDING_PROBABILITY) succeeds.
     *
     * @return The number of new deer born (could be zero).
     */
    private int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }
    
    /**
     * Check whether the deer is old enough to breed.
     *
     * @return True if the deer's age is at least the breeding age.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }
    
    /**
     * Get the food value of the deer.
     * This value is used by predators when consuming a deer.
     *
     * @return The food value of a deer (18).
     */
    public int getFoodValue() {
        return MAX_FOOD_VALUE;
    }
}
