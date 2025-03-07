import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.scene.paint.Color; 

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing animals.
 * 
 * @author David J. Barnes, Michael Kölling and Jeffery Raphael
 * @version 2025.02.10
 */

public class Simulator {
    private static final double COYOTE_CREATION_PROBABILITY = 0.02;
    private static final double SQUIRREL_CREATION_PROBABILITY = 0.08;
    private static final double WOLF_CREATION_PROBABILITY = 0.012;
    private static final double MOUSE_CREATION_PROBABILITY = 0.15;
    private static final double DEER_CREATION_PROBABILITY = 0.06;

    private final List<Animal> animals;
    private final Field field;
    private int step;
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        animals = new ArrayList<>();
        field = new Field(depth, width);

        reset();
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * animal.
     */
    public void simulateOneStep() {
        step++;
        List<Animal> newAnimals = new ArrayList<>();        
        
        // Iterate over a copy to avoid concurrent modification errors.
        for (Animal animal : new ArrayList<>(animals)) {
            animal.act(newAnimals);
            if (!animal.isAlive()) {
                animals.remove(animal);
            }
        }
               
        animals.addAll(newAnimals);
    }

        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        animals.clear();
        populate();
    }
    
    /**
     * Randomly populate the field with animals.
     */
    private void populate() {
        
        Random rand = Randomizer.getRandom();
        field.clear();
        
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= COYOTE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Coyote coyote = new Coyote(field, location, Color.DARKSALMON);
                    animals.add(coyote);
                } else if(rand.nextDouble() <= SQUIRREL_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Squirrel squirrel = new Squirrel(field, location, Color.BURLYWOOD);
                    animals.add(squirrel);
                } else if(rand.nextDouble() <= WOLF_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Wolf wolf = new Wolf(field, location, Color.BLACK);
                    animals.add(wolf);
                } else if(rand.nextDouble() <= MOUSE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Mouse mouse = new Mouse(field, location, Color.GREY);
                    animals.add(mouse);
                } else if(rand.nextDouble() <= DEER_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Deer deer = new Deer(field, location, Color.RED);
                    animals.add(deer);
                } else {
                    Location location = new Location(row, col);
                    Grass grass = new Grass(true, field, location, Color.DARKSEAGREEN);
                    animals.add(grass);
                }
            }
        }
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    public void delay(int millisec) {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
    

    public Field getField() {
        return field;
    }

    public int getStep() {
        return step;
    }
}