import javafx.scene.paint.Color;

/**
 * A model of a wolf. Extends Predator.
 */
public class Wolf extends Predator {

    public Wolf(Field field, Location location, Color col) {
        super(field, location, col);
        foodLevel = rand.nextInt(14); // base food value of 15.
        gene.BREEDING_AGE = rand.nextInt(15, 21);
        gene.MAX_AGE = rand.nextInt(40, 50);
        age = rand.nextInt(1, gene.MAX_AGE);
        gene.BREEDING_PROBABILITY = rand.nextDouble(0.21, 0.25);
        gene.DISEASE_PROBABILITY = rand.nextDouble(0.35, 0.4);
        gene.MAX_LITTER_SIZE = Math.max(1, rand.nextInt(1, 3));
        gene.METABOLISM = rand.nextDouble(0.25, 1);
        lifeLeft = 13;
        createGeneString();
    }

    @Override
    protected Predator createOffspring(Location loc) {
        return new Wolf(getField(), loc, getColor());
    }
}
