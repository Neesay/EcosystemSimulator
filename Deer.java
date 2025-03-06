import javafx.scene.paint.Color;

/**
 * A model of a deer. Extends Prey.
 */
public class Deer extends Prey {

    public Deer(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        // Initialize gene parameters for Deer.
        gene.BREEDING_AGE = rand.nextInt(9, 16);
        gene.MAX_AGE = rand.nextInt(65, 85);
        gene.BREEDING_PROBABILITY = rand.nextDouble(0.27, 0.32);
        gene.DISEASE_PROBABILITY = rand.nextDouble(0.05, 0.1);
        gene.MAX_LITTER_SIZE = Math.max(1, rand.nextInt(1, 3));
        gene.MAX_FOOD_VALUE = rand.nextInt(15, 22);
        // Increase metabolism so deer lose food faster.
        gene.METABOLISM = rand.nextDouble(0.25, 1.0);

        age = rand.nextInt(gene.MAX_AGE);
        foodLevel = rand.nextInt(gene.MAX_FOOD_VALUE);
        lifeLeft = 6;
        disease = false;
        createGeneString();
    }

    @Override
    protected Prey createYoung(Location loc) {
        return new Deer(false, getField(), loc, getColor());
    }
}
