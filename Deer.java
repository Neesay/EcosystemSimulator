import javafx.scene.paint.Color;
import java.util.List;

public class Deer extends Prey {

    public Deer(Field field, Location location, Color col) {
        super(field, location, col);
        foodLevel = rand.nextInt(8);
        gene.BREEDING_AGE = Gene.clampInt(rand.nextInt(9, 16), 12, 90);
        gene.MAX_AGE = Gene.clampInt(rand.nextInt(45, 65), 10, 120);
        gene.BREEDING_PROBABILITY = Gene.clampDouble(rand.nextDouble(0.140, 0.321), 0.0, 0.50);
        gene.DISEASE_PROBABILITY = Gene.clampDouble(rand.nextDouble(0.15, 0.36), 0.0, 0.50);
        gene.MAX_LITTER_SIZE = Gene.clampInt(rand.nextInt(1, 3), 1, 12);
        gene.MAX_FOOD_VALUE = rand.nextInt(15, 22);
        gene.METABOLISM = Gene.clampDouble(rand.nextDouble(0.25, 1.0), 0.25, 1.0);
        age = rand.nextInt(1, gene.MAX_AGE);
        lifeLeft = 6;
        disease = false;
        createGeneString();
    }

    @Override
    protected Prey createOffspring(Location loc) {
        return new Deer(getField(), loc, getColor());
    }

    /**
     * Deer-specific act behavior:
     * - If a predator is nearby, try to flee to a safer location.
     * - Otherwise, stay put and graze (regain a small amount of food).
     * Common behaviors (aging, hunger, reproduction, disease handling) are inherited.
     */
    @Override
    public void act(List<Animal> newOffspring) {
        // Perform common behavior: aging and hunger.
        incrementAge();
        incrementHunger();
        if (!isAlive()) return;

        // Attempt reproduction.
        giveBirth(newOffspring);

        // Check for nearby predators.
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
            // Flight Response: attempt to move to a safer location.
            Location safe = chooseSafeLocation();
            if (safe != null) {
                setLocation(safe);
            } else {
                // If no safe cell found, try any free adjacent cell.
                Location free = getField().getFreeAdjacentLocation(getLocation());
                if (free != null) {
                    setLocation(free);
                } else {
                    setDead();
                    return;
                }
            }
        } else {
            // Grazing Behavior: remain in place and slowly regain food.
            int grazingBonus = 2;
            foodLevel = Math.min(foodLevel + grazingBonus, getFoodValue());
        }

        handleDisease();
        diseaseSpread();
    }

    /**
     * Chooses a free adjacent location that minimizes predator risk.
     * Evaluates risk by counting the number of predators in adjacent cells.
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
     * Evaluates the risk of a given location by counting predators in its vicinity.
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
}
