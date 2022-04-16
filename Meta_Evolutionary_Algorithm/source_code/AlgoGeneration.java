package source_code;

import java.util.ArrayList;
import java.util.HashMap;

public class AlgoGeneration {
	
	
	// Fields
	public ArrayList<Algo> population = new ArrayList<Algo>();
    public HashMap<Algo, Integer> algoScoreMap; // save for efficient reuse
    public Algo bestAlgo;   // save for efficient reuse
    public Algo worstAlgo;  // save for efficient reuse
	public Integer genNumber;
	public Integer populationSize;
	public Integer algoGenomeLength;
	
	
	// Constructor
	public AlgoGeneration(Integer genNumber, Integer populationSize) {
		this.genNumber = genNumber;
		this.populationSize = populationSize;
		
		for (int i = 0; i < populationSize; i++) {
			this.population.add(new Algo());
		} // end for loop
	} // end constructor


    // mapAlgoFitness()
    public HashMap<Algo, Integer> mapAlgoFitness() {
        
        HashMap<Algo, Integer> algoScoreMap = new HashMap<Algo, Integer>();

        for (Algo a : this.population) {
            algoScoreMap.put(a, a.fitness());
        }

        // assign as field for reuse
        this.algoScoreMap = algoScoreMap;

        return algoScoreMap;
    }


    // getBestAlgo()
    public Algo getBestAlgo() {
        Integer lowestFitness = Integer.MAX_VALUE;
        Algo best = null;

        for (Algo a : this.algoScoreMap.keySet()) {
            if (this.algoScoreMap.get(a) < lowestFitness) {
                lowestFitness = this.algoScoreMap.get(a);
                best = a;
            }
        }

        this.bestAlgo = best;
        return best;
    }


    // getWorstAlgo()
    public Algo getWorstAlgo() {
        Integer highestFitness = 0;
        Algo worst = null;

        for (Algo a : this.algoScoreMap.keySet()) {
            if (this.algoScoreMap.get(a) > highestFitness) {
                highestFitness = this.algoScoreMap.get(a);
                worst = a;
            }
        }

        this.worstAlgo = worst;
        return worst;
    }
		
	
	// getAverageFitness()
	public Integer getAverageFitness() {
		Integer scoreSum = 0;

        for (Integer score : this.algoScoreMap.values()) {
			scoreSum += score;
		}
		
		return scoreSum / this.algoScoreMap.size();
	}
	
	
} // end class
