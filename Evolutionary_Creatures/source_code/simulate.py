from simulation import SIMULATION
import sys

directOrGUI = sys.argv[1]
solutionID = sys.argv[2]

simulation = SIMULATION(directOrGUI, solutionID)
simulation.Run()
simulation.Get_Fitness()

##################################################
# To execute, from a terminal run the command:
# python3 simulation.py
# OR
# python simulation.Ppy

# Trouble getting this to run? contact your instructor or TA
