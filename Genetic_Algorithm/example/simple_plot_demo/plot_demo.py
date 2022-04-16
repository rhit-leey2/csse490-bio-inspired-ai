import matplotlib.pyplot as plt
import csv

# Installation instructions for matplotlib
# https://matplotlib.org/stable/users/installing.html

# Longer tutorial here
# https://docs.python.org/3/library/csv.html

data="/Users/lyralee/Documents/Github/fa21_hw1-genetic-algorithm-rhit-leey2/example/simple_plot_demo/plot.csv"
generations=[]
best=[]
average=[]
mins=[]

with open(data, 'r') as csvfile:
    # Using the csv reader automatically places all values 
    # in columns within a row in a dictionary with a 
    # key based on the header (top line of the file)
    reader = csv.DictReader(csvfile)
    for row in reader:
        generations.append( row["generation"] )
        best.append( int(row["max"] ) )
        average.append( int(row["average"] ))
        mins.append( int(row["min"] ))
        

plt.plot(generations,best,label="max")
plt.plot(generations,average,label="average")
plt.plot(generations,mins,label="min")

plt.legend()
plt.xlabel('Generations')
plt.ylabel('Fitness')


plt.savefig('plot.png')
plt.show()