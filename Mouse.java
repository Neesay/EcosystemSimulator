import javafx.scene.paint.Color;

/**
 * A model of a mouse. This class extends Prey and initializes a Mouse with randomly generated
 * genetic parameters (breeding age, max age, breeding probability, disease probability, litter size,
 * max food value, and metabolism) without clamping. It also initializes state variables
 * (age, food level, lifeLeft, and disease status) and creates a gene string representing its genetic makeup.
 */
public class Mouse extends Prey {

    /**
     * Constructs a new Mouse with random genetic parameters.
     * @param field the simulation field where the mouse lives
     * @param location the starting location of the mouse
     * @param col the color representing the mouse
     */
    public Mouse(Field field, Location location, Color col) {
        super(field, location, col);
        // Set genetic parameters using random values.
        gene.BREEDING_AGE = rand.nextInt(2, 5);
        gene.MAX_AGE = rand.nextInt(18, 28);
        gene.BREEDING_PROBABILITY = rand.nextDouble(0.42, 0.47);
        gene.DISEASE_PROBABILITY = rand.nextDouble(0.05, 0.1);
        gene.MAX_LITTER_SIZE = rand.nextInt(10, 19);
        gene.MAX_FOOD_VALUE = rand.nextInt(5, 7);
        gene.METABOLISM = rand.nextDouble(0.25, 0.5);
        
        age = rand.nextInt(1, gene.MAX_AGE);
        foodLevel = rand.nextInt(gene.MAX_FOOD_VALUE);
        lifeLeft = 10;
        disease = false;
        createGeneString();
    }

    /**
     * Creates and returns a new Mouse offspring at the specified location.
     * @param loc the location where the offspring will be placed
     * @return a new Mouse instance with similar genetic characteristics
     */
    @Override
    protected Prey createOffspring(Location loc) {
        return new Mouse(getField(), loc, getColor());
    }
}
