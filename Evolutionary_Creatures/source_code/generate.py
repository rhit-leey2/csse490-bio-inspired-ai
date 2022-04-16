import pyrosim.pyrosim as pyrosim
import random as rd

# def Create_World():
#     #tell pyrosim where to store information about the world you'd like to create
#     pyrosim.Start_SDF("world.sdf")

#     #define the size and position of the box
#     length, width, height = 1, 1, 1
#     x, y, z = 2, 2, 0.5

#     #stores a box with length, width and height all equal to 1 meter, in box.sdf
#     pyrosim.Send_Cube(name="Box", pos=[x, y, z] , size=[width, length, height])

#     pyrosim.End()

def Generate_Brain():
    pyrosim.Start_NeuralNetwork("brain.nndf")

    pyrosim.Send_Sensor_Neuron(name = 0 , linkName = "Torso")
    pyrosim.Send_Sensor_Neuron(name = 1 , linkName = "BackLeg")
    pyrosim.Send_Sensor_Neuron(name = 2 , linkName = "FrontLeg")
    pyrosim.Send_Motor_Neuron( name = 3 , jointName = "Torso_BackLeg")
    pyrosim.Send_Motor_Neuron( name = 4 , jointName = "Torso_FrontLeg")

    for i in range(3):
        for j in range(3, 5):
            pyrosim.Send_Synapse(sourceNeuronName = i, targetNeuronName = j, weight = (rd.random() - 0.5) * 2)

    pyrosim.End()

def Generate_Body():
    pyrosim.Start_URDF("body.urdf")

    #define the size and position of the box
    length, width, height = 1, 1, 1
    x, y, z = 1.5, 0, 1.5

     #stores a box with length, width and height all equal to 1 meter, in box.sdf
    pyrosim.Send_Cube(name="Torso", pos=[x, y, z] , size=[width, length, height])
    pyrosim.Send_Joint(name = "Torso_BackLeg" , parent= "Torso" , child = "BackLeg" , type = "revolute", position = "1.0 0 1")
    pyrosim.Send_Cube(name="BackLeg", pos=[-0.5, 0, -0.5] , size=[width, length, height])
    pyrosim.Send_Joint(name = "Torso_FrontLeg" , parent= "Torso" , child = "FrontLeg" , type = "revolute", position = "2 0 1")
    pyrosim.Send_Cube(name="FrontLeg", pos=[0.5, 0, -0.5] , size=[width, length, height])

    pyrosim.End()


# Create_World()
Generate_Body()
Generate_Brain()