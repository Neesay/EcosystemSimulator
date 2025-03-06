import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;

/**
 * An abstract class for all prey animals.
 * Contains common behavior such as aging, hunger, reproduction, disease handling,
 * and grazing (eating grass).
 */
public abstract class Prey extends Animal {
    protected int age;
    protected boolean disease;
    protected int lifeLeft;
    protected int foodLevel;
    protected static final Random rand = Randomizer.getRandom();

    public Prey(Field field, Location location, Color col) {
        super(field, location, col);
    }

    /**
     * Common act method for all prey:
     * - Age, get hungry, reproduce.
     * - Try to graze (eat grass) first; if none is available, move to a free adjacent location.
     * - Handle disease.
     */
    @Override
    public void act(List<Animal> newOffspring) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            giveBirth(newOffspring);
            Location foodLocation = feed();
            if (foodLocation == null) {
                foodLocation = getField().getFreeAdjacentLocation(getLocation());
            }
            if (foodLocation != null) {
                setLocation(foodLocation);
            } else {
                setDead();
            }
            handleDisease();
            diseaseSpread(); // Spread disease to adjacent animals.
        }
    }


    protected void incrementAge() {
        age++;
        if (age > gene.MAX_AGE) {
            setDead();
        }
    }

    protected void incrementHunger() {
        foodLevel -= 1 + gene.METABOLISM;
        if (foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Look for grass in adjacent locations. If found, "eat" it by increasing
     * foodLevel and removing the grass from the field.
     *
     * @return The location where grass was eaten, or null if none found.
     */
    protected Location feed() {
        List<Location> adjacent = getField().adjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Object obj = getField().getObjectAt(loc);
            if (obj instanceof Grass) {
                Grass grass = (Grass) obj;
                int grassFood = grass.getFoodValue();
                foodLevel = Math.min(foodLevel + grassFood, gene.MAX_FOOD_VALUE);
                grass.setDead();
                break;
            }
        }
        return null;
    }



    /**
     * Handles disease: if not diseased, there's a chance to become diseased;
     * if already diseased, reduce lifeLeft and possibly die.
     */
    protected void handleDisease() {
        if (!disease) {
            if (rand.nextDouble() < gene.DISEASE_PROBABILITY) {
                disease = true;
            }
        } else {
            lifeLeft--;
            if (lifeLeft <= 0) {
                setDead();
            }
        }
    }

    /**
     * Generic method for giving birth. Only females give birth.
     * Uses the abstract factory method createYoung(Location) to create a new instance.
     */
    protected void giveBirth(List<Animal> newOffspring) {
        if (getGender() == 1) { // Only females reproduce.
            List<Location> free = getField().getFreeAdjacentLocations(getLocation());
            int births = breed();
            for (int b = 0; b < births && !free.isEmpty(); b++) {
                Location loc = free.remove(0);
                Prey young = createYoung(loc);
                // Find a mate; if none is found, use self as fallback.
                Animal mate = getField().findParent(getLocation(), getGender());
                if (mate == null) {
                    mate = this;
                }
                young.gene = new Gene(this, mate);
                newOffspring.add(young);
            }
        }
    }

    /**
     * Determines the number of births if breeding occurs.
     */
    protected int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= gene.BREEDING_PROBABILITY) {
            births = rand.nextInt(Math.max(1, gene.MAX_LITTER_SIZE)) + 1;
        }
        return births;
    }

    /**
     * Checks whether the prey is old enough to breed and has an opposite-gender neighbor.
     */
    protected boolean canBreed() {
        return age >= gene.BREEDING_AGE && getField().findOppositeGenderAnimal(getLocation(), getGender());
    }

    @Override
    public int getFoodValue() {
        return gene.MAX_FOOD_VALUE;
    }

    /**
     * Factory method to create a new offspring of the concrete prey type.
     * Each concrete prey must implement this method.
     *
     * @param loc The location at which the new prey will be placed.
     * @return A new instance of the prey.
     */
    protected abstract Prey createYoung(Location loc);
}
