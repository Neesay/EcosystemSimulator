    import java.util.List;
import javafx.scene.paint.Color;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 *
 * @author Ulvis Turkers and Yaseen Alam
 * @version ...
 */

public abstract class Animal {
    public int GENDER; // 0 for male and 1 for female
    private static final Random rand = Randomizer.getRandom();

    private boolean alive;
    private Field field;
    private Location location;
    private Color color = Color.BLACK;

    // The gene object now holds breeding/age/metabolism fields.
    public Gene gene;

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
        this.gene = new Gene();
        this.GENDER = rand.nextInt(2);
    }

    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newAnimals);

    /**
     * Check gender of animal.
     * @return 0 for male or 1 for female.
     */
    public int getGender() {
        return this.GENDER;
    }

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

    /**
     * Create the gene string.
     */
    public void createGeneString() {
        gene.createGeneString();
    }

    /**
     * Return the gene string.
     */
    public String getGeneString(){
        return gene.getGeneString();
    }

    /**
     * Return the breeding age (integer) by decoding from the gene string.
     */
    public int getBreedingAgeFromGene() {
        return gene.getBreedingAgeFromGene();
    }

    /**
     * Return the life span (integer) by decoding from the gene string.
     */
    public int getLifeSpanFromGene() {
        return gene.getLifeSpanFromGene();
    }

    /**
     * Return the breeding probability (double) by decoding from the gene string.
     */
    public double getBreedingProbabilityFromGene() {
        return gene.getBreedingProbabilityFromGene();
    }

    /**
     * Return the litter size (integer) by decoding from the gene string.
     */
    public int getLitterSizeFromGene() {
        return gene.getLitterSizeFromGene();
    }

    /**
     * Return the disease probability (double) by decoding from the gene string.
     */
    public double getDiseaseProbabilityFromGene() {
        return gene.getDiseaseProbabilityFromGene();
    }

    /**
     * Return the metabolism (double) by decoding from the gene string.
     */
    public double getMetabolismFromGene() {
        return gene.getMetabolismFromGene();
    }
}
