package source_code;

import java.util.ArrayList;

public class ChromoGeneration {
	
	
	// Fields
	public ArrayList<Chromosome> population = new ArrayList<Chromosome>();
	public Integer genNumber;
	public Integer populationSize;
	public Integer chromosomeLength;
	public String fitnessType;
	
	
	// Constructor
	public ChromoGeneration(Integer genNumber, Integer populationSize, Integer chromosomeLength, String fitnessType) {
		this.genNumber = genNumber;
		this.populationSize = populationSize;
		this.chromosomeLength = chromosomeLength;
		this.fitnessType = fitnessType;
		
		for (int i = 0; i < populationSize; i++) {
			this.population.add(new Chromosome(chromosomeLength));
		} // end for loop
	} // end constructor
	
	
	// getAverageHammingDistance method
	public Integer getAverageHammingDistance() {
		
		Integer hammingDistanceSum = 0;
		Integer comparisonCount = 0;
		
		for (int i = 0; i < this.populationSize; i++) {				// get chromosome
			Chromosome c = this.population.get(i);
			
			for (int j = i + 1; j < this.populationSize; j++) {		// get another chromosome to compare it to
				Chromosome comparison = this.population.get(j);
				
				comparisonCount++;
				
				for (int k = 0; k < c.length; k++) {				// go through all genes
					Integer geneOfC = c.genes.get(k);
					Integer geneOfComparison = comparison.genes.get(k);
					
					if (geneOfC != geneOfComparison) {
						hammingDistanceSum++;
					} // end if statement
				} // end for loop
			} // end for loop
		} // end for loop
		return hammingDistanceSum / comparisonCount;		
	} // end method
	
	
	// getHighestFitness method
	public Integer getHighestFitness() {
		Integer highestScore = 0;
		
		for (Chromosome c : this.population) {
			if (c.evaluateFitness(fitnessType) > highestScore) {
				highestScore = c.evaluateFitness(fitnessType);
			} // end if statement
		} // end for loop
		return highestScore;
	} // end method
	
	
	// getLowestFitness method
	public Integer getLowestFitness() {
		Integer lowestScore = 100;
		
		for (Chromosome c : this.population) {
			if (c.evaluateFitness(fitnessType) < lowestScore) {
				lowestScore = c.evaluateFitness(fitnessType);
			} // end if statement
		} // end for loop
		return lowestScore;
	} // end method
	
	
	// getAverageFitness method	
	public Integer getAverageFitness() {
		Integer scoreSum = 0;
		
		for (Chromosome c : this.population) {
			scoreSum += c.evaluateFitness(fitnessType);
		}
		return scoreSum / this.populationSize;
	} // end method
	
	
} // end class
