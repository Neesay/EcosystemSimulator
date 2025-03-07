import javafx.scene.paint.Color;
import java.util.List;

/**
 * A model of a wolf. Extends Predator.
 */
public class Wolf extends Predator {
    private Location territoryCenter;

    public Wolf(Field field, Location location, Color col) {
        super(field, location, col);
        // Initialize gene parameters for Wolf.
        foodLevel = rand.nextInt(14); // base food value of 15.
        gene.BREEDING_AGE = rand.nextInt(15, 21);
        gene.MAX_AGE = rand.nextInt(40, 50);
        gene.BREEDING_PROBABILITY = rand.nextDouble(0.195, 0.24);
        gene.DISEASE_PROBABILITY = rand.nextDouble(0.36, 0.41);
        gene.MAX_LITTER_SIZE = Math.max(1, rand.nextInt(1, 3));
        gene.METABOLISM = rand.nextDouble(0.25, 1);
        age = rand.nextInt(1, gene.MAX_AGE);
        lifeLeft = 13;
        createGeneString();
    }

    @Override
    protected Predator createOffspring(Location loc) {
        return new Wolf(getField(), loc, getColor());
    }

    @Override
    protected Location findFood() {
        // First, try to find food using the default predator behavior
        Location foodLocation = super.findFood();
        if (foodLocation != null) {
            // If food is found, check for nearby wolves (pack members)
            List<Animal> neighbours = getField().getLivingNeighbours(getLocation());
            int packCount = 0;
            for (Animal animal : neighbours) {
                if (animal instanceof Wolf && animal != this) {
                    packCount++;
                }
            }
            if (packCount > 0) {
                // Apply a bonus to foodLevel for each nearby wolf
                int bonus = packCount * 2; // e.g., each wolf in the pack adds 2 food units
                foodLevel += bonus;
            }
        }
        return foodLocation;
    }

    @Override
    public void act(List<Animal> newAnimals) {
        // Perform standard predator behavior first
        super.act(newAnimals);

        // If the wolf is no longer alive, exit early.
        if (!isAlive()) return;

        // Check for nearby pack members
        List<Animal> neighbours = getField().getLivingNeighbours(getLocation());
        int packCount = 0;
        for (Animal animal : neighbours) {
            if (animal instanceof Wolf && animal != this) {
                packCount++;
            }
        }

        if (packCount > 0) {
            // If pack exists, mark territory: set territory center if not already set
            if (territoryCenter == null) {
                territoryCenter = getLocation();
            }
        } else {
            // Clear territory if no pack members are around
            territoryCenter = null;
        }

        // If a territory center is set and the wolf isn't already there,
        // choose an adjacent free location that is closer to the territory center.
        if (territoryCenter != null && !getLocation().equals(territoryCenter)) {
            Location best = chooseLocationCloserTo(territoryCenter);
            if (best != null) {
                setLocation(best);
            }
        }
    }

    // Choose an adjacent free location that brings the wolf closer to the target (territory center)
    private Location chooseLocationCloserTo(Location target) {
        List<Location> freeLocations = getField().getFreeAdjacentLocations(getLocation());
        if (freeLocations.isEmpty()) return null;
        Location current = getLocation();
        int currentDistance = distance(current, target);
        Location best = null;
        int bestDistance = currentDistance;
        for (Location loc : freeLocations) {
            int d = distance(loc, target);
            if (d < bestDistance) {
                bestDistance = d;
                best = loc;
            }
        }
        return best;
    }

    // Compute Manhattan distance between two locations
    private int distance(Location a, Location b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }
}

