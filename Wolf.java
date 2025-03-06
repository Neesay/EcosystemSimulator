import javafx.scene.paint.Color;

/**
 * A model of a wolf. Extends Predator.
 */
public class Wolf extends Predator {

    public Wolf(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        // Initialize gene parameters for Wolf.
        foodLevel = rand.nextInt(15); // base food value of 15.
        gene.BREEDING_AGE = rand.nextInt(16, 22);
        gene.MAX_AGE = rand.nextInt(60, 80);
        age = rand.nextInt(1, gene.MAX_AGE);
        gene.BREEDING_PROBABILITY = rand.nextDouble(0.17, 0.22);
        gene.DISEASE_PROBABILITY = rand.nextDouble(0.05, 0.1);
        gene.MAX_LITTER_SIZE = Math.max(1, rand.nextInt(1, 3));
        gene.METABOLISM = rand.nextDouble(0.25, 1);
        lifeLeft = 13;
        createGeneString();
    }

    @Override
    protected Predator createYoung(Location loc) {
        return new Wolf(false, getField(), loc, getColor());
    }

    // The act() method is inherited from Predator.
}
