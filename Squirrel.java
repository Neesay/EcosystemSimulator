import javafx.scene.paint.Color;

/**
 * A model of a squirrel. Extends Prey.
 */
public class Squirrel extends Prey {

    public Squirrel(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        // Initialize gene parameters for Squirrel.
        gene.BREEDING_AGE = rand.nextInt(3, 8);
        gene.MAX_AGE = rand.nextInt(6, 48);
        gene.BREEDING_PROBABILITY = rand.nextDouble(0.285, 0.335);
        gene.DISEASE_PROBABILITY = rand.nextDouble(0.05, 0.1);
        gene.MAX_LITTER_SIZE = Math.max(1, rand.nextInt(3, 6));
        gene.MAX_FOOD_VALUE = rand.nextInt(12, 16); 
        gene.METABOLISM = rand.nextDouble(0.1, 0.3);  

        age = rand.nextInt(gene.MAX_AGE);
        foodLevel = rand.nextInt(gene.MAX_FOOD_VALUE);
        lifeLeft = 10; // increased survival buffer
        disease = false;
        createGeneString();
    }

    @Override
    protected Prey createYoung(Location loc) {
        return new Squirrel(false, getField(), loc, getColor());
    }
}
