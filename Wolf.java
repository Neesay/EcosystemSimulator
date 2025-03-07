import javafx.scene.paint.Color;

/**
 * A model of a wolf. Extends Predator.
 */
public class Wolf extends Predator {

    public Wolf(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        // Initialize gene parameters for Wolf.
        foodLevel = rand.nextInt(14); // base food value of 15.
        gene.BREEDING_AGE = rand.nextInt(15, 21);
        gene.MAX_AGE = rand.nextInt(40, 50);
        age = rand.nextInt(1, gene.MAX_AGE);
        gene.BREEDING_PROBABILITY = rand.nextDouble(0.195, 0.24);
        gene.DISEASE_PROBABILITY = rand.nextDouble(0.36, 0.41);
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
