import javafx.scene.paint.Color;

/**
 * A model of a coyote. Extends Predator.
 */
public class Coyote extends Predator {

    public Coyote(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        // Initialize gene parameters for Coyote.
        foodLevel = rand.nextInt(10);
        gene.BREEDING_AGE = rand.nextInt(12, 19);
        gene.MAX_AGE = rand.nextInt(40, 60);
        gene.BREEDING_PROBABILITY = rand.nextDouble(0.48, 0.5);
        gene.DISEASE_PROBABILITY = gene.BREEDING_PROBABILITY - 0.02;
        gene.MAX_LITTER_SIZE = rand.nextInt(1, 4);
        gene.METABOLISM = rand.nextDouble(0.25, 1);
        age = rand.nextInt(1, gene.MAX_AGE);
        lifeLeft = 12;
        createGeneString();
    }

    @Override
    protected Predator createYoung(Location loc) {
        return new Coyote(false, getField(), loc, getColor());
    }

    // The act() method is inherited from Predator.
}
