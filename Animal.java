import java.util.List;
import javafx.scene.paint.Color; 

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes, Michael Kölling and Jeffery Raphael
 * @version 2025.02.10
 */

public abstract class Animal {
    public int BREEDING_AGE;
    public int MAX_AGE; 
    public double BREEDING_PROBABILITY; 
    public double DISEASE_PROBABILITY;
    public int MAX_LITTER_SIZE;
    public int MAX_FOOD_VALUE;
    public double METABOLISM;
    
    private boolean alive;
    private Field field;
    private Location location;
    private Color color = Color.BLACK;
    
    private static int BREEDING_AGE;
    private static int MAX_AGE;
    private static double BREEDING_PROBABILITY;
    private static double DISEASE_PROBABILITY;
    private static int MAX_LITTER_SIZE;
    private static double METABOLISM;
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    
    public Animal(Field field, Location location, Color col) {
        alive = true;
        this.field = field;
        setLocation(location);
        setColor(col);
    }
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newAnimals);

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive() {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead() {
        alive = false;
        if(location != null) {
            field.clear(location);
            field.place(new Grass(true, field, location, Color.DARKSEAGREEN), location);
            
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation() {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation) {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField() {
        return field;
    }
    
    /**
     * Changes the color of the animal
     */
    public void setColor(Color col) {
        color = col;
    }

    /**
     * Returns the animal's color
     */
    public Color getColor() {
        return color;
    }   
    
    /**
     * Abstract method for retrievign the food value of prey anuimals.
     */
    public abstract int getFoodValue();
}