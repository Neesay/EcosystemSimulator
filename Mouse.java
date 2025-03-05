import javafx.scene.paint.Color;

/**
 * A model of a mice. Extends Prey.
 */
public class Mouse extends Prey {

    public Mouse(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        // Initialize gene parameters for Mice.
        gene.BREEDING_AGE = rand.nextInt(2, 5);
        gene.MAX_AGE = rand.nextInt(4, 38);
        gene.BREEDING_PROBABILITY = rand.nextDouble(0.48, 0.5);
        gene.DISEASE_PROBABILITY = 0;//rand.nextDouble(0.05, 0.1);
        gene.MAX_LITTER_SIZE = rand.nextInt(7, 11);
        gene.MAX_FOOD_VALUE = rand.nextInt(9, 15);
        gene.METABOLISM = rand.nextDouble(0.25, 1.0);

        age = rand.nextInt(gene.MAX_AGE);
        foodLevel = 14;//rand.nextInt(gene.MAX_FOOD_VALUE);
        lifeLeft = 10;
        disease = false;
        createGeneString();
    }

    @Override
    protected Prey createYoung(Location loc) {
        return new Mouse(false, getField(), loc, getColor());
    }
}
