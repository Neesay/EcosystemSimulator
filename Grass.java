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
    private static final int MAX_AGEING = 500;
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
    
    /**
     * Returns the current age of this grass (for debugging).
     */
    public int getAge() {
        return age;
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
        // Using Math.min so that food value increases with age up to MAX_AGEING.
        int value = min(age, MAX_AGEING);
        return value;
    }
}
