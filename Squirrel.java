import javafx.scene.paint.Color;

/**
 * A model of a squirrel. Extends Prey.
 */
public class Squirrel extends Prey {

    public Squirrel(Field field, Location location, Color col) {
        super(field, location, col);
        gene.BREEDING_AGE = Gene.clampInt(rand.nextInt(3, 8), 12, 90);
        gene.MAX_AGE = Gene.clampInt(rand.nextInt(6, 25), 10, 120);
        gene.BREEDING_PROBABILITY = Gene.clampDouble(rand.nextDouble(0.250, 0.296), 0.0, 0.50);
        gene.DISEASE_PROBABILITY = Gene.clampDouble(rand.nextDouble(0.05, 0.1), 0.0, 0.50);
        gene.MAX_LITTER_SIZE = Gene.clampInt(rand.nextInt(3, 6), 1, 12);
        gene.MAX_FOOD_VALUE = rand.nextInt(12, 16);
        gene.METABOLISM = Gene.clampDouble(rand.nextDouble(0.1, 0.3), 0.25, 1.0);
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
}

