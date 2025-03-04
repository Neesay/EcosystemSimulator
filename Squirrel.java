import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;

/**
 * A simple model of a squirrel.
 * Squirrels age, move, breed, and die.
 */

public class Squirrel extends Animal {
    private int age;
    private boolean disease = false;
    private int life_left = 4;
    private int foodLevel;
    private static final Random rand = Randomizer.getRandom();

    public Squirrel(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
        if(randomAge) {
            gene.BREEDING_AGE = rand.nextInt(3, 8);
            gene.MAX_AGE = rand.nextInt(33, 48);
            gene.BREEDING_PROBABILITY = rand.nextDouble(0.06, 0.12);
            gene.DISEASE_PROBABILITY = gene.BREEDING_PROBABILITY - 0.02;
            gene.MAX_LITTER_SIZE = rand.nextInt(3, 6);
            gene.MAX_FOOD_VALUE = rand.nextInt(8, 11);
            gene.METABOLISM = rand.nextDouble(0.25, 1.0);

            age = rand.nextInt(gene.MAX_AGE);
            foodLevel = rand.nextInt(gene.MAX_FOOD_VALUE);
            createGeneString();
        }
    }

    public Squirrel(boolean randomAge, Field field, Location location, Color col, Squirrel parent) {
        super(field, location, col);
        age = 0;
        foodLevel = gene.MAX_FOOD_VALUE;

        gene.BREEDING_AGE = Math.min(Math.max(parent.getBreedingAgeFromGene() + rand.nextInt(-2, 3), 12), 90);
        gene.MAX_AGE = Math.min(Math.max(parent.getLifeSpanFromGene() + rand.nextInt(-7, 8), 10), 120);
        gene.BREEDING_PROBABILITY = Math.min(Math.max(parent.getBreedingProbabilityFromGene() + rand.nextDouble(-0.02, 0.02), 0), 0.50);
        gene.DISEASE_PROBABILITY = Math.min(Math.max(gene.BREEDING_PROBABILITY - 0.02, 0), 0.5);
        gene.MAX_LITTER_SIZE = Math.min(Math.max(parent.getLitterSizeFromGene() + rand.nextInt(-1, 2), 1), 12);
        gene.METABOLISM = Math.min(Math.max(parent.getMetabolismFromGene() + rand.nextDouble(-0.1, 0.1), 0.25), 1.0);

        createGeneString();
    }

    public void act(List<Animal> newSquirrels) {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newSquirrels);
            Location newLocation = getField().getFreeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                setDead();
            }
            if (!disease){
                double chance = rand.nextDouble();
                if (chance < gene.DISEASE_PROBABILITY) {
                    disease = true;
                }
            } else{
                life_left--;
                if (life_left <= 0){
                    setDead();
                }
            }
        }
    }

    private void incrementAge() {
        age++;
        if(age > gene.MAX_AGE) {
            setDead();
        }
    }

    private void incrementHunger() {
        foodLevel -= 1 + gene.METABOLISM;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    private void giveBirth(List<Animal> newSquirrels) {
        List<Location> free = getField().getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            
            Location loc = free.remove(0);
            Squirrel young = new Squirrel(false, getField(), loc, getColor(), this);
            newSquirrels.add(young);
        }
    }

    private int breed() {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= gene.BREEDING_PROBABILITY) {
            births = rand.nextInt(gene.MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    private boolean canBreed() {
        return age >= gene.BREEDING_AGE && getField().findOppositeGenderAnimal(getLocation(), getGender()) && GENDER == 1;
    }

    public int getFoodValue() {
        return gene.MAX_FOOD_VALUE;
    }
}
