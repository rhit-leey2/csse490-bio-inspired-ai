import java.util.ArrayList;

public class Generation {

    public Integer population;
    public Integer generationNum;
    public Integer chromosomeLength;
    public ArrayList<Chromosome> allChromosomes = new ArrayList<Chromosome>();

    // Constructor
    public Generation(Integer chromosomeLength, Integer population) {
        this.chromosomeLength = chromosomeLength;
        this.population = population;
    }

    // Constructor
    public Generation(Integer generationNum, Integer population, Integer chromosomeLength) {
        this.generationNum = generationNum;
        this.population = population;
        this.chromosomeLength = chromosomeLength;

        for (int i = 0; i < population; i++) {
            this.allChromosomes.add(new Chromosome(chromosomeLength));
        }
    }

    // // getHighestFitness method
    // public Integer getHighestFitness() {
    // Integer highestScore = 0;

    // for (Chromosome c : this.allChromosomes) {
    // if (c.evaluateFitness() > highestScore) {
    // highestScore = c.evaluateFitness();
    // } // end if statement
    // } // end for loop
    // return highestScore;
    // } // end method

    // // getLowestFitness method
    // public Integer getLowestFitness() {
    // Integer lowestScore = 100;

    // for (Chromosome c : this.allChromosomes) {
    // if (c.evaluateFitness() < lowestScore) {
    // lowestScore = c.evaluateFitness();
    // } // end if statement
    // } // end for loop
    // return lowestScore;
    // } // end method

    // // getAverageFitness method
    // public Integer getAverageFitness() {
    // Integer scoreSum = 0;

    // for (Chromosome c : this.allChromosomes) {
    // scoreSum += c.evaluateFitness();
    // }
    // return scoreSum / this.population;
    // } // end method

}
