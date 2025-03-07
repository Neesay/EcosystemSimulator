import javafx.scene.paint.Color;

/**
 * A model of a coyote. Extends Predator.
 */
public class Coyote extends Predator {

    public Coyote(Field field, Location location, Color col) {
        super(field, location, col);
        foodLevel = rand.nextInt(8);
        gene.BREEDING_AGE = Gene.clampInt(rand.nextInt(10, 17), 12, 90);
        gene.MAX_AGE = Gene.clampInt(rand.nextInt(30, 40), 10, 120);
        gene.BREEDING_PROBABILITY = Gene.clampDouble(rand.nextDouble(0.31, 0.38), 0.0, 0.50);
        gene.DISEASE_PROBABILITY = Gene.clampDouble(rand.nextDouble(0.31, 0.36), 0.0, 0.50);
        gene.MAX_LITTER_SIZE = Gene.clampInt(rand.nextInt(1, 4), 1, 12);
        gene.METABOLISM = Gene.clampDouble(rand.nextDouble(0.25, 1), 0.25, 1.0);
        age = rand.nextInt(1, gene.MAX_AGE);
        lifeLeft = 12;
        createGeneString();
    }

    @Override
    protected Predator createOffspring(Location loc) {
        return new Coyote(getField(), loc, getColor());
    }
}

