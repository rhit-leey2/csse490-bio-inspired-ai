import pybullet as p
import pybullet_data
from world import WORLD
from robot import ROBOT
import constants as c
import time


class SIMULATION:

    def __init__(self, directOrGUI, solutionID):

        self.directOrGUI = directOrGUI
        self.solutionID = solutionID

        if self.directOrGUI == "DIRECT":
            self.physicsClient = p.connect(p.DIRECT) 
        elif self.directOrGUI == "GUI":
            self.physicsClient = p.connect(p.GUI)

        p.setAdditionalSearchPath(pybullet_data.getDataPath())
        p.setGravity(0, 0, -9.8)

        self.world = WORLD()
        self.robot = ROBOT(self.solutionID)


    def __del__(self):
        for sensor in self.robot.sensors.values():
            sensor.Save_Values()

        p.disconnect()


    def Run(self):

        for timestep in range(c.iternum):

            if self.directOrGUI == "GUI":
                time.sleep(c.sleepTime)
        
            p.stepSimulation()

            self.robot.Sense(timestep)
            self.robot.Think()
            self.robot.Act()
    

    def Get_Fitness(self):
        self.robot.Get_Fitness(self.solutionID)
