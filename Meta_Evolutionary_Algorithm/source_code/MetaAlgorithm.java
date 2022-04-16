package source_code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class MetaAlgorithm {

    public static void main(String[] args) {

        MetaAlgorithm evolveTheEvolution = new MetaAlgorithm();

        // specify population size in the second argument, 10 by default
        evolveTheEvolution.createGeneration(1, 10);    

        // specify max generations in the index bound, 30 by default
        for (int i = 2; i <= 30; i++) {

            // specify mutation rate in the second argument, 1 by default
            evolveTheEvolution.nextGeneration(i, 1);
        }

        // write to CSV
        evolveTheEvolution.writeToCSV();
    }

    // specify selectionType, elitismPercent and crossover on/off below
    String selectionType = "Truncation"; // or "Roulette Wheel"
	Integer elitismPercent = 10;
	boolean crossover = true;
	
	
	// Fields
	ArrayList<AlgoGeneration> allGenerations = new ArrayList<AlgoGeneration>();	
	
	
	// createGeneration method
	public void createGeneration(Integer genNumber, Integer populationSize) {
		
		// construct a randomly generated population
		AlgoGeneration g = new AlgoGeneration(genNumber, populationSize);
		
		// add to allGenerations
		this.allGenerations.add(g);

        // map fitness
        g.mapAlgoFitness();

        // print-outs (refactored for runtime efficiency)
        // also serves to identify and save best and worst algorithms
        System.out.println();
		System.out.println("Generation 1: ");

        Algo best = g.getBestAlgo();
        String bestOnOff;
        if (best.crossover) {
            bestOnOff = "on";
        } else {
            bestOnOff = "off";
        }
        System.out.println("The best algorithm evolved a solution in " + g.algoScoreMap.get(best)
        + " generations, with mutation rate " + best.mutationRate + ", population size "
        + best.populationSize + ", selection type " + best.selectionType + ", "
        + best.elitismPercent + "% elitism, and crossover " + bestOnOff);

        Algo worst = g.getWorstAlgo();
        String worstOnOff;
        if (worst.crossover) {
            worstOnOff = "on";
        } else {
            worstOnOff = "off";
        }
        System.out.println("The worst algorithm evolved a solution in " + g.algoScoreMap.get(worst)
        + " generations, with mutation rate " + worst.mutationRate + ", population size "
        + worst.populationSize + ", selection type " + worst.selectionType + ", "
        + worst.elitismPercent + "% elitism, and crossover " + worstOnOff);
        System.out.println();

	} // end method
	
	
	// nextGeneration method
	public void nextGeneration(Integer genNumber, Integer mutationRate) {
		
		// get previous generation
		AlgoGeneration prevGeneration = this.allGenerations.get(genNumber - 2);

        // deep copy the algoScoreMap of previous generation
        HashMap<Algo, Integer> algoScoreMap = new HashMap<Algo, Integer>();
        for (Algo a : prevGeneration.algoScoreMap.keySet()) {
            algoScoreMap.put(a, prevGeneration.algoScoreMap.get(a));
        }

		
		// list to hold children
		ArrayList<Algo> children = new ArrayList<Algo>();
		
		// elitism
		Integer numOfEliteChildren = (this.elitismPercent * prevGeneration.populationSize) / 100;
		
		for (int i = 0; i < numOfEliteChildren; i++) {
			Integer minScore = Integer.MAX_VALUE;
			Algo best = null;
			for (Algo alg : algoScoreMap.keySet()) {
				Integer score = algoScoreMap.get(alg); 
				if (score < minScore) {
					minScore = score;
					best = alg;
				}
			}
			ArrayList<Integer> geneSequence = new ArrayList<Integer>();
			for (int j = 0; j < best.genes.size(); j++) {
				geneSequence.add(best.genes.get(j));
			}			
			Algo child = new Algo(geneSequence);
			children.add(child);
			algoScoreMap.remove(best);
		}
		
		
		if (this.selectionType == "Roulette Wheel") {

            // determine population score sum
            Integer populationScoreSum = 0;
            for (Integer score : algoScoreMap.values()) {
                populationScoreSum += score;
            }
			
			// construct HashMap that links each algo to the number of childrens it deserves
			Integer childrenSum = 0;
			HashMap<Algo, Integer> algoChildrenMap = new HashMap<Algo, Integer>();
			for (Algo a : algoScoreMap.keySet()) {
				Integer numberOfChildren = (algoScoreMap.get(a) * prevGeneration.populationSize) / populationScoreSum;
				algoChildrenMap.put(a, numberOfChildren);
				childrenSum += numberOfChildren;
			}
			
			
			// if space will be left, assign more children to the best algos
			Integer spaceLeft = prevGeneration.populationSize - numOfEliteChildren - childrenSum;
			for (int i = 0; i < spaceLeft; i++) {
				Integer minScore = Integer.MAX_VALUE;
				Algo best = null;
				
				for (Algo alg : algoScoreMap.keySet()) {
					Integer score = algoScoreMap.get(alg); 
					if (score < minScore) {
						minScore = score;
						best = alg;
					} // end if
				} // end for
				
				// give 1 more children for the best algo
				algoChildrenMap.replace(best, algoChildrenMap.get(best) + 1);
				
				// remove best and its score from the algoScoreMap
				algoScoreMap.remove(best);
			}
			
			// randomizer for choosing crossover match
			Random randomizer = new Random();
			
			Integer crossoverCount = 0;
			
			// create children for algos that deserve 1 or more children
			for (Algo a : algoChildrenMap.keySet()) {
				
				
				if (crossover) {
					Object[] withChildren = algoChildrenMap.keySet().toArray();
					Algo crossoverMatch = (Algo) withChildren[randomizer.nextInt(withChildren.length)];
					
					ArrayList<Integer> geneSequence1 = new ArrayList<Integer>();
					ArrayList<Integer> geneSequence2 = new ArrayList<Integer>();
					
					for (int i = 0; i < a.genes.size() / 2; i++) {
						geneSequence1.add(a.genes.get(i));				// geneSequence1 gets the first half of the a's genes
						geneSequence2.add(crossoverMatch.genes.get(i)); // geneSequence2 gets the first half of the match's genes
					}	
					
					for (int i = (a.genes.size() / 2) ; i < a.genes.size(); i++) {
						geneSequence1.add(crossoverMatch.genes.get(i)); // geneSequence1 gets the second half of the match's genes
						geneSequence2.add(a.genes.get(i));				// geneSequence2 gets the first half of the match's genes
					}	
					
					ArrayList<Integer> geneSequence;
					
					if (crossoverCount % 2 == 0) {
						geneSequence = geneSequence1;
					} else {
						geneSequence = geneSequence2;
					}
					
					Integer index = 0;
					while (children.size() < prevGeneration.populationSize && index < algoChildrenMap.get(a)) {
						Algo child = new Algo(geneSequence);
						child.mutate(mutationRate);
						children.add(child);
						index++;
					}
					
					crossoverCount++;
					
				} else {
					
					// copy gene sequence
					ArrayList<Integer> geneSequence = new ArrayList<Integer>();
					for (int i = 0; i < a.genes.size(); i++) {
						geneSequence.add(a.genes.get(i));
					}
					
					// create as many children as it deserves, as long as the children list isn't full
					Integer index = 0;
					while (children.size() < prevGeneration.populationSize && index < algoChildrenMap.get(a)) {
						Algo child = new Algo(geneSequence);
						child.mutate(mutationRate);
						children.add(child);
						index++;
					}
				}
				
			}
			System.out.println("     " + crossoverCount + " crossovers complete");
			System.out.println("     " + "Roulette Wheel successful with " + numOfEliteChildren + " elite children");
			
			
			
		} else if (this.selectionType == "Truncation") {
			
			// after elitism, extract an arraylist of algos that would survive
			ArrayList<Algo> survived = new ArrayList<Algo>();
			for (int i = 0; i < (prevGeneration.populationSize / 2) - (children.size() / 2); i++) {
				Integer minScore = Integer.MAX_VALUE;
				Algo best = null;
				for (Algo alg : algoScoreMap.keySet()) {
					Integer score = algoScoreMap.get(alg); 
					if (score < minScore) {
						minScore = score;
						best = alg;
					}
				}
				survived.add(best);
				algoScoreMap.remove(best);
			}	
			
			// randomizer for choosing crossoverMatch
			Random randomizer = new Random();
			
			Integer crossoverCount = 0;
			
			// creating children from survived algos
			for (Algo a : survived) {
				
				if (crossover) {
					Algo crossoverMatch = survived.get(randomizer.nextInt(survived.size()));
					
					ArrayList<Integer> geneSequence1 = new ArrayList<Integer>();
					ArrayList<Integer> geneSequence2 = new ArrayList<Integer>();
					
					for (int i = 0; i < a.genes.size() / 2; i++) {
						geneSequence1.add(a.genes.get(i));				// geneSequence1 gets the first half of the a's genes
						geneSequence2.add(crossoverMatch.genes.get(i)); // geneSequence2 gets the first half of the match's genes
					}	
					
					for (int i = (a.genes.size() / 2) ; i < a.genes.size(); i++) {
						geneSequence1.add(crossoverMatch.genes.get(i)); // geneSequence1 gets the second half of the match's genes
						geneSequence2.add(a.genes.get(i));				// geneSequence2 gets the first half of the match's genes
					}	
					
					Algo child1 = new Algo(geneSequence1);
					child1.mutate(mutationRate);
					children.add(child1);
					
					Algo child2 = new Algo(geneSequence2);
					child2.mutate(mutationRate);
					children.add(child2);
					
					crossoverCount++;
					
				} else {
					
					ArrayList<Integer> geneSequence = new ArrayList<Integer>();
					for (int i = 0; i < a.genes.size(); i++) {
						geneSequence.add(a.genes.get(i));
					}			
					Algo child1 = new Algo(geneSequence);
					child1.mutate(mutationRate);
					children.add(child1);
					
					Algo child2 = new Algo(geneSequence);
					child2.mutate(mutationRate);
					children.add(child2);
				}
				
			} 
			System.out.println("     " + crossoverCount + " crossovers complete");
			System.out.println("     " + "Truncation successful with " + numOfEliteChildren + " elite children");
			
		} // end if
		
		AlgoGeneration newGeneration = new AlgoGeneration(genNumber, prevGeneration.populationSize);
		newGeneration.population = children;
        newGeneration.mapAlgoFitness();
		this.allGenerations.add(newGeneration);
        
		

        // print-outs (refactored for runtime efficiency)
        // also serves to identify and save best and worst algorithms
        System.out.println();
		System.out.println("Generation " + genNumber + ":");

        Algo best = newGeneration.getBestAlgo();
        String bestOnOff;
        if (best.crossover) {
            bestOnOff = "on";
        } else {
            bestOnOff = "off";
        }
        System.out.println("The best algorithm evolved a solution in " + newGeneration.algoScoreMap.get(best)
        + " generations, with mutation rate " + best.mutationRate + ", population size "
        + best.populationSize + ", selection type " + best.selectionType + ", "
        + best.elitismPercent + "% elitism, and crossover " + bestOnOff);

        Algo worst = newGeneration.getWorstAlgo();
        String worstOnOff;
        if (worst.crossover) {
            worstOnOff = "on";
        } else {
            worstOnOff = "off";
        }
        System.out.println("The worst algorithm evolved a solution in " + newGeneration.algoScoreMap.get(worst)
        + " generations, with mutation rate " + worst.mutationRate + ", population size "
        + worst.populationSize + ", selection type " + worst.selectionType + ", "
        + worst.elitismPercent + "% elitism, and crossover " + worstOnOff);
        System.out.println();
		
	} // end method

    // writeToCSV()
    public void writeToCSV() {

        // instantiate writer and file
        PrintWriter csvWriter;
        File csvFile = new File("source_code/plot_data.csv");

        // create file, disregard overwriting
        try {
            csvFile.createNewFile();
        } catch (IOException e) {
            System.err.println("CSV file not created");
        }

        // write data
        try {
            csvWriter = new PrintWriter(csvFile);

            // first row
            csvWriter.print("generation,bestFitness,averageFitness,worstFitness\n");

            // each row is a generation
            for (int genNum = 1; genNum <= this.allGenerations.size(); genNum++) {
                AlgoGeneration thisGen = this.allGenerations.get(genNum - 1);

                csvWriter.printf("%d,%d,%d,%d\n", genNum, thisGen.algoScoreMap.get(thisGen.bestAlgo), 
                thisGen.getAverageFitness(), thisGen.algoScoreMap.get(thisGen.worstAlgo));
            }

            //
            System.out.println();
            System.out.println("Evolution data successfully written to plot_data.csv");
            System.out.println();

            // close writer
            csvWriter.flush();
            csvWriter.close();

        } catch (FileNotFoundException e) {
            System.err.println("CSV file not found");
        }
    }
	
	
} // end class