import java.util.ArrayList;
import java.util.Random;

/**
 * Chromosome class used to manage one bit-string of chromosomes Also where
 * mutation happens
 */
public class Chromosome {

	// Fields
	public Integer fitnessScore;
	public Integer chromosomeLength;
	public ArrayList<Integer> genes = new ArrayList<Integer>();

	// Constructor for Chromosome Class
	public Chromosome(Integer length) {
		this.chromosomeLength = length;
	}

	public Chromosome(ArrayList<Integer> genes) {
		for (int i = 0; i < genes.size(); i++) {
			this.genes.add(genes.get(i));
		}
		this.chromosomeLength = this.genes.size();
	}

	// evaluateFitness method
	public Integer evaluateFitness() {
		int fitness = 0;
		for (int j = 0; j < this.genes.size(); j++) {
			fitness += this.genes.get(j);
		}
		this.fitnessScore = fitness;
		return fitness;
	}

	public void mutate(Double mutationRate) {
		Random randomizer = new Random();
		// System.out.println("Original: " + this.genes);

		// Randomly change genes
		for (int i = 0; i < this.genes.size(); i++) {

			if (randomizer.nextDouble() <= 1.0 * mutationRate) {
				if (this.genes.get(i) == 0) { // if gene is 0,
					this.genes.remove(i); // remove the gene at index i,
					this.genes.add(i, 1); // and add new gene at same index, as 1

				} else {
					this.genes.remove(i);
					this.genes.add(i, 0);
				}
			}
		}
	}

}