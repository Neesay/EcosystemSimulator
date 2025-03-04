import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;

public class Deer extends Animal {
    private int age;
    private boolean disease = false;
    private int life_left = 8;
    private int foodLevel;
    private static final Random rand = Randomizer.getRandom();

    public Deer(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col);
            gene.BREEDING_AGE = rand.nextInt(7, 14);
            gene.MAX_AGE = rand.nextInt(65, 96);
            gene.BREEDING_PROBABILITY = rand.nextDouble(0.07, 0.14);
            gene.DISEASE_PROBABILITY = gene.BREEDING_PROBABILITY - 0.02;
            gene.MAX_LITTER_SIZE = Math.max(1, rand.nextInt(1, 3));
            gene.MAX_FOOD_VALUE = rand.nextInt(15, 22);
            gene.METABOLISM = rand.nextDouble(0.25, 1.0);

            age = rand.nextInt(gene.MAX_AGE);
            foodLevel = rand.nextInt(gene.MAX_FOOD_VALUE);
            createGeneString();
    }

    public void act(List<Animal> newDeer) {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newDeer);
            Location newLocation = getField().getFreeAdjacentLocation(getLocation());
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

    private void giveBirth(List<Animal> newDeer) {
        if (getGender() == 1) {
            List<Location> free = getField().getFreeAdjacentLocations(getLocation());
            int births = breed();
            for (int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                Deer young = new Deer(false, getField(), loc, getColor());
                young.gene = new Gene(this, getField().findParent(getLocation(), getGender()));
                newDeer.add(young);
            }
        }
    }

    private int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= gene.BREEDING_PROBABILITY) {
            births = rand.nextInt(gene.MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    private boolean canBreed() {
        return age >= gene.BREEDING_AGE && getField().findOppositeGenderAnimal(getLocation(), getGender());
    }

    public int getFoodValue() {
        return gene.MAX_FOOD_VALUE;
    }
}
