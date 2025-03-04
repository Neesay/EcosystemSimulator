import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.LinkedList;
import javafx.scene.paint.Color;

/**
 * A simple model of a coyote.
 * coyotes age, move, eat rabbits, and die.
 *
 * @author Yaseen Alam and Ulvis Turkers
 * @version 03/03/2025
 */

public class Coyote extends Animal {
    
    private static final Random rand = Randomizer.getRandom();
    private int age;
    private int foodLevel;
    private boolean disease = false;
    private int life_left = 12;

    /**
     * Create a coyote. A coyote can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the coyote will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Coyote(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        if(randomAge) {
            foodLevel = rand.nextInt(10);

            gene.BREEDING_AGE = rand.nextInt(12,19);
            gene.MAX_AGE = rand.nextInt(40,60);
            gene.BREEDING_PROBABILITY = rand.nextDouble(0.05,0.12);
            gene.DISEASE_PROBABILITY = gene.BREEDING_PROBABILITY - 0.02;
            gene.MAX_LITTER_SIZE = rand.nextInt(1,4);
            gene.METABOLISM = rand.nextDouble(0.25, 1);

            age = rand.nextInt(1, gene.MAX_AGE);
            createGeneString();
        }
    }

    /**
     * Create a newborn coyote.
     *
     * @param randomAge If true, the coyote will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Coyote(boolean randomAge, Field field, Location location, Color col, Coyote parent) {
        super(field, location, col);
        age = 0;
        foodLevel = 10;

        gene.BREEDING_AGE = Math.min(Math.max(parent.getBreedingAgeFromGene() + rand.nextInt(-3, 4), 12), 90);
        gene.MAX_AGE = Math.min(Math.max(parent.getLifeSpanFromGene() + rand.nextInt(-10, 11), 10), 120);
        gene.BREEDING_PROBABILITY = Math.min(Math.max(parent.getBreedingProbabilityFromGene() + rand.nextDouble(-0.02, 0.02), 0), 0.50);
        gene.DISEASE_PROBABILITY = Math.min(Math.max(gene.BREEDING_PROBABILITY - 0.02, 0), 0.5);
        gene.MAX_LITTER_SIZE = Math.min(Math.max(parent.getLitterSizeFromGene() + rand.nextInt(-1, 2), 1), 12);
        gene.METABOLISM = Math.min(Math.max(parent.getMetabolismFromGene() + rand.nextDouble(-0.1, 0.1), 0.25), 1.0);

        createGeneString();
    }

    /**
     * This is what the coyote does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     */
    public void act(List<Animal> newCoyotes) {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newCoyotes);
            Location newLocation = findFood();
            if(newLocation == null) {
                newLocation = getField().getFreeAdjacentLocation(getLocation());
            }
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                setDead();
            }

            if (!disease){
                double chance = rand.nextDouble();
                if (chance < gene.DISEASE_PROBABILITY) {
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
     * Increase the age. This could result in the coyote's death.
     */
    private void incrementAge() {
        age++;
        if(age > gene.MAX_AGE) {
            setDead();
        }
    }

    /**
     * Make this coyote more hungry. This could result in the coyote's death.
     */
    private void incrementHunger() {
        foodLevel -= 1 + gene.METABOLISM;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Look for prey in adjacent locations.
     */
    private Location findFood() {
        List<Location> adjacent = getField().adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            Object animal = getField().getObjectAt(where);
            if (animal instanceof Squirrel || animal instanceof Mice || animal instanceof Deer) {
                Animal prey = (Animal) animal;
                if (prey.isAlive()) {
                    prey.setDead();
                    foodLevel += prey.getFoodValue();
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Check whether or not this coyote is to give birth at this step.
     */
    private void giveBirth(List<Animal> newCoyotes) {
        List<Location> free = getField().getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Coyote young = new Coyote(false, getField(), loc, getColor(), this);
            newCoyotes.add(young);
        }
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     */
    private int breed() {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= gene.BREEDING_PROBABILITY) {
            births = rand.nextInt(gene.MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A coyote can breed if it has reached the breeding age.
     */
    private boolean canBreed() {
        return age >= gene.BREEDING_AGE;
    }

    @Override
    public int getFoodValue() {
        return 0;
    }
}
