import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;

public class Deer extends Animal {    
    // Instance variables.
    private int age;
    private boolean disease = false;
    private int life_left = 8;
    private int foodLevel;

    // Random number generator.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a new deer.
     * If randomAge is true, the deer's properties are randomly chosen within the specified ranges.
     * Otherwise, default base values are used.
     *
     * Ranges when randomAge is true:
     * - BREEDING_AGE: [7, 13]
     * - MAX_AGE: [65, 95]
     * - BREEDING_PROBABILITY: [0.07, 0.14)
     * - DISEASE_PROBABILITY: BREEDING_PROBABILITY - 0.02
     * - MAX_LITTER_SIZE: either 1 or 2
     * - MAX_FOOD_VALUE: [15, 21]
     * - METABOLISM: [0.25, 1.0)
     *
     * @param randomAge If true, assign random property values; otherwise, use fixed defaults.
     * @param field The field where the deer exists.
     * @param location The initial location of the deer within the field.
     * @param col The color representing the deer.
     */
    public Deer(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        if(randomAge) {
            // Assign random values within the specified ranges.
            BREEDING_AGE = rand.nextInt(7, 14);  // Generates a value between 7 and 13.
            MAX_AGE = rand.nextInt(65, 96);        // Generates a value between 65 and 95.
            BREEDING_PROBABILITY = rand.nextDouble(0.07, 0.14);  // Value between 0.07 and 0.14.
            DISEASE_PROBABILITY = BREEDING_PROBABILITY - 0.02;
            MAX_LITTER_SIZE = Math.max(1, rand.nextInt(1, 3)); // Generates either 1 or 2.
            MAX_FOOD_VALUE = rand.nextInt(15, 22);  // Generates a value between 15 and 21.
            METABOLISM = rand.nextDouble(0.25, 1.0);  // Value between 0.25 and 1.0.

            // Calculate dependent properties.
            life_left = MAX_AGE / 10;
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(MAX_FOOD_VALUE);
        } else {
            // Use default base values.
            BREEDING_AGE = 10;
            MAX_AGE = 80;
            BREEDING_PROBABILITY = 0.10;
            DISEASE_PROBABILITY = BREEDING_PROBABILITY - 0.02;
            MAX_LITTER_SIZE = 2;
            MAX_FOOD_VALUE = 18;
            METABOLISM = 1.0;

            life_left = MAX_AGE / 10;
            age = 0;
            foodLevel = MAX_FOOD_VALUE;
        }
    }
    
    
    // Additional methods (act, incrementAge, giveBirth, etc.) would follow here.


    
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
        foodLevel -= 1 + METABOLISM;
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
