import pybullet as p
import constants as c
import numpy as np
import pyrosim.pyrosim as pyrosim

class MOTOR:

    def __init__(self, jointName):
        self.jointName = jointName

    def Set_Value(self, desiredAngle, robotID):

        pyrosim.Set_Motor_For_Joint(
            bodyIndex = robotID, 
            jointName = self.jointName,
            controlMode = p.POSITION_CONTROL,
            targetPosition = desiredAngle, 
            maxForce = c.maxForce_front)