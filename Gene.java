/**
 * Representing genes of animals with methods for different
 * situations.
 *
 * @author Yaseen Alam and Ulvis Turkers
 * @version 03/03/2025
 */
public class Gene
{
    public int BREEDING_AGE;
    public int MAX_AGE;
    public double BREEDING_PROBABILITY;
    public double DISEASE_PROBABILITY;
    public int MAX_LITTER_SIZE;
    public int MAX_FOOD_VALUE;
    public double METABOLISM;

    private String gene_string;

    public Gene() {
    }

    public void createGeneString() {
        // 1) Breeding Age: 2 digits
        String breedingAgeStr = String.format("%02d", BREEDING_AGE);

        // 2) Life Span (MAX_AGE): 3 digits
        String lifeSpanStr = String.format("%03d", MAX_AGE);

        // 3) Breeding Probability: 2 digits (multiply by 100)
        String breedingProbStr = String.format("%02d", (int) (BREEDING_PROBABILITY * 100));

        // 4) Litter Size: 2 digits
        String litterSizeStr = String.format("%02d", MAX_LITTER_SIZE);

        // 5) Disease Probability: 2 digits (multiply by 100)
        String diseaseProbStr = String.format("%02d", (int) (DISEASE_PROBABILITY * 100));

        // 6) Metabolism: 3 digits (multiply by 100)
        String metabolismStr = String.format("%03d", (int) (METABOLISM * 100));

        // Concatenate into a single 14-digit string.
        gene_string = breedingAgeStr
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
        // We know gene_string is already 14 digits, so just parse it:
        return Integer.parseInt(gene_string.substring(0, 2));
    }

    public int getLifeSpanFromGene() {
        return Integer.parseInt(gene_string.substring(2, 5));
    }

    public double getBreedingProbabilityFromGene() {
        int probValue = Integer.parseInt(gene_string.substring(5, 7));
        return probValue / 100.0;
    }

    public int getLitterSizeFromGene() {
        return Integer.parseInt(gene_string.substring(7, 9));
    }

    public double getDiseaseProbabilityFromGene() {
        int diseaseValue = Integer.parseInt(gene_string.substring(9, 11));
        return diseaseValue / 100.0;
    }

    public double getMetabolismFromGene() {
        int metabolismValue = Integer.parseInt(gene_string.substring(11, 14));
        return metabolismValue / 100.0;
    }
}
