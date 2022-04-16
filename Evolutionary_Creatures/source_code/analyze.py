import numpy as np
import matplotlib.pyplot as plt

# Now use numpy.load() to load data/backLegSensorValues.npy into the vector backLegSensorValue
# backLegSensorValue = np.load('data/backLegSensorValues.npy')
# frontLegSensorValue = np.load('data/frontLegSensorValues.npy')
backLegMotorValue = np.load('data/backLegMotorValues.npy')
frontLegMotorValue = np.load('data/frontLegMotorValues.npy')
# targetAngles = np.load('data/targetAngles.npy')

# Increase the width until both trajectories are easily visible with line width
# plt.plot(backLegSensorValue, label='Back Leg', linewidth=3)
# plt.plot(frontLegSensorValue, label='Front Leg',linewidth=2)
plt.plot(backLegMotorValue, label='Back Leg Motor', linewidth=5)
plt.plot(frontLegMotorValue, label='Front Leg Motor', linewidth=1.5)
#plt.plot(targetAngles, label='Target Angles', linewidth=1.5)
plt.legend()
plt.show()
