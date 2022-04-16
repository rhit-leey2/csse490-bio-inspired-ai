import pybullet as p
import constants as c
import os
import pyrosim.pyrosim as pyrosim
from sensor import SENSOR
from motor import MOTOR
from pyrosim.neuralNetwork import NEURAL_NETWORK


class ROBOT:
    def __init__(self, solutionID):
        self.robotID = p.loadURDF("body.urdf")
        pyrosim.Prepare_To_Simulate("body.urdf")
        self.Prepare_To_Sense()
        self.Prepare_To_Act()
        self.nn = NEURAL_NETWORK("brain" + solutionID + ".nndf")
        os.system("rm brain" + solutionID + ".nndf")

    def Prepare_To_Sense(self):
        self.sensors = {}
        for linkName in pyrosim.linkNamesToIndices:
            self.sensors[linkName] = SENSOR(linkName)

    def Sense(self, timestep):
        for sensor in self.sensors.values():
            sensor.Get_Value(timestep)

    def Prepare_To_Act(self):
        self.motors = {}
        for jointName in pyrosim.jointNamesToIndices:
            self.motors[jointName] = MOTOR(jointName)

    def Act(self):
        for neuronName in self.nn.Get_Neuron_Names():
            if self.nn.Is_Motor_Neuron(neuronName):
                jointName = self.nn.Get_Motor_Neurons_Joint(neuronName)
                desiredAngle = self.nn.Get_Value_Of(neuronName)
                self.motors[jointName].Set_Value(desiredAngle, self.robotID)

    def Think(self):
        self.nn.Update()
        #self.nn.Print()

    def Get_Fitness(self, solutionID):
        stateOfLinkZero = p.getLinkState(self.robotID, 0)

        positionOfLinkZero = stateOfLinkZero[0]
        xCoordinateOfLinkZero = positionOfLinkZero[0]

        tempFileName = "temp" + str(solutionID) + ".txt"
        fitnessFileName = "fitness" + str(solutionID) + ".txt"

        fitnessFile = open(tempFileName, 'w')
        fitnessFile.write(str(xCoordinateOfLinkZero))
        fitnessFile.close()

        os.rename(tempFileName, fitnessFileName)

        exit()