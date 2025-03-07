import javafx.scene.paint.Color;

/**
 * A model of a coyote. Extends Predator.
 */
public class Coyote extends Predator {

    public Coyote(Field field, Location location, Color col) {
        super(field, location, col);
        foodLevel = rand.nextInt(8);
        gene.BREEDING_AGE = rand.nextInt(10, 17);
        gene.MAX_AGE = rand.nextInt(30, 40);
        gene.BREEDING_PROBABILITY = rand.nextDouble(0.31, 0.38);
        gene.DISEASE_PROBABILITY = rand.nextDouble(0.31, 0.36);
        gene.MAX_LITTER_SIZE = Math.max(1, rand.nextInt(1, 4));
        gene.METABOLISM = rand.nextDouble(0.25, 1);
        age = rand.nextInt(1, gene.MAX_AGE);
        lifeLeft = 12;
        createGeneString();
    }

    @Override
    protected Predator createOffspring(Location loc) {
        return new Coyote(getField(), loc, getColor());
    }
}
