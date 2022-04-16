import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

// Manages evolution (selection, mutation, crossover)
public class GeneticAlgorithm {

  // Fields
  public Integer chromosomeLength;
  public Integer population;
  public Double mutationRate;
  public String selectionType = "Truncation"; // change this to Roulette Wheel, or Truncation
  public Integer elitismPercent = 0;
  public boolean crossover = false;
  public Integer terminationScore;
  public Integer maxGeneration;
  public Integer lastGenerationNum;

  ArrayList<Generation> allGenerations = new ArrayList<Generation>();
  ArrayList<Integer> bestFitness = new ArrayList<Integer>();
  ArrayList<Integer> worstFitness = new ArrayList<Integer>();
  ArrayList<Integer> avgFitness = new ArrayList<Integer>();

  public GeneticAlgorithm() {
    this.chromosomeLength = 100; // should be 100
    this.population = 100;
    this.mutationRate = 50.0 / chromosomeLength;
    this.terminationScore = 100;
    this.maxGeneration = 200;
  }

  public static void main(String[] args) {
    GeneticAlgorithm GA = new GeneticAlgorithm();
    GA.createGeneration(0, GA.population, GA.chromosomeLength);

    for (int i = 1; i < GA.maxGeneration; i++) {
      if (GA.bestFitness.get(GA.bestFitness.size() - 1) >= GA.terminationScore) {
        GA.lastGenerationNum = i;
        GA.writeCSVFile();
        System.out.println("Used " + GA.selectionType + " method");
        System.out.println("Best Fitness in each generation: " + GA.bestFitness);
        System.err.println("Evolution terminated by reaching best score");
        return;
      }
      GA.nextGeneration(i, GA.mutationRate);
    }
    GA.writeCSVFile();
    System.out.println("Used " + GA.selectionType + " method");
    System.out.println("Best Fitness in each generation: " + GA.bestFitness);
  }

  public void createGeneration(Integer generationNum, Integer population, Integer chromosomeLength) {
    Generation genZero = new Generation(chromosomeLength, population);
    allGenerations.add(genZero);

    // Using seed for randomly getting objects
    int seed = 42; // should be 42
    int random = 0;
    Random r = new Random(seed);

    for (int i = 0; i < population; i++) {
      genZero.allChromosomes.add(new Chromosome(chromosomeLength));
      for (int j = 0; j < chromosomeLength; j++) {
        random = r.nextInt(2);
        genZero.allChromosomes.get(i).genes.add(random);
      }
    }

    System.out.println("Created Generation " + generationNum + " of Chromosomes");

    for (int i = 0; i < genZero.population; i++) {
      genZero.allChromosomes.get(i).evaluateFitness();
    }

    sortChromosomes(0);
    getBestFitness(0);
    printChromosomes(0);

    System.out.println("Generation " + generationNum + " Fitness Scores: ");

    for (int i = 0; i < genZero.population; i++) {
      System.out.print(genZero.allChromosomes.get(i).fitnessScore + ", ");
    }
    System.out.println();
    System.out.println("------------------------------");
  }

  public Integer evaluateFitness(Integer generationNum) {
    int fitness = 0;
    for (int i = 0; i < chromosomeLength; i++) {
      fitness = 0;
      for (int j = 0; j < allGenerations.get(generationNum).allChromosomes.get(i).genes.size(); j++) {
        fitness += allGenerations.get(generationNum).allChromosomes.get(i).genes.get(j);
      }
      allGenerations.get(generationNum).allChromosomes.get(i).fitnessScore = fitness;
    }
    return fitness;
  }

  public Integer getBestFitness(Integer generationNum) {
    int size = allGenerations.get(generationNum).allChromosomes.size() - 1;
    int bestInt = allGenerations.get(generationNum).allChromosomes.get(size).fitnessScore;
    bestFitness.add(bestInt);
    return bestInt;
  }

  public Integer getWorstFitness(Integer generationNum) {
    int wortstInt = allGenerations.get(generationNum).allChromosomes.get(0).fitnessScore;
    worstFitness.add(wortstInt);
    return wortstInt;
  }

  public Integer getAverageFitness(Integer generationNum) {

    int scoreSum = 0;

    for (int i = 0; i < allGenerations.get(0).allChromosomes.size(); i++) {
      scoreSum += allGenerations.get(generationNum).allChromosomes.get(i).fitnessScore;
    }

    int avgInt = scoreSum / allGenerations.get(generationNum).allChromosomes.size();
    avgFitness.add(avgInt);
    return avgInt;
  }

  public void sortChromosomes(Integer generationNum) {
    ArrayList<Chromosome> chr = new ArrayList<>();
    for (int i = 0; i < chromosomeLength; i++) {
      int min = this.terminationScore;
      int minIndex = 0;
      for (int j = 0; j < allGenerations.get(generationNum).allChromosomes.size(); j++) {
        int currentScore = allGenerations.get(generationNum).allChromosomes.get(j).fitnessScore;
        // System.out.println("i: " + i + " / curscore: " + currentScore);
        if (currentScore < min) {
          min = currentScore;
          minIndex = j;
        }
      }

      chr.add(allGenerations.get(generationNum).allChromosomes.get(minIndex));
      allGenerations.get(generationNum).allChromosomes.remove(minIndex);
    }

    for (int index = 0; index < chr.size(); index++) {
      allGenerations.get(generationNum).allChromosomes.add(chr.get(index));
    }
    // System.out.println("Sorted chromosomes: " +
    // allGenerations.get(generationNum).allChromosomes);
  }

  public void mutateChromosomes(Integer generationNum, Double mutationRate) {
    // System.out.println("Original: " +
    // allGenerations.get(generationNum).allChromosomes.get(0).genes);
    Chromosome ch = new Chromosome(allGenerations.get(generationNum).allChromosomes.get(0).genes);
    ch.mutate(mutationRate);
    // System.out.println("Mutated: " + mutatedList);
  }

  public ArrayList<Chromosome> crossOver(Integer generationNum, Double mutationRate, ArrayList<Integer> parent1,
      ArrayList<Integer> parent2, Integer crossOverPoints) {

    Random r = new Random();
    int originalCrossNum = crossOverPoints;
    int crossPoint = 0;

    ArrayList<Chromosome> result = new ArrayList<Chromosome>();
    ArrayList<Integer> newChromosome1 = new ArrayList<Integer>();
    ArrayList<Integer> newChromosome2 = new ArrayList<Integer>();
    ArrayList<Integer> offspring = new ArrayList<Integer>();

    while (crossOverPoints > 0) {
      crossPoint = r.nextInt(chromosomeLength + 1);

      if (crossOverPoints == 1) {
        System.out.println("[Trial " + (originalCrossNum - crossOverPoints + 1) + "] Cross point is " + crossPoint);
        for (int i = 0; i < crossPoint; i++) {
          offspring.add(parent1.get(i));
        }

        for (int i = crossPoint; i < parent1.size(); i++) {
          offspring.add(parent2.get(i));
        }
        crossOverPoints--;

      } else {
        System.out.println("[Trial " + (originalCrossNum - crossOverPoints + 1) + "] Cross point is " + crossPoint);
        for (int i = 0; i < crossPoint; i++) {
          newChromosome1.add(parent1.get(i));
          newChromosome2.add(parent2.get(i));
        }

        for (int i = crossPoint; i < parent1.size(); i++) {
          newChromosome1.add(parent2.get(i));
          newChromosome2.add(parent1.get(i));
        }
        parent1.clear();
        parent2.clear();
        parent1 = newChromosome1;
        parent2 = newChromosome2;
        crossOverPoints--;
        // System.out.println("crossover p1: " + parent1);
        // System.out.println("crossover p2: " + parent2);
      }
    }
    Chromosome sequence1 = new Chromosome(parent1);
    Chromosome sequence2 = new Chromosome(parent1);

    result.add(sequence1);
    result.add(sequence2);

    return result;
    // System.out.println("offspring is: " + offspring);

  }

  // Generational loop mutations and selection
  public void nextGeneration(Integer generationNum, Double mutationRate) {

    Generation previousGeneration = this.allGenerations.get(generationNum - 1);

    // Create Hashmap of fitnessScore and chromosomes
    HashMap<Chromosome, Integer> fitnessScoreMap = new HashMap<Chromosome, Integer>();
    Integer populationScoreSum = 0;

    for (int i = 0; i < previousGeneration.population; i++) {
      Chromosome c = previousGeneration.allChromosomes.get(i);
      Integer fitnessScore = previousGeneration.allChromosomes.get(i).fitnessScore;
      populationScoreSum += fitnessScore;
      fitnessScoreMap.put(c, fitnessScore);
    }

    // Children ArrayList
    ArrayList<Chromosome> children = new ArrayList<Chromosome>();

    // Elitism
    Integer eliteChildrenCount = (this.elitismPercent * previousGeneration.population) / 100;

    System.out.println("Number of Elite: " + eliteChildrenCount);

    for (int i = 0; i < eliteChildrenCount; i++) {
      Integer maxScore = 0;
      Chromosome best = null;
      for (Chromosome chromosome : fitnessScoreMap.keySet()) {
        Integer score = fitnessScoreMap.get(chromosome);
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
      fitnessScoreMap.remove(best);
    }

    if (this.selectionType == "Truncation") {

      // 1) Select survivors
      ArrayList<Chromosome> survivors = new ArrayList<Chromosome>();
      for (int i = 0; i < (previousGeneration.population / 2) - (children.size() / 2); i++) {
        Integer maxScore = 0;
        Chromosome best = null;
        for (Chromosome chromosome : fitnessScoreMap.keySet()) {
          Integer score = fitnessScoreMap.get(chromosome);
          if (score > maxScore) {
            maxScore = score;
            best = chromosome;
          }
        }
        survivors.add(best);
        fitnessScoreMap.remove(best);
      }

      // 2) Randomly Crossover
      Random randomizer = new Random();
      Integer crossoverCount = 0;

      for (Chromosome c : survivors) {

        if (crossover) {
          Chromosome crossoverMatch = survivors.get(randomizer.nextInt(survivors.size()));

          ArrayList<Chromosome> chr = crossOver(generationNum, mutationRate, c.genes, crossoverMatch.genes, 1);
          Chromosome ch1 = chr.get(0);
          Chromosome ch2 = chr.get(1);

          ch1.mutate(mutationRate);
          children.add(ch1);

          ch2.mutate(mutationRate);
          children.add(ch2);

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

      System.out.println("Crossovers completed: " + crossoverCount);
      System.out.println("Truncation method used with " + eliteChildrenCount + " elite children");
      System.out.println();

    } // end of truncation loop

    else if (this.selectionType == "Roulette Wheel") {
      // Create HashMap of chromosomes and children
      Integer childrenSum = 0;
      HashMap<Chromosome, Integer> chromosomeChildrenHashMap = new HashMap<Chromosome, Integer>();
      for (Chromosome c : fitnessScoreMap.keySet()) {
        Integer childrenNum = (fitnessScoreMap.get(c) * previousGeneration.population) / populationScoreSum;
        chromosomeChildrenHashMap.put(c, childrenNum);
        childrenSum += childrenNum;
      }

      Integer more = previousGeneration.population - eliteChildrenCount - childrenSum;
      for (int i = 0; i < more; i++) {
        Integer maxScore = 0;
        Chromosome best = null;

        for (Chromosome chromosome : fitnessScoreMap.keySet()) {
          Integer score = fitnessScoreMap.get(chromosome);
          if (score > maxScore) {
            maxScore = score;
            best = chromosome;
          }
        }

        chromosomeChildrenHashMap.replace(best, chromosomeChildrenHashMap.get(best) + 1);
        fitnessScoreMap.remove(best);
      }

      Random ran = new Random();

      Integer crossoverCount = 0;
      for (Chromosome c : chromosomeChildrenHashMap.keySet()) {

        if (crossover) { // crossover is happening
          Object[] withChildren = chromosomeChildrenHashMap.keySet().toArray();
          Chromosome crossoverMatch = (Chromosome) withChildren[ran.nextInt(withChildren.length)];

          ArrayList<Integer> geneSequence1 = new ArrayList<Integer>();
          ArrayList<Integer> geneSequence2 = new ArrayList<Integer>();

          for (int i = 0; i < c.genes.size() / 2; i++) {
            geneSequence1.add(c.genes.get(i));
            geneSequence2.add(crossoverMatch.genes.get(i));
          }

          for (int i = (c.genes.size() / 2); i < c.genes.size(); i++) {
            geneSequence1.add(crossoverMatch.genes.get(i));
            geneSequence2.add(c.genes.get(i));
          }

          ArrayList<Integer> geneSequence;

          if (crossoverCount % 2 == 0) {
            geneSequence = geneSequence1;
          } else {
            geneSequence = geneSequence2;
          }

          Integer index = 0;
          while (children.size() < previousGeneration.population && index < chromosomeChildrenHashMap.get(c)) {
            Chromosome child = new Chromosome(geneSequence);
            child.mutate(mutationRate);
            children.add(child);
            index++;
          }

          crossoverCount++;

        } else { // crossover is not happening

          ArrayList<Integer> geneSequence = new ArrayList<Integer>();
          for (int i = 0; i < c.genes.size(); i++) {
            geneSequence.add(c.genes.get(i));
          }

          Integer index = 0;
          while (children.size() < previousGeneration.population && index < chromosomeChildrenHashMap.get(c)) {
            Chromosome child = new Chromosome(geneSequence);
            child.mutate(mutationRate);
            children.add(child);
            index++;
          }
        }
      }

      System.out.println(crossoverCount + " crossovers complete");
      System.out.println("successful with " + eliteChildrenCount + " elite children");
      System.out.println("Roulette Wheel method used with " + eliteChildrenCount + " elite children");

    } // end of wheel loop

    // When elitism rate is a odd number
    if (this.elitismPercent % 2 != 0) {
      int size = children.size();
      children.remove(size - 1);
    }

    Generation newGeneration = new Generation(generationNum, previousGeneration.population,
        previousGeneration.chromosomeLength);
    newGeneration.allChromosomes = children;
    this.allGenerations.add(newGeneration);

    evaluateFitness(generationNum);
    sortChromosomes(generationNum);
    getBestFitness(generationNum);
    printChromosomes(generationNum);

    System.out.println();
  } // end of nextGeneration Loop

  public void writeCSVFile() {

    PrintWriter csvWriter;
    File csvFile = new File("source_code/plot_data.csv");

    try {
      csvFile.createNewFile();
    } catch (IOException e) {
      System.err.println("Error! File not created");
    }

    try {
      csvWriter = new PrintWriter(csvFile);

      csvWriter.print("generation,max,average,min\n");
      // System.out.println("lastgen: " + lastGenerationNum);
      for (int generationNum = 0; generationNum <= this.allGenerations.size() - 1; generationNum++) {
        csvWriter.printf("%d,%d,%d,%d\n", generationNum, getBestFitness(generationNum),
            getAverageFitness(generationNum), getWorstFitness(generationNum));
      }

      System.out.println("Evolution data successfully written to plot_data.csv");
      System.out.println();

      csvWriter.flush();
      csvWriter.close();

    } catch (FileNotFoundException e) {
      System.err.println("CSV file not found");
      // } catch (IndexOutOfBoundsException i) {
      // System.err.println("Error! Index out of bounds");
    }
  }

  public void printFitnessOfChromosomes(Integer generationNum) {
    for (int i = 0; i < chromosomeLength; i++) {
      System.out.println("Fitness score : " + allGenerations.get(generationNum).allChromosomes.get(i).fitnessScore);
    }
  }

  public void printChromosomes(Integer generationNum) {
    for (int i = 0; i < population; i++) {
      System.out.println(allGenerations.get(generationNum).allChromosomes.get(i).genes);
    }
  }
}
