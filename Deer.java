import java.util.List;
import javafx.scene.paint.Color;

/**
 * A model of a deer. Extends Prey.
 * This class initializes a Deer with random genetic parameters and defines its behavior.
 */
public class Deer extends Prey {

    /**
     * Constructs a new Deer using random genetic parameters.
     * @param field The simulation field where the deer is placed.
     * @param location The initial location of the deer.
     * @param col The color representing the deer.
     */
    public Deer(Field field, Location location, Color col) {
        super(field, location, col);
        // Set genetic parameters using random values.
        gene.BREEDING_AGE = rand.nextInt(9, 16);
        gene.MAX_AGE = rand.nextInt(45, 65);
        gene.BREEDING_PROBABILITY = rand.nextDouble(0.13, 0.18);
        gene.MAX_LITTER_SIZE = rand.nextInt(1, 2);
        gene.MAX_FOOD_VALUE = rand.nextInt(15, 22);
        gene.DISEASE_PROBABILITY = rand.nextDouble(0.05, 0.1);
        gene.METABOLISM = rand.nextDouble(0.25, 1.0);
        
        // Initialize state variables.
        age = rand.nextInt(1, gene.MAX_AGE);
        foodLevel = rand.nextInt(gene.MAX_FOOD_VALUE);
        lifeLeft = 6;
        disease = false;
        createGeneString();
    }

    /**
     * Deer-specific act behavior:
     * - Ages and gets hungry.
     * - Attempts reproduction.
     * - Checks for nearby predators to decide whether to flee.
     * - If no predators are nearby, grazes to regain food.
     * - Handles disease and spreads it.
     * @param newOffspring A list to receive any new offspring produced.
     */
    @Override
    public void act(List<Animal> newOffspring) {
        incrementAge();
        incrementHunger();
        if (!isAlive()) return;
        
        giveBirth(newOffspring);
        
        List<Location> adjacent = getField().adjacentLocations(getLocation());
        boolean predatorNearby = false;
        for (Location loc : adjacent) {
            Animal a = getField().getObjectAt(loc);
            if (a instanceof Predator) {
                predatorNearby = true;
                break;
            }
        }
        
        if (predatorNearby) {
            // Flight response: move to a safer location.
            Location safe = chooseSafeLocation();
            if (safe != null) {
                setLocation(safe);
            } else {
                Location free = getField().getFreeAdjacentLocation(getLocation());
                if (free != null) {
                    setLocation(free);
                } else {
                    setDead();
                    return;
                }
            }
        } else {
            // Grazing behavior: stay in place and regain some food.
            int grazingBonus = 2;
            foodLevel = Math.min(foodLevel + grazingBonus, getFoodValue());
        }
        
        handleDisease();
        diseaseSpread();
    }
    
    /**
     * Chooses a free adjacent location with minimal predator risk.
     * @return The chosen safe location, or null if none available.
     */
    private Location chooseSafeLocation() {
        List<Location> freeLocations = getField().getFreeAdjacentLocations(getLocation());
        if (freeLocations.isEmpty()) return null;
        Location best = null;
        int bestRisk = Integer.MAX_VALUE;
        for (Location loc : freeLocations) {
            int risk = evaluateRisk(loc);
            if (risk < bestRisk) {
                bestRisk = risk;
                best = loc;
            }
        }
        return best;
    }
    
    /**
     * Evaluates the risk of a location by counting predators in adjacent cells.
     * @param loc The location to evaluate.
     * @return The risk score.
     */
    private int evaluateRisk(Location loc) {
        int risk = 0;
        List<Location> neighbours = getField().adjacentLocations(loc);
        for (Location n : neighbours) {
            Animal a = getField().getObjectAt(n);
            if (a instanceof Predator) {
                risk++;
            }
        }
        return risk;
    }
    
    /**
     * Factory method to create a new Deer offspring at the given location.
     * @param loc The location for the offspring.
     * @return A new Deer instance.
     */
    @Override
    protected Prey createOffspring(Location loc) {
        return new Deer(getField(), loc, getColor());
    }
}
