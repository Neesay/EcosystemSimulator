import java.util.List;
import java.util.Iterator;
import java.util.Random;
import javafx.scene.paint.Color;

/**
 * A simple model of a wolf.
 * Wolves age, move, hunt rabbits, breed slowly, and eventually die.
 */

public class Wolf extends Animal {

    private static final int BASE_FOOD_VALUE = 15;
    private static final Random rand = Randomizer.getRandom();

    private int age;
    private int foodLevel;
    private boolean disease = false;
    private int life_left = 13;

    public Wolf(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
            foodLevel = rand.nextInt(BASE_FOOD_VALUE);

            gene.BREEDING_AGE = rand.nextInt(17,23);
            gene.MAX_AGE = rand.nextInt(40,80);
            age = rand.nextInt(1, gene.MAX_AGE);
            gene.BREEDING_PROBABILITY = rand.nextDouble(0.03,0.09);
            gene.DISEASE_PROBABILITY = gene.BREEDING_PROBABILITY - 0.02;
            gene.MAX_LITTER_SIZE = rand.nextInt(1,3);
            gene.METABOLISM = rand.nextDouble(0.25, 1);
            createGeneString();
    }


    public void act(List<Animal> newWolves) {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newWolves);
            Location newLocation = findFood();
            if(newLocation == null) {
                newLocation = getField().getFreeAdjacentLocation(getLocation());
            }
            if(newLocation != null) {
                setLocation(newLocation);
            } else {
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

    private Location findFood() {
        List<Location> adjacent = getField().adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            Object animal = getField().getObjectAt(where);
            if (animal instanceof Squirrel || animal instanceof Mice || animal instanceof Deer) {
                Animal prey = (Animal) animal;
                if (prey.isAlive()) {
                    prey.setDead();
                    foodLevel += prey.getFoodValue();
                    return where;
                }
            }
        }
        return null;
    }

    private void giveBirth(List<Animal> newWolves) {
        if (getGender() == 1) {
            List<Location> free = getField().getFreeAdjacentLocations(getLocation());
            int births = breed();
            for(int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                Wolf young = new Wolf(false, getField(), loc, getColor());
                Animal mate = getField().findParent(getLocation(), getGender());
                if (mate == null) {
                    // Fallback: use the same animal as mate if no valid mate found.
                    mate = this;
                }
                young.gene = new Gene(this, mate);
                newWolves.add(young);
            }
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
        return age >= gene.BREEDING_AGE && getField().findOppositeGenderAnimal(getLocation(), getGender());
    }

    @Override
    public int getFoodValue() {
        return 0;
    }
}
