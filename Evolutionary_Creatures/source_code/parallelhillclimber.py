from solution import SOLUTION
import constants as c
import copy
import os

class PARALLEL_HILL_CLIMBER:

    def __init__(self):
       
        os.system("rm brain*.nndf")
        os.system("rm fitness*.txt")

        self.nextAvailableID = 0
        self.parents = {}

        for i in range(c.populationSize):
            self.parents[i] = SOLUTION(self.nextAvailableID)
            self.nextAvailableID += 1

    
    def Evolve_For_One_Generation(self):
        self.Spawn()
        self.Mutate()
        self.Evaluate(self.children)
        self.Print()
        self.Select()

    def Evolve(self):
        self.Evaluate(self.parents)
        for currentGeneration in range(c.genNum):
            self.Evolve_For_One_Generation()

    def Spawn(self):
        self.children = {}
        for i in range(c.populationSize):
            self.children[i] = copy.deepcopy(self.parents[i])
            self.children[i].Set_ID(self.nextAvailableID)
            self.nextAvailableID += 1

        
    def Mutate(self):
        for i in range(c.populationSize):
            self.children[i].Mutate()
    

    def Select(self):
        for i in range(c.populationSize):
            if float(self.children[i].fitness) < float(self.parents[i].fitness):
                self.parents[i] = copy.deepcopy(self.children[i])


    def Print(self):
        for i in range(c.populationSize):
            print("Parent Fitness: " + self.parents[i].fitness + " | Child Fitness: " + self.children[i].fitness + "\n")


    def Show_Best(self):
        bestFitness = float("inf")
        bestFitnessKey = None

        for i in range(c.populationSize):
            if float(self.parents[i].fitness) < bestFitness:
                bestFitness = float(self.parents[i].fitness)
                bestFitnessKey = i
        
        self.parents[bestFitnessKey].Start_Simulation("GUI")

    def Evaluate(self, solutions):
        for i in range(c.populationSize):
            solutions[i].Start_Simulation("DIRECT")

        for i in range(c.populationSize):
            solutions[i].Wait_For_Simulation_To_End()