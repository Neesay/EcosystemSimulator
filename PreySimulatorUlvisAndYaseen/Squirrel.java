import javafx.scene.paint.Color;
import java.util.List;

public class Squirrel extends Prey {

    public Squirrel(Field field, Location location, Color col) {
        super(field, location, col);
        // Initialize gene parameters for Squirrel.
        gene.BREEDING_AGE = rand.nextInt(3, 8);
        gene.MAX_AGE = rand.nextInt(6, 48);
        gene.BREEDING_PROBABILITY = rand.nextDouble(0.4, 0.45);
        gene.DISEASE_PROBABILITY = rand.nextDouble(0.05, 0.1);
        gene.MAX_LITTER_SIZE = Math.max(1, rand.nextInt(6, 10));
        gene.MAX_FOOD_VALUE = rand.nextInt(12, 16); 
        gene.METABOLISM = rand.nextDouble(0.1, 0.3);  

        age = rand.nextInt(gene.MAX_AGE);
        foodLevel = rand.nextInt(gene.MAX_FOOD_VALUE);
        lifeLeft = 10;
        disease = false;
        createGeneString();
    }

    @Override
    protected Prey createOffspring(Location loc) {
        return new Squirrel(getField(), loc, getColor());
    }
    
    /**
     * Override the canBreed() method to relax the requirement for an adjacent opposite-gender animal.
     * This change allows squirrels to reproduce as soon as they reach breeding age,
     * addressing the issue of low reproduction rates.
     */
    @Override
    protected boolean canBreed() {
        return age >= gene.BREEDING_AGE;
    }

    /**
     * Override act() to add erratic movement and a hiding mechanism.
     * - If a predator is detected in an adjacent cell, the squirrel remains in place (hides).
     * - Otherwise, with a 20% chance, it makes an erratic jump (a random free location within a ±3 cell range).
     *   If no erratic move is chosen or available, it uses a standard adjacent free cell.
     */
    @Override
    public void act(List<Animal> newOffspring) {
        // Perform common behaviors.
        incrementAge();
        incrementHunger();
        if (!isAlive()) return;
        giveBirth(newOffspring);

        // Check for predators in adjacent locations.
        boolean predatorNearby = false;
        List<Location> adjacent = getField().adjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Animal a = getField().getObjectAt(loc);
            if (a != null && a instanceof Predator) {
                predatorNearby = true;
                break;
            }
        }

        if (predatorNearby) {
            // Hiding: do not move this turn.
            //System.out.println("Squirrel at " + getLocation() + " is hiding due to predator presence.");
        } else {
            // Erratic Movement: 20% chance to jump erratically.
            Location newLocation = null;
            if (rand.nextDouble() < 0.2) {
                newLocation = chooseErraticLocation();
            }
            if (newLocation == null) {
                newLocation = getField().getFreeAdjacentLocation(getLocation());
            }
            if (newLocation != null) {
                setLocation(newLocation);
                //System.out.println("Squirrel moves to " + newLocation);
            } else {
                setDead();
                return;
            }
        }

        handleDisease();
        diseaseSpread();
    }

    /**
     * Attempts to find a random free location within a larger range (±3 cells) to simulate erratic jumping.
     * Tries up to 5 times before giving up.
     * @return A free Location if found, or null otherwise.
     */
    private Location chooseErraticLocation() {
        Field field = getField();
        int depth = field.getDepth();
        int width = field.getWidth();
        for (int i = 0; i < 5; i++) {
            int deltaRow = rand.nextInt(7) - 3; // Range: -3 to +3
            int deltaCol = rand.nextInt(7) - 3;
            int newRow = getLocation().getRow() + deltaRow;
            int newCol = getLocation().getCol() + deltaCol;
            if (newRow >= 0 && newRow < depth && newCol >= 0 && newCol < width) {
                Location candidate = new Location(newRow, newCol);
                if (field.getObjectAt(candidate) == null) {
                    return candidate;
                }
            }
        }
        return null;
    }
}
