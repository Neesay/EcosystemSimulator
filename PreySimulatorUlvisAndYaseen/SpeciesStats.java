public class SpeciesStats {
    private String species;
    private int count;
    private double avgBreedingAge;
    private double avgLifeSpan;
    private double diseaseFrequency;

    public SpeciesStats(String species, int count, double avgBreedingAge, double avgLifeSpan, double diseaseFrequency) {
        this.species = species;
        this.count = count;
        this.avgBreedingAge = avgBreedingAge;
        this.avgLifeSpan = avgLifeSpan;
        this.diseaseFrequency = diseaseFrequency;
    }

    public String getSpecies() {
        return species;
    }

    public int getCount() {
        return count;
    }

    public double getAvgBreedingAge() {
        return avgBreedingAge;
    }

    public double getAvgLifeSpan() {
        return avgLifeSpan;
    }

    public double getDiseaseFrequency() {
        return diseaseFrequency;
    }
}
