import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;

/**
 * An abstract class for all predators.
 * Contains common behavior for predators: aging, hunger, reproduction, disease handling,
 * and hunting prey (using instanceof Prey).
 */
public abstract class Predator extends Animal {
    protected int age;
    protected int foodLevel;
    protected int lifeLeft;
    protected static final Random rand = Randomizer.getRandom();

    public Predator(Field field, Location location, Color col) {
        super(field, location, col);
    }

    /**
     * Common act method for predators:
     * - Increment age and hunger.
     * - Attempt reproduction.
     * - Hunt for food (by checking for any Prey).
     * - Move to a new location (food or free adjacent).
     * - Handle disease.
     * - Spread disease to adjacent animals of the same species.
     */
    @Override
    public void act(List<Animal> newPredators) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            giveBirth(newPredators);
            Location foodLocation = findFood();
            Location newLocation = (foodLocation != null)
                    ? foodLocation
                    : getField().getFreeAdjacentLocation(getLocation());
            if (newLocation != null) {
                setLocation(newLocation);
            } else {
                setDead();
                return;
            }
            handleDisease();
            diseaseSpread();
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
     * Handles the disease state.
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
     * Looks for food in adjacent locations.
     * Checks if the animal is an instance of Prey.
     * If a live prey is found, it is killed, foodLevel is increased, and its location returned.
     */
    protected Location findFood() {
        List<Location> adjacent = getField().adjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Object animal = getField().getObjectAt(loc);
            if (animal instanceof Prey) {
                Animal prey = (Animal) animal;
                if (prey.isAlive()) {
                    prey.setDead();
                    foodLevel += prey.getFoodValue();
                    return loc;
                }
            }
        }
        return null;
    }

    /**
     * Handles reproduction. Only females reproduce.
     * Uses a factory method createYoung(Location) to create an offspring.
     */
    protected void giveBirth(List<Animal> newPredators) {
        if (getGender() == 1) { // Only females reproduce.
            List<Location> free = getField().getFreeAdjacentLocations(getLocation());
            int births = breed();
            for (int b = 0; b < births && !free.isEmpty(); b++) {
                Location loc = free.remove(0);
                Predator young = createYoung(loc);
                // Find a mate; if none is found, use this as fallback.
                Animal mate = getField().findParent(getLocation(), getGender());
                if (mate == null) {
                    mate = this;
                }
                young.gene = new Gene(this, mate);
                newPredators.add(young);
            }
        }
    }

    /**
     * Returns the number of births, if breeding occurs.
     */
    protected int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= gene.BREEDING_PROBABILITY) {
            births = rand.nextInt(Math.max(1, gene.MAX_LITTER_SIZE)) + 1;
        }
        return births;
    }

    /**
     * Determines if the predator is old enough to breed and has an opposite-gender neighbor.
     */
    protected boolean canBreed() {
        return age >= gene.BREEDING_AGE && getField().findOppositeGenderAnimal(getLocation(), getGender());
    }

    /**
     * Factory method to create a new offspring.
     * Each concrete predator must implement this method.
     */
    protected abstract Predator createYoung(Location loc);

    @Override
    public int getFoodValue() {
        // Predators are not prey.
        return 0;
    }
    
    public boolean isDiseased() {
        return disease;
    }
    
    /**
     * Sets the disease status of this animal.
     * @param diseased true if the animal should be marked as diseased.
     */
    public void setDiseased(boolean disease) {
        this.disease = disease;
        System.out.println("called setDiseased()");
    }
    
    /**
     * Spread disease to adjacent animals of the same species.
     * For each adjacent animal (of the same class) that is not already diseased,
     * it is infected with a probability of 0.05.
     */
    public void diseaseSpread() {
        double prob_of_spread = 0.05; // 5% infection chance for each adjacent animal.
        if (isDiseased()){
            System.out.println("diseaseSpread() called for animal at " + getLocation() + ". Diseased: " + isDiseased());
        }
        // Only spread disease if this animal is already diseased.
        if (!isDiseased()) {
            return;
        }

        if (!isAlive()) {
            return;
        }
        
        // Retrieve all adjacent locations.
        List<Location> adjacent = getField().adjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Animal other = getField().getObjectAt(loc);
            if (other != null && other.getClass().equals(this.getClass()) && !other.isDiseased()) {
                System.out.println("Checking adjacent animal at " + loc + " (" + other.getClass().getSimpleName() + ")");
                if (Randomizer.getRandom().nextDouble() < prob_of_spread) {
                    other.setDiseased(true);
                    System.out.println("Infected animal at " + loc);
                } else {
                    System.out.println("Did not infect animal at " + loc);
                }
            }
        }
    }
}
    