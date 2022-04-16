package source_code;

import java.util.ArrayList;
import java.util.Random;

public class Algo {

    // specify evolutionary objective here (of the GeneticAlgorithm)
    public String fitnessType = "countNumOf1s"; // or "countNumOf0s", "maxConsec1s", "maxConsec0s"

    /**
     * terminate at generation 200, those that haven't found a solution will be penalized proportionally
     * fix chromosome genome length at 100, terminate at chromosome score 95 
     * vary: mutation rate, population size, selection type, elitism percent, crossover on/off
     * 
     * mutation rate from 0 to 100, encode in a 7-length bit string 
     *      handle 101-127 as 0 
     * populaton size from 10 to 200, encode in a 8-length bit string 
     *      handle 0-9, 201-255 as 10 
     * selection type between truncation(0) and roulette wheel(1), encode in 1 bit 
     * elitism percent from 0 to 100, encode in a 7-length bit string
     *      handle 101-127 as 0 
     * crossover on(1) and off(0), encode in 1 bit
     * 
     * Algo will have genome length 7 + 8 + 1 + 7 + 1 = 24 bits
     */

    
    public static void main(String[] args) {

        for (int i = 0; i < 20; i++) {
            // new Algo().//fitness();
        }
    }


    // Fields
    public ArrayList<Integer> genes = new ArrayList<Integer>();
    public Integer length = 24;
    public GeneticAlgorithm algorithm = new GeneticAlgorithm();

    // fixed parameters
    public Integer maxGeneration = 200;
    public Integer genomeLength = 100;
    public Integer terminationScore = 95;

    // variable parameters
    public Integer mutationRate;
    public Integer populationSize;
    public String selectionType;
    public Integer elitismPercent;
    public Boolean crossover;


    // Constructor (random gene generation, used in first generation)
    public Algo() {

        for (int i = 0; i < length; i++) {
            this.genes.add(new Random().nextInt(2));
        }

    } // end constructor


    // Constructor (gene sequence passed, as a child from a parent)
    public Algo(ArrayList<Integer> genes) {

        for (int i = 0; i < length; i++) {
            this.genes.add(genes.get(i));
        }

    } // end constructor


    // expressPhenotype()
    public void expressPhenotype() {

        // instantiate string to combine all genes (binary)
        StringBuilder binaryString = new StringBuilder();

        // transcribe binary genes to string
        for (int i = 0; i < length; i++) {
            binaryString.append(genes.get(i));
        }

        // decode genes to algorithm parameters
        Integer decodedMutationRate = Integer.parseInt(binaryString.substring(0, 7), 2);
        Integer decodedPopulationSize = Integer.parseInt(binaryString.substring(7, 15), 2);
        Boolean decodedIsTruncation = (genes.get(15) == 0);
        Integer decodedElitismPercent = Integer.parseInt(binaryString.substring(16, 23), 2);
        Boolean decodedCrossoverOn = (genes.get(23) == 1);

        // handle exceptional bounds
        if (decodedMutationRate > 100) {
            decodedMutationRate = 0;
        }
        if (decodedPopulationSize % 2 == 1) {
            decodedPopulationSize++;
        }
        if (decodedPopulationSize < 10 || decodedPopulationSize > 200) {
            decodedPopulationSize = 10;
        }

        if (decodedElitismPercent > 100) {
            decodedElitismPercent = 0;
        }

        // express genes to parameters, to control GeneticAlgorithm in the fitness() method below
        mutationRate = decodedMutationRate;
        populationSize = decodedPopulationSize;
        if (decodedIsTruncation) {
            selectionType = "Truncation";
        } else {
            selectionType = "Roulette Wheel";
        }
        elitismPercent = decodedElitismPercent;
        crossover = decodedCrossoverOn;

    } // end method


    // fitness()
    public Integer fitness() {

        // express phenotype (moved from constructor, so it's only called when needed)
        expressPhenotype();

        // pass setup configurations
        algorithm.selectionType = selectionType;
        algorithm.elitismPercent = elitismPercent;
        algorithm.crossover = crossover;
        algorithm.fitnessType = fitnessType;

        // create first generation
        algorithm.createGeneration(1, populationSize, genomeLength);

        // variable to track number of generations
        Integer atGeneration = 1;

        // run evolution
        while (true) {
            atGeneration++; 
            algorithm.nextGeneration(atGeneration, mutationRate);

            Integer bestScore = algorithm.allGenerations.get(atGeneration - 1).getHighestFitness();

            // if terminated by score,
            if (bestScore >= terminationScore) {
                System.out.println("\nALGORITHM FOUND SOLUTION AT GENERATION " + atGeneration);

                return atGeneration;
            
            // or if terminated by generation,
            } else if (atGeneration >= maxGeneration) {
    
                // to reduce runtime while distinguishing performance, algorithms that were not able to
                // evolve a solution within the maxGeneration will be penalized according to their
                // distance to the solution
                return maxGeneration + (terminationScore - bestScore) * 3;

            } // end if statement

        } // end while loop

    } // end method


    // mutate()
    public void mutate(Integer mutationRate) {

        // Construct Random object to use
        Random randomizer = new Random();

        // Randomly change genes
        for (int i = 0; i < this.length; i++) {

            if (mutationRate == 0) {
                continue;

            } else if (randomizer.nextDouble() <= 1.0 * (new Double(mutationRate) / this.length)) {

                if (this.genes.get(i) == 0) { // if gene is 0,
                    this.genes.remove(i); // remove the gene at index i,
                    this.genes.add(i, 1); // and add new gene at same index, as 1

                } else {
                    this.genes.remove(i);
                    this.genes.add(i, 0);
                } // end if statement

            } // end if statement
        } // end for loop
    } // end method

} // end class