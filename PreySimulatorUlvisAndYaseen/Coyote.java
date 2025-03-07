import javafx.scene.paint.Color;
import java.util.List;

/**
 * Represents a Coyote, a type of Predator, with specific behaviours and gene settings.
 * This class utilises random gene values and food levels during initialisation.
 *
 * @author Yaseen Alam and Ulvis Turkers
 * @version 04/03/2025
 */
public class Coyote extends Predator {

    /**
     * Constructs a new Coyote with initial gene and food level values.
     *
     * @param field    The field in which the coyote is placed.
     * @param location The location within the field.
     * @param col      The initial colour of the coyote.
     */
    public Coyote(Field field, Location location, Color col) {
        super(field, location, col);
        // Initialise the food level with a random value up to 7.
        foodLevel = rand.nextInt(8);
        // Set gene attributes with random values.
        gene.BREEDING_AGE = rand.nextInt(10, 17);
        gene.MAX_AGE = rand.nextInt(30, 40);
        gene.BREEDING_PROBABILITY = rand.nextDouble(0.3, 0.35);
        gene.DISEASE_PROBABILITY = rand.nextDouble(0.31, 0.36);
        gene.MAX_LITTER_SIZE = rand.nextInt(1, 4);
        gene.METABOLISM = rand.nextDouble(0.25, 1);
        // Initialise the age of the coyote to a random value within its maximum age.
        age = rand.nextInt(1, gene.MAX_AGE);
        // Set the remaining life of the coyote.
        lifeLeft = 10;
        // Create the gene string for this coyote.
        createGeneString();
    }

    /**
     * Creates an offspring of this coyote at the specified location.
     *
     * @param loc The location for the new offspring.
     * @return A new Coyote object representing the offspring.
     */
    @Override
    protected Predator createOffspring(Location loc) {
        return new Coyote(getField(), loc, getColor());
    }

    /**
     * Defines the behaviour of the coyote for a single simulation step.
     * This method first determines if the coyote is in a group by counting neighbouring coyotes.
     * It then calls the superclass act method to perform common predator actions.
     * If the coyote is solitary (not in a group) and still alive, it makes an extra move to simulate speed.
     *
     * @param newPredators A list to collect newly created predators.
     */
    @Override
    public void act(List<Animal> newPredators) {
        // Get a list of living neighbouring animals.
        List<Animal> neighbours = getField().getLivingNeighbours(getLocation());
        int groupCount = 0;
        // Count how many neighbours are also coyotes (excluding itself).
        for (Animal animal : neighbours) {
            if (animal instanceof Coyote && animal != this) {
                groupCount++;
            }
        }
        boolean inGroup = groupCount >= 2;

        // Perform common predator actions.
        super.act(newPredators);

        // If solitary and still alive, attempt an extra move to simulate enhanced speed.
        if (!inGroup && isAlive()) {
            Location extraMove = getField().getFreeAdjacentLocation(getLocation());
            if (extraMove != null) {
                setLocation(extraMove);
            }
        }
    }

    /**
     * Attempts to find food for the coyote.
     * First, it attempts normal hunting by utilising the superclass's findFood method.
     * If no live prey is found, it then attempts opportunistic scavenging.
     * Scavenging involves checking adjacent locations for grass patches that are very young,
     * which are assumed to indicate recent carcasses. With a 50% chance, the coyote treats the
     * fresh carcass as a food source, gains bonus food, and returns its location.
     *
     * @return The location where food was found, or null if no food was located.
     */
    @Override
    protected Location findFood() {
        // Attempt to find live prey first.
        Location foodLocation = super.findFood();
        if (foodLocation != null) {
            return foodLocation;
        }

        // 20% chance of not finding any food.
        if (rand.nextDouble(1) < 0.2) {
            return null;
        }

        // No live prey found; try scavenging for recent carcasses.
        List<Location> adjacent = getField().adjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Object obj = getField().getObjectAt(loc);
            if (obj instanceof Grass grass) {
                // Assume that a young grass patch (age less than 2) indicates a fresh carcass.
                if (grass.getAge() < 2) {
                    // With a 50% chance, utilise the carcass for bonus food.
                    if (Randomizer.getRandom().nextDouble() < 0.5) {
                        int scavengedBonus = 1;
                        foodLevel += scavengedBonus;
                        return loc;
                    }
                }
            }
        }
        // No food found after scavenging.
        return null;
    }
}
