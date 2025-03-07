import java.util.List;
import javafx.scene.paint.Color;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

/**
 * A class representing shared characteristics of animals.
 *
 * @author Ulvis Turkers and Yaseen Alam
 * @version 04/03/2025
 */
public abstract class Animal {
    public int GENDER; // 0 for male and 1 for female
    private static final Random rand = Randomizer.getRandom();
    protected boolean disease;
    private boolean alive;
    private Field field;
    private Location location;
    private Color colour = Color.BLACK;
    protected int lifeLeft;
    public static int totalDeaths = 0;
    public static int totalBirths = 0;
    public static int totalDiseaseCatches = 0;
    public static int totalDiseaseSpreads = 0;
    public static Map<String, Integer> deathsBySpecies = new HashMap<>();
    public static Map<String, Integer> birthsBySpecies = new HashMap<>();
    public static Map<String, Integer> diseaseCatchesBySpecies = new HashMap<>();
    public static Map<String, Integer> diseaseSpreadsBySpecies = new HashMap<>();
    public Gene gene;

    /**
     * Creates a new animal at a location in a field.
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     * @param col      The initial colour of the animal.
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
     * Makes this animal act – that is: perform its behaviours as required.
     *
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newAnimals);

    /**
     * Checks the gender of the animal.
     *
     * @return 0 for male or 1 for female.
     */
    public int getGender() {
        return this.GENDER;
    }

    /**
     * Checks whether the animal is alive.
     *
     * @return true if the animal is still alive.
     */
    protected boolean isAlive() {
        return alive;
    }

    /**
     * Indicates that the animal is no longer alive.
     * It is removed from the field and the death is logged.
     */
    protected void setDead() {
        alive = false;
        if (location != null) {
            field.clear(location);
            field.place(new Grass(true, field, location, Color.DARKSEAGREEN), location);
            location = null;
            field = null;
        }

        // Log the death: increment total deaths and update the species death count.
        totalDeaths++;
        String species = this.getClass().getSimpleName();
        deathsBySpecies.put(species, deathsBySpecies.getOrDefault(species, 0) + 1);
    }

    /**
     * Returns the animal's location.
     *
     * @return The current location of the animal.
     */
    protected Location getLocation() {
        return location;
    }

    /**
     * Places the animal at a new location in the given field.
     *
     * @param newLocation The new location for the animal.
     */
    protected void setLocation(Location newLocation) {
        if (location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Returns the animal's field.
     *
     * @return The field in which the animal resides.
     */
    protected Field getField() {
        return field;
    }

    /**
     * Changes the colour of the animal.
     *
     * @param col The new colour being set.
     */
    public void setColor(Color col) {
        colour = col;
    }

    /**
     * Returns the animal's colour.
     *
     * @return The current colour of the animal.
     */
    public Color getColor() {
        return colour;
    }

    /**
     * Abstract method for retrieving the food value of prey animals.
     *
     * @return The food value as an integer.
     */
    public abstract int getFoodValue();

    /**
     * Creates the gene string for the animal.
     */
    public void createGeneString() {
        gene.createGeneString();
    }

    /**
     * Returns the gene string.
     *
     * @return The gene string.
     */
    public String getGeneString() {
        return gene.getGeneString();
    }

    /**
     * Returns the breeding age decoded from the gene string.
     *
     * @return The breeding age as an integer.
     */
    public int getBreedingAgeFromGene() {
        return gene.getBreedingAgeFromGene();
    }

    /**
     * Returns the life span decoded from the gene string.
     *
     * @return The life span as an integer.
     */
    public int getLifeSpanFromGene() {
        return gene.getLifeSpanFromGene();
    }

    /**
     * Returns the breeding probability decoded from the gene string.
     *
     * @return The breeding probability as a double.
     */
    public double getBreedingProbabilityFromGene() {
        return gene.getBreedingProbabilityFromGene();
    }

    /**
     * Returns the litter size decoded from the gene string.
     *
     * @return The litter size as an integer.
     */
    public int getLitterSizeFromGene() {
        return gene.getLitterSizeFromGene();
    }

    /**
     * Returns the disease probability decoded from the gene string.
     *
     * @return The disease probability as a double.
     */
    public double getDiseaseProbabilityFromGene() {
        return gene.getDiseaseProbabilityFromGene();
    }

    /**
     * Returns the metabolism decoded from the gene string.
     *
     * @return The metabolism as a double.
     */
    public double getMetabolismFromGene() {
        return gene.getMetabolismFromGene();
    }

    /**
     * Checks whether this animal is diseased.
     *
     * @return true if the animal is diseased, false otherwise.
     */
    public boolean isDiseased() {
        return disease;
    }

    /**
     * Sets the disease status of this animal.
     *
     * @param disease true if the animal should be marked as diseased.
     */
    public void setDiseased(boolean disease) {
        this.disease = disease;
    }

    /**
     * Spreads disease to adjacent animals of the same species.
     * For each adjacent animal (of the same class) that is not already diseased,
     * it is infected with a probability of 0.05.
     */
    public void diseaseSpread() {
        double prob_of_spread = 0.05;

        if (!isDiseased()) {
            return;
        }
        if (!isAlive()) {
            return;
        }

        List<Location> adjacent = getField().adjacentLocations(getLocation());
        for (Location loc : adjacent) {
            Animal other = getField().getObjectAt(loc);
            if (other != null && other.getClass().equals(this.getClass()) && !other.isDiseased()) {
                if (Randomizer.getRandom().nextDouble() < prob_of_spread) {
                    other.setDiseased(true);
                }
            }
        }
    }

    /**
     * Handles disease: if not diseased, there is a chance to become diseased;
     * if already diseased, reduces lifeLeft and possibly causes death.
     */
    protected void handleDisease() {
        if (!disease) {
            if (rand.nextDouble() < getDiseaseProbabilityFromGene()) {
                disease = true;
            }
        } else {
            lifeLeft--;
            if (lifeLeft <= 0) {
                setDead();
            }
        }
    }
}
