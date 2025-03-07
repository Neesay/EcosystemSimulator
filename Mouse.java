import javafx.scene.paint.Color;

/**
 * A model of a mouse. Extends Prey.
 */
public class Mouse extends Prey {

    public Mouse(Field field, Location location, Color col) {
        super(field, location, col);
        gene.BREEDING_AGE = Gene.clampInt(rand.nextInt(2, 5), 12, 90);
        gene.MAX_AGE = Gene.clampInt(rand.nextInt(18, 28), 10, 120);
        gene.BREEDING_PROBABILITY = Gene.clampDouble(rand.nextDouble(0.341, 0.432), 0.0, 0.50);
        gene.DISEASE_PROBABILITY = Gene.clampDouble(rand.nextDouble(0.05, 0.1), 0.0, 0.50);
        gene.MAX_LITTER_SIZE = Gene.clampInt(rand.nextInt(10, 15), 1, 12);
        gene.MAX_FOOD_VALUE = Math.max(1, rand.nextInt(5, 7));
        gene.METABOLISM = Gene.clampDouble(rand.nextDouble(0.25, 0.5), 0.25, 1.0);
        age = rand.nextInt(gene.MAX_AGE);
        foodLevel = rand.nextInt(gene.MAX_FOOD_VALUE);
        lifeLeft = 10;
        disease = false;
        createGeneString();
    }

    @Override
    protected Prey createOffspring(Location loc) {
        return new Mouse(getField(), loc, getColor());
    }
}

