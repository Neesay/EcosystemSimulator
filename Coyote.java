import javafx.scene.paint.Color;
import java.util.List;

public class Coyote extends Predator {

    public Coyote(Field field, Location location, Color col) {
        super(field, location, col);
        foodLevel = rand.nextInt(8);
        gene.BREEDING_AGE = rand.nextInt(10, 17);
        gene.MAX_AGE = rand.nextInt(30, 40);
        gene.BREEDING_PROBABILITY = rand.nextDouble(0.3, 0.35);
        gene.DISEASE_PROBABILITY = rand.nextDouble(0.31, 0.36);
        gene.MAX_LITTER_SIZE = rand.nextInt(1, 4);
        gene.METABOLISM = rand.nextDouble(0.25, 1);
        age = rand.nextInt(1, gene.MAX_AGE);
        lifeLeft = 12;
        createGeneString();
    }

    @Override
    protected Predator createOffspring(Location loc) {
        return new Coyote(getField(), loc, getColor());
    }

    /**
     * Alternate behavior: act() checks for nearby coyotes to decide if it is in group mode.
     * In group mode, the coyote might gain extra advantages (e.g., enhanced scavenging bonus).
     * If solitary, it may move extra quickly.
     */
    @Override
    public void act(List<Animal> newPredators) {
        List<Animal> neighbours = getField().getLivingNeighbours(getLocation());
        int groupCount = 0;
        for (Animal animal : neighbours) {
            if (animal instanceof Coyote && animal != this) {
                groupCount++;
            }
        }
        boolean inGroup = groupCount >= 2;

        super.act(newPredators);

        // If solitary (not in a group) and still alive, make an extra move to simulate speed.
        if (!inGroup && isAlive()) {
            Location extraMove = getField().getFreeAdjacentLocation(getLocation());
            if (extraMove != null) {
                setLocation(extraMove);
            }
        }
    }

    /**
     * Opportunistic scavenging: attempt normal hunting first. If no live prey is found,
     * look for adjacent grass patches that are very young (assumed to indicate recent carcasses).
     * With a 50% chance, treat the fresh carcass as a food source, add bonus food, and return its location.
     */
    @Override
    protected Location findFood() {
        Location foodLocation = super.findFood();
        if (foodLocation != null) {
            return foodLocation;
        }
        // No live prey found; try scavenging.
        List<Location> adjacent = getField().adjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Object obj = getField().getObjectAt(loc);
            if (obj instanceof Grass grass) {
                // Assume fresh carcass is indicated by a young grass patch (e.g., age less than 2).
                if (grass.getAge() < 2) {
                    if (Randomizer.getRandom().nextDouble() < 0.5) {
                        int scavengedBonus = 3;
                        foodLevel += scavengedBonus;
                        //System.out.println("Coyote at " + getLocation() + " scavenged a carcass for a bonus of " + scavengedBonus + " food units.");
                        return loc;
                    }
                }
            }
        }
        return null;
    }
}
