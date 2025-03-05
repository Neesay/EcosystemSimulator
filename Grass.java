import java.util.*;
import javafx.scene.paint.Color; 
import static java.lang.Math.min;
/**
 * A simple model of grass.
 * Grass age, and die.
 * 
 * @author Yaseen A.
 * @version 26.02.2025
 */

public class Grass extends Animal {
    private static final int MAX_AGEING = 3;
    private static final int MAX_LITTER_SIZE = 3;
    private static final Color color = Color.DARKSEAGREEN;
    private static final Random rand = Randomizer.getRandom();
    private int age;  // Now an instance variable.

    public Grass(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGEING + 1);
        }
    }

    public void act(List<Animal> newGrass) {
        incrementAge();
        // You could implement reproduction if desired.
    }

    private void incrementAge() {
        age++;
    }

    @Override
    public int getFoodValue() {
        return Math.min(age, MAX_AGEING); // Now based on the instance's age.
    }
}
