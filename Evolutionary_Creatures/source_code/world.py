import pybullet as p
import pybullet_data
import time
import numpy as np
import random as rd
import constants as c
import pyrosim.pyrosim as pyrosim

class WORLD:
    def __init__(self):
        # add a floor
        self.planeId = p.loadURDF("plane.urdf")

        #import box.sdf
        p.loadSDF("world.sdf")