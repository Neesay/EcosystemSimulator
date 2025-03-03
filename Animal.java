import java.util.List;
import javafx.scene.paint.Color; 

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes, Michael Kölling and Jeffery Raphael
 * @version 2025.02.10
 */

public abstract class Animal {
    public int gender; // 0 for male and 1 for female
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
    private String gene_string;
    
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
    
    public void createGeneString() {
        // 1) Breeding Age: 2 digits
        String breedingAgeStr = String.format("%02d", BREEDING_AGE);
        
        // 2) Life Span (MAX_AGE): 3 digits (e.g. 86 → "086", 120 → "120")
        String lifeSpanStr = String.format("%03d", MAX_AGE);
        
        // 3) Breeding Probability: 2 digits (multiply by 100, e.g. 0.35 → 35 → "35")
        String breedingProbStr = String.format("%02d", (int) (BREEDING_PROBABILITY * 100));
        
        // 4) Litter Size: 2 digits
        String litterSizeStr = String.format("%02d", MAX_LITTER_SIZE);
        
        // 5) Disease Probability: 2 digits (multiply by 100, e.g. 0.12 → 12 → "12")
        String diseaseProbStr = String.format("%02d", (int) (DISEASE_PROBABILITY * 100));
        
        // 6) Metabolism: 3 digits (multiply by 100, e.g. 0.80 → 80 → "080")
        // If METABOLISM can be 1.0, that becomes 100 → "100".
        String metabolismStr = String.format("%03d", (int) (METABOLISM * 100));
        
        // Concatenate into a single 14-digit string.
        String gene_string = breedingAgeStr
                    + lifeSpanStr
                    + breedingProbStr
                    + litterSizeStr
                    + diseaseProbStr
                    + metabolismStr;
    }
    
    public String getGeneString(){
        return gene_string;
    }
    
    public int getBreedingAgeFromGene() {
        // Ensure a 14-character string with leading zeros if necessary.
        String gene = String.format("%014d", gene_string);
        return Integer.parseInt(gene.substring(0, 2));
    }

    /**
     * Return the life span (integer) by decoding digits [2..5).
     */
    public int getLifeSpanFromGene() {
        String gene = String.format("%014d", gene_string);
        return Integer.parseInt(gene.substring(2, 5));
    }

    /**
     * Return the breeding probability (double) by decoding digits [5..7)
     * and dividing by 100.0.
     */
    public double getBreedingProbabilityFromGene() {
        String gene = String.format("%014d", gene_string);
        int probValue = Integer.parseInt(gene.substring(5, 7));
        return probValue / 100.0;
    }

    /**
     * Return the litter size (integer) by decoding digits [7..9).
     */
    public int getLitterSizeFromGene() {
        String gene = String.format("%014d", gene_string);
        return Integer.parseInt(gene.substring(7, 9));
    }

    /**
     * Return the disease probability (double) by decoding digits [9..11)
     * and dividing by 100.0.
     */
    public double getDiseaseProbabilityFromGene() {
        String gene = String.format("%014d", gene_string);
        int diseaseValue = Integer.parseInt(gene.substring(9, 11));
        return diseaseValue / 100.0;
    }

    /**
     * Return the metabolism (double) by decoding digits [11..14)
     * and dividing by 100.0.
     */
    public double getMetabolismFromGene() {
        String gene = String.format("%014d", gene_string);
        int metabolismValue = Integer.parseInt(gene.substring(11, 14));
        return metabolismValue / 100.0;
    }
}