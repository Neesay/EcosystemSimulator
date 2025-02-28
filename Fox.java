import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.LinkedList;
import javafx.scene.paint.Color; 

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2025.02.10
 */

public class Fox extends Animal {

    private static int BREEDING_AGE = 15;
    private static int MAX_AGE = 120;
    private static double BREEDING_PROBABILITY = 0.08;
    private static double DISEASE_PROBABILITY = BREEDING_PROBABILITY - 0.02;
    private static int MAX_LITTER_SIZE = 2;
    private static int MAX_FOOD_VALUE = 10; //might have to adjust later

    private static final Random rand = Randomizer.getRandom();
    private int age;
    private int foodLevel;
    private Rabbit rabbit;
    private boolean disease = false;
    private int life_left = MAX_AGE/10;
    private static double METABOLISM;

    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Fox(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(MAX_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = MAX_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newFoxes A list to return newly born foxes.
     */
    public void act(List<Animal> newFoxes) {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newFoxes);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().getFreeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
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
     * Increase the age. This could result in the fox's death.
     */
    private void incrementAge() {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this fox more hungry. This could result in the fox's death.
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
     * Check whether or not this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newFoxes A list to return newly born foxes.
     */
    private void giveBirth(List<Animal> newFoxes) {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Fox young = new Fox(false, field, loc, getColor());
            newFoxes.add(young);
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
     * A fox can breed if it has reached the breeding age.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }
    
    @Override
    public int getFoodValue() {
        return 0; // Predators aren't meant to be food.
    }
}