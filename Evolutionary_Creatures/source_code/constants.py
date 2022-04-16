import numpy as np

iternum = 2000
genNum = 10
populationSize = 10

sleepTime = 1/240

amplitude, frequency, offset = np.pi, 5, 0 

amplitude_front, frequency_front, phaseOffset_front = -np.pi/4, 10, 0
amplitude_back, frequency_back, phaseOffset_back = -np.pi/4, 10, np.pi/4 

maxForce_front,maxForce_back = 500, 500