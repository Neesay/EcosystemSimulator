import javafx.scene.paint.Color;

/**
 * A model of a squirrel. Extends Prey.
 */
public class Squirrel extends Prey {

    public Squirrel(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        // Initialize gene parameters for Squirrel.
        gene.BREEDING_AGE = rand.nextInt(3, 8);
        gene.MAX_AGE = rand.nextInt(33, 48);
        gene.BREEDING_PROBABILITY = rand.nextDouble(0.06, 0.12);
        gene.DISEASE_PROBABILITY = gene.BREEDING_PROBABILITY - 0.02;
        gene.MAX_LITTER_SIZE = rand.nextInt(3, 6);
        gene.MAX_FOOD_VALUE = rand.nextInt(8, 11);
        gene.METABOLISM = rand.nextDouble(0.25, 1.0);

        // Initialize instance fields.
        age = rand.nextInt(gene.MAX_AGE);
        foodLevel = rand.nextInt(gene.MAX_FOOD_VALUE);
        lifeLeft = 4;
        disease = false;
        createGeneString();
    }

    @Override
    protected Prey createYoung(Location loc) {
        return new Squirrel(false, getField(), loc, getColor());
    }
}
