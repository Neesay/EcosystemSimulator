import javafx.scene.paint.Color;

/**
 * A model of a deer. Extends Prey.
 */
public class Deer extends Prey {

    public Deer(Field field, Location location, Color col) {
        super(field, location, col);
        gene.BREEDING_AGE = Gene.clampInt(rand.nextInt(9, 16), 12, 90);
        gene.MAX_AGE = Gene.clampInt(rand.nextInt(45, 65), 10, 120);
        gene.BREEDING_PROBABILITY = Gene.clampDouble(rand.nextDouble(0.140, 0.321), 0.0, 0.50);
        gene.DISEASE_PROBABILITY = Gene.clampDouble(rand.nextDouble(0.15, 0.36), 0.0, 0.50);
        gene.MAX_LITTER_SIZE = Gene.clampInt(rand.nextInt(1, 3), 1, 12);
        gene.MAX_FOOD_VALUE = rand.nextInt(15, 22);
        gene.METABOLISM = Gene.clampDouble(rand.nextDouble(0.25, 1.0), 0.25, 1.0);
        age = rand.nextInt(gene.MAX_AGE);
        foodLevel = rand.nextInt(gene.MAX_FOOD_VALUE);
        lifeLeft = 6;
        disease = false;
        createGeneString();
    }

    @Override
    protected Prey createOffspring(Location loc) {
        return new Deer(getField(), loc, getColor());
    }
}

