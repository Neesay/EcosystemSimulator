import javafx.scene.paint.Color;

/**
 * A model of a mouse. Extends Prey.
 */
public class Mouse extends Prey {

    public Mouse(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        // Initialize gene parameters for Mouse.
        gene.BREEDING_AGE = rand.nextInt(2, 5);
        gene.MAX_AGE = rand.nextInt(4, 38);
        gene.BREEDING_PROBABILITY = rand.nextDouble(0.3, 0.38);
        gene.DISEASE_PROBABILITY = rand.nextDouble(0.05, 0.1);
        gene.MAX_LITTER_SIZE = rand.nextInt(7, 11);
        // Lower metabolism so mice lose food more slowly.
        gene.MAX_FOOD_VALUE = Math.max(1, rand.nextInt(9, 15));
        gene.METABOLISM = rand.nextDouble(0.25, 0.5);

        age = rand.nextInt(gene.MAX_AGE);
        foodLevel = rand.nextInt(gene.MAX_FOOD_VALUE);
        lifeLeft = 10;
        disease = false;
        createGeneString();
    }

    @Override
    protected Prey createYoung(Location loc) {
        return new Mouse(false, getField(), loc, getColor());
    }
}
