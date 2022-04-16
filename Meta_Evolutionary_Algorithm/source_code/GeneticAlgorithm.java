package source_code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class GeneticAlgorithm {
	
	
	// Fields
	ArrayList<ChromoGeneration> allGenerations = new ArrayList<ChromoGeneration>();	
	String fitnessType = "countNumOf1s";
	String selectionType = "Truncation";
	Integer elitismPercent = 0;
	boolean crossover = false;
	
	
	// createGeneration method
	public void createGeneration(Integer genNumber, Integer populationSize, Integer chromosomeLength) {
		
		// construct a randomly generated population
		ChromoGeneration g = new ChromoGeneration(genNumber, populationSize, chromosomeLength, this.fitnessType);
		
		// add to allGenerations
		this.allGenerations.add(g);
		
		// System.out.println("Fitness scores for Generation 1 are: ");
		// for (int i = 0; i < g.populationSize; i++) {
		// 	System.out.print(g.population.get(i).evaluateFitness(fitnessType) + ", ");
		// }
		// System.out.println();
		// System.out.println("Fitness Type - " + this.fitnessType);
		// System.out.println();
	} // end method
	
	
	// nextGeneration method
	public void nextGeneration(Integer genNumber, Integer mutationRate) {
		
		// get previous generation
		ChromoGeneration prevGeneration = this.allGenerations.get(genNumber - 2);
		
		// construct HashMap that links each chromosome to its score
		HashMap<Chromosome, Integer> chromosomeScoreMap = new HashMap<Chromosome, Integer>();
		Integer populationScoreSum = 0;
		for (int i = 0; i < prevGeneration.populationSize; i++) {
			Chromosome c = prevGeneration.population.get(i);
			Integer fitnessScore = prevGeneration.population.get(i).evaluateFitness(this.fitnessType);
			populationScoreSum += fitnessScore;
			chromosomeScoreMap.put(c, fitnessScore);
		}
		
		// list to hold children
		ArrayList<Chromosome> children = new ArrayList<Chromosome>();
		
		// elitism
		Integer numOfEliteChildren = (this.elitismPercent * prevGeneration.populationSize) / 100;
		
		for (int i = 0; i < numOfEliteChildren; i++) {
			Integer maxScore = 0;
			Chromosome best = null;
			for (Chromosome chromosome : chromosomeScoreMap.keySet()) {
				Integer score = chromosomeScoreMap.get(chromosome); 
				if (score > maxScore) {
					maxScore = score;
					best = chromosome;
				}
			}
			ArrayList<Integer> geneSequence = new ArrayList<Integer>();
			for (int j = 0; j < best.genes.size(); j++) {
				geneSequence.add(best.genes.get(j));
			}			
			Chromosome child = new Chromosome(geneSequence);
			children.add(child);
			chromosomeScoreMap.remove(best);
		}
		
		
		if (this.selectionType == "Roulette Wheel") {
			
			// construct HashMap that links each chromosome to the number of childrens it deserves
			Integer childrenSum = 0;
			HashMap<Chromosome, Integer> chromosomeChildrenMap = new HashMap<Chromosome, Integer>();
			for (Chromosome c : chromosomeScoreMap.keySet()) {
				Integer numberOfChildren = (chromosomeScoreMap.get(c) * prevGeneration.populationSize) / populationScoreSum;
				chromosomeChildrenMap.put(c, numberOfChildren);
				childrenSum += numberOfChildren;
			}
			
			
			// if space will be left, assign more children to the best chromosomes
			Integer spaceLeft = prevGeneration.populationSize - numOfEliteChildren - childrenSum;
			for (int i = 0; i < spaceLeft; i++) {
				Integer maxScore = 0;
				Chromosome best = null;
				
				for (Chromosome chromosome : chromosomeScoreMap.keySet()) {
					Integer score = chromosomeScoreMap.get(chromosome); 
					if (score > maxScore) {
						maxScore = score;
						best = chromosome;
					} // end if
				} // end for
				
				// give 1 more children for the best chromosome
				chromosomeChildrenMap.replace(best, chromosomeChildrenMap.get(best) + 1);
				
				// remove best and its score from the chromosomeScoreMap
				chromosomeScoreMap.remove(best);
			}
			
			// randomizer for choosing crossover match
			Random randomizer = new Random();
			
			Integer crossoverCount = 0;
			
			// create children for chromosomes that deserve 1 or more children
			for (Chromosome c : chromosomeChildrenMap.keySet()) {
				
				
				if (crossover) {
					Object[] withChildren = chromosomeChildrenMap.keySet().toArray();
					Chromosome crossoverMatch = (Chromosome) withChildren[randomizer.nextInt(withChildren.length)];
					
					ArrayList<Integer> geneSequence1 = new ArrayList<Integer>();
					ArrayList<Integer> geneSequence2 = new ArrayList<Integer>();
					
					for (int i = 0; i < c.genes.size() / 2; i++) {
						geneSequence1.add(c.genes.get(i));				// geneSequence1 gets the first half of the c's genes
						geneSequence2.add(crossoverMatch.genes.get(i)); // geneSequence2 gets the first half of the match's genes
					}	
					
					for (int i = (c.genes.size() / 2) ; i < c.genes.size(); i++) {
						geneSequence1.add(crossoverMatch.genes.get(i)); // geneSequence1 gets the second half of the match's genes
						geneSequence2.add(c.genes.get(i));				// geneSequence2 gets the first half of the match's genes
					}	
					
					ArrayList<Integer> geneSequence;
					
					if (crossoverCount % 2 == 0) {
						geneSequence = geneSequence1;
					} else {
						geneSequence = geneSequence2;
					}
					
					Integer index = 0;
					while (children.size() < prevGeneration.populationSize && index < chromosomeChildrenMap.get(c)) {
						Chromosome child = new Chromosome(geneSequence);
						child.mutate(mutationRate);
						children.add(child);
						index++;
					}
					
					crossoverCount++;
					
				} else {
					
					// copy gene sequence
					ArrayList<Integer> geneSequence = new ArrayList<Integer>();
					for (int i = 0; i < c.genes.size(); i++) {
						geneSequence.add(c.genes.get(i));
					}
					
					// create as many children as it deserves, as long as the children list isn't full
					Integer index = 0;
					while (children.size() < prevGeneration.populationSize && index < chromosomeChildrenMap.get(c)) {
						Chromosome child = new Chromosome(geneSequence);
						child.mutate(mutationRate);
						children.add(child);
						index++;
					}
				}
				
			}
			// System.out.println("     " + crossoverCount + " crossovers complete");
			// System.out.println("     " + "Roulette Wheel successful with " + numOfEliteChildren + " elite children");
			// System.out.println();
			
			
			
		} else if (this.selectionType == "Truncation") {
			
			// after elitism, extract an arraylist of chromosomes that would survive
			ArrayList<Chromosome> survived = new ArrayList<Chromosome>();
			for (int i = 0; i < (prevGeneration.populationSize / 2) - (children.size() / 2); i++) {
				Integer maxScore = 0;
				Chromosome best = null;
				for (Chromosome chromosome : chromosomeScoreMap.keySet()) {
					Integer score = chromosomeScoreMap.get(chromosome); 
					if (score > maxScore) {
						maxScore = score;
						best = chromosome;
					}
				}
				survived.add(best);
				chromosomeScoreMap.remove(best);
			}	
			
			// randomizer for choosing crossoverMatch
			Random randomizer = new Random();
			
			Integer crossoverCount = 0;
			
			// creating children from survived chromosomes
			for (Chromosome c : survived) {
				
				if (crossover) {
					Chromosome crossoverMatch = survived.get(randomizer.nextInt(survived.size()));
					
					ArrayList<Integer> geneSequence1 = new ArrayList<Integer>();
					ArrayList<Integer> geneSequence2 = new ArrayList<Integer>();
					
					for (int i = 0; i < c.genes.size() / 2; i++) {
						geneSequence1.add(c.genes.get(i));				// geneSequence1 gets the first half of the c's genes
						geneSequence2.add(crossoverMatch.genes.get(i)); // geneSequence2 gets the first half of the match's genes
					}	
					
					for (int i = (c.genes.size() / 2) ; i < c.genes.size(); i++) {
						geneSequence1.add(crossoverMatch.genes.get(i)); // geneSequence1 gets the second half of the match's genes
						geneSequence2.add(c.genes.get(i));				// geneSequence2 gets the first half of the match's genes
					}	
					
					Chromosome child1 = new Chromosome(geneSequence1);
					child1.mutate(mutationRate);
					children.add(child1);
					
					Chromosome child2 = new Chromosome(geneSequence2);
					child2.mutate(mutationRate);
					children.add(child2);
					
					crossoverCount++;
					
				} else {
					
					ArrayList<Integer> geneSequence = new ArrayList<Integer>();
					for (int i = 0; i < c.genes.size(); i++) {
						geneSequence.add(c.genes.get(i));
					}			
					Chromosome child1 = new Chromosome(geneSequence);
					child1.mutate(mutationRate);
					children.add(child1);
					
					Chromosome child2 = new Chromosome(geneSequence);
					child2.mutate(mutationRate);
					children.add(child2);
				}
				
			} 
			// System.out.println("     " + crossoverCount + " crossovers complete");
			// System.out.println("     " + "Truncation successful with " + numOfEliteChildren + " elite children");
			// System.out.println();
			
		} // end if
		
		ChromoGeneration newGeneration = new ChromoGeneration(genNumber, prevGeneration.populationSize, prevGeneration.chromosomeLength, 
													this.fitnessType);
		newGeneration.population = children;
		this.allGenerations.add(newGeneration);
		
		// System.out.println("Fitness scores for Generation " + genNumber + " are: ");
		// for (int i = 0; i < newGeneration.populationSize; i++) {
		// 	System.out.print(newGeneration.population.get(i).evaluateFitness(this.fitnessType) + ", ");
		// }
		// System.out.println();
		// System.out.println("Fitness Type - " + this.fitnessType);
		// System.out.println();
		
	} // end method
	

	
	
} // end class