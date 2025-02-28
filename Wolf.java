import java.util.List;
import java.util.Iterator;
import java.util.Random;
import javafx.scene.paint.Color;

/**
 * A simple model of a wolf.
 * Wolves are predators that age, move, hunt rabbits, breed slowly, and eventually die.
 * This class follows a similar structure to the Fox class.
 * 
 * Wolves have a breeding age of 20, a maximum age of 130,
 * a breeding probability of 0.06, and a maximum litter size of 2.
 * Instead of using a constant food value, the wolf obtains its food value
 * by calling the Rabbit.getRabbitFoodValue() method.
 * 
 * @author 
 * @version 
 */
public class Wolf extends Animal {
    // Constants specific to wolves.
    private static int BREEDING_AGE = 20;
    private static int MAX_AGE = 130;
    private static double BREEDING_PROBABILITY = 0.06;
    private static double DISEASE_PROBABILITY = BREEDING_PROBABILITY - 0.02;
    private static int MAX_LITTER_SIZE = 2;
    private static final int MAX_FOOD_VALUE = 15; //might have to adjust later
    private static double METABOLISM;
    
    // A shared random number generator for controlling breeding and other random behaviors.
    private static final Random rand = Randomizer.getRandom();
    
    // Instance variables to track the wolf's age and hunger level.
    private int age;
    private int foodLevel;
    private Rabbit rabbit;
    private boolean disease = false;
    private int life_left = MAX_AGE/10;

    /**
     * Construct a new Wolf.
     * If randomAge is true, the wolf will have a random age and a random food level based on
     * the rabbit's food value. Otherwise, it starts as a newborn with a full food level.
     * 
     * @param randomAge If true, initialize with a random age and hunger level.
     * @param field The field in which the wolf exists.
     * @param location The wolf's initial location within the field.
     * @param col The color used to represent the wolf.
     */
    public Wolf(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            // Set the food level to a random value up to the rabbit's food value.
            foodLevel = rand.nextInt(MAX_FOOD_VALUE);
            METABOLISM = rand.nextDouble(0.25, 1);
        } else {
            age = 0;
        
            // Start with the full food level provided by the rabbit's food value.
            foodLevel = MAX_FOOD_VALUE;
        }
        
    }
    
    /**
     * Define the wolf's behavior during its turn.
     * The wolf ages, gets hungrier, may give birth, hunts for food,
     * and moves to a new location if possible.
     * 
     * @param newWolves A list to add newly born wolves.
     */
    public void act(List<Animal> newWolves) {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newWolves);
            // Try to find food in adjacent locations.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // If no food is found, attempt to move to a free adjacent location.
                newLocation = getField().getFreeAdjacentLocation(getLocation());
            }
            // Move to the new location if one was found.
            if(newLocation != null) {
                setLocation(newLocation);
            } else {
                // No movement possible due to overcrowding, so the wolf dies.
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
     * Increase the wolf's age. If the wolf exceeds its maximum age, it dies.
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
     * Look for prey in adjacent locations. The wolf will check for rabbits, deer, or mice.
     * If a live prey is found, the wolf kills it and restores its food level based on the prey's food value.
     *
     * @return The location where prey was found, or null if no prey is present.
     */
    private Location findFood() {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            // Check if the object is an instance of Rabbit, Mice, or Deer.
            if (animal instanceof Rabbit || animal instanceof Mice || animal instanceof Deer) {
                Animal prey = (Animal) animal;
                if (prey.isAlive()) {
                    // Kill the prey and restore the wolf's food level based on the prey's food value.
                    prey.setDead();
                    foodLevel += prey.getFoodValue();
                    return where;
                }
            }
        }
        return null;
    }

    
    /**
     * Allow the wolf to give birth to new wolves in free adjacent locations.
     * 
     * @param newWolves The list where newly born wolves will be added.
     */
    private void giveBirth(List<Animal> newWolves) {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Wolf young = new Wolf(false, field, loc, getColor());
            newWolves.add(young);
        }
    }
    
    /**
     * Determine the number of births based on the wolf's breeding probability and litter size.
     * 
     * @return The number of new wolves (could be zero if breeding does not occur).
     */
    private int breed() {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }
    
    /**
     * Check if the wolf is old enough to breed.
     * 
     * @return True if the wolf's age is at least the breeding age.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }
    
    @Override
    public int getFoodValue() {
        return 0; // Predators aren't meant to be food.
    }

}
