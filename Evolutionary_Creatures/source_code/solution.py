import numpy as np
import random as rd
import constants as c
import time
import os
import pyrosim.pyrosim as pyrosim

class SOLUTION:
    def __init__(self, solID):
        self.weights = (np.random.rand(3, 2) * 2) - 1
        self.solID = solID
    
    def Evaluate(self, directOrGUI):
        self.Start_Simulation(directOrGUI)
        self.Wait_For_Simulation_To_End()

    def Start_Simulation(self, directOrGUI):
        self.Create_World()
        self.Create_Body()
        self.Create_Brain()

        os.system("start /B python simulate.py " + directOrGUI + " " + str(self.solID))
    
    def Wait_For_Simulation_To_End(self):
        fitnessFileName = "fitness" + str(self.solID) + ".txt"

        while not os.path.exists(fitnessFileName):
	        time.sleep(0.01)

        fitnessFile = open(fitnessFileName, 'r')
        self.fitness = fitnessFile.read()
        fitnessFile.close()

        os.remove(fitnessFileName)

    def Mutate(self):
        self.weights[rd.randint(0, 2)][rd.randint(0, 1)] = ((rd.random() * 2) - 1)

    def Create_World(self):
        pyrosim.Start_SDF("world.sdf")

        length, width, height = 1, 1, 1
        x, y, z = -5.5, 0.5, 0.5

        pyrosim.Send_Cube(name="Box", pos=[x, y, z], size=[width, length, height])
        pyrosim.End()


    def Create_Brain(self):
        pyrosim.Start_NeuralNetwork("brain" + str(self.solID) + ".nndf")

        pyrosim.Send_Sensor_Neuron(name=0, linkName="Torso")
        pyrosim.Send_Sensor_Neuron(name=1, linkName="BackLeg")
        pyrosim.Send_Sensor_Neuron(name=2, linkName="FrontLeg")
        pyrosim.Send_Motor_Neuron(name=3, jointName="Torso_BackLeg")
        pyrosim.Send_Motor_Neuron(name=4, jointName="Torso_FrontLeg")

        for currentRow in range(3):
            for currentColumn in range(2):
                pyrosim.Send_Synapse(sourceNeuronName = currentRow, targetNeuronName = currentColumn + 3, weight = self.weights[currentRow][currentColumn])

        pyrosim.End()

    def Create_Body(self):
        pyrosim.Start_URDF("body.urdf")

        length, width, height = 1, 1, 1
        x, y, z = 1.5, 0, 1.5

        pyrosim.Send_Cube(name="Torso", pos=[x, y, z], size=[width, length, height])
        pyrosim.Send_Joint(name="Torso_BackLeg", parent="Torso", child="BackLeg", type="revolute", position="1.0 0 1")
        pyrosim.Send_Cube(name="BackLeg", pos=[-0.5, 0, -0.5], size=[width, length, height])
        pyrosim.Send_Joint(name="Torso_FrontLeg", parent="Torso", child="FrontLeg", type="revolute", position="2 0 1")
        pyrosim.Send_Cube(name="FrontLeg", pos=[0.5, 0, -0.5], size=[width, length, height])

        pyrosim.End()

    def Set_ID(self, ID):
        self.myID = ID