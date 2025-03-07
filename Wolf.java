import javafx.scene.paint.Color;

/**
 * A model of a wolf. Extends Predator.
 */
public class Wolf extends Predator {

    public Wolf(Field field, Location location, Color col) {
        super(field, location, col);
        foodLevel = rand.nextInt(14);
        gene.BREEDING_AGE = Gene.clampInt(rand.nextInt(15, 21), 12, 90);
        gene.MAX_AGE = Gene.clampInt(rand.nextInt(40, 60), 10, 120);
        age = rand.nextInt(1, gene.MAX_AGE);
        gene.BREEDING_PROBABILITY = Gene.clampDouble(rand.nextDouble(0.21, 0.29), 0.0, 0.50);
        gene.DISEASE_PROBABILITY = Gene.clampDouble(rand.nextDouble(0.35, 0.4), 0.0, 0.50);
        gene.MAX_LITTER_SIZE = Gene.clampInt(rand.nextInt(1, 3), 1, 12);
        gene.METABOLISM = Gene.clampDouble(rand.nextDouble(0.25, 1), 0.25, 1.0);
        lifeLeft = 13;
        createGeneString();
    }

    @Override
    protected Predator createOffspring(Location loc) {
        return new Wolf(getField(), loc, getColor());
    }
}

