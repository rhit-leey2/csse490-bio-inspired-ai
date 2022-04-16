package source_code;

import java.util.ArrayList;
import java.util.Random;

public class Chromosome {

	
	// Fields
	public ArrayList<Integer> genes = new ArrayList<Integer>();
	public Integer length;	
	
	
	// Constructor (gene sequence passed)
	public Chromosome(ArrayList<Integer> genes) {
		
		for (int i = 0; i < genes.size(); i++) {
			this.genes.add(genes.get(i));
		}
		
		this.length = this.genes.size();
	} // end constructor
	
	
	// Constructor (random gene generation)
	public Chromosome(Integer length) {
		this.length = length;

		Random randomizer = new Random();
		for (int i = 0; i < length; i++) {
			this.genes.add(randomizer.nextInt(2));
		}
	} // end constructor

	
	// mutate method
	public void mutate(Integer mutationRate) {
		
		// Construct Random object to use
		Random randomizer = new Random();

		// Randomly change genes
		for (int i = 0; i < this.length; i++) {
			
			if (mutationRate == 0) {
				continue;
				
			} else if (randomizer.nextDouble() <= 1.0 * (new Double(mutationRate) / this.length)) { 
				
				if (this.genes.get(i) == 0) { 	// if gene is 0,
					this.genes.remove(i); 		// remove the gene at index i,
					this.genes.add(i, 1); 		// and add new gene at same index, as 1
					
				} else {
					this.genes.remove(i);
					this.genes.add(i, 0);
				} // end if statement
				
			} // end if statement
		} // end for loop
	} // end method
	
	
	// evaluateFitness method
	public Integer evaluateFitness(String type) {
		
		if (type.equals("countNumOf1s")) {
			return this.countGene(1);
			
		} else if (type.equals("countNumOf0s")) {
			return this.countGene(0);
			
		} else if (type.equals("maxConsec1s")) {
			return this.countMaxConsecGene(1);
			
		} else if (type.equals("maxConsec0s")) {
			return this.countMaxConsecGene(0);
			
		} else {
			return 0;
		}
		
	} // end method
	
	
	// countGene method
	public Integer countGene(Integer gene) {
		Integer fitnessScore = 0;
		
		for (int i = 0; i < this.length; i++) {
			if (this.genes.get(i) == gene) {
				fitnessScore++;
			}
		}
		return fitnessScore;
	} // end method
	
	
	// countMaxConsecGene method
	public Integer countMaxConsecGene(Integer gene) {
		Integer maxConsecGene = 0;
		Integer currConsecGene = 0;
		Integer count = 0;
		
		for (int i = 0; i < this.genes.size() - 1; i++) {	
			if (this.genes.get(i) != gene) {
				count = 0;
				currConsecGene = 0;
			} // end if statement
			
			if (this.genes.get(i) == gene && this.genes.get(i + 1) == gene) {
				count++;
				
				if (count == 1) {
					currConsecGene += 2;
				} else {
					currConsecGene++;
				} // end if statement
				
				if (currConsecGene > maxConsecGene) {
					maxConsecGene = currConsecGene;
				} // end if statement
				
			} // end if statement
			
		} // end for loop
		return maxConsecGene;
	} // end method
	

} // end class