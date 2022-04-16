import matplotlib.pyplot as plt
import csv
import seaborn as sea

# Installation instructions for matplotlib
# https://matplotlib.org/stable/users/installing.html

# Longer tutorial here
# https://docs.python.org/3/library/csv.html

data="/Users/lyralee/Documents/Github/fa21_hw4-perceptron-rhit-leey2/example/simple_plot_demo/data.csv"
time=[]
w1=[]
w2=[]
wb=[]
acc=[]

#sea.set_palette(sea.color_palette("hls", 8))
#sea.set_palette(sea.color_palette("rocket"))
sea.set_palette(palette="Spectral")
with open(data,'r') as csvfile:
    # Using the csv reader automatically places all values 
    # in columns within a row in a dictionary with a 
    # key based on the header (top line of the file)
    reader = csv.DictReader(csvfile)
    for row in reader:
        time.append( int(row["time"]) )
        w1.append( float(row["w1"] ) )
        w2.append( float(row["w2"] ) )
        wb.append( float(row["wb"] ) )
        acc.append( float(row["acc"] )  )
        
fig, ax1 = plt.subplots()

ax1.plot(time,w1,label="w1")
ax1.plot(time,w2,label="w2")
ax1.plot(time,wb,label="bias")


ax1.legend()
ax1.set_xlabel('time')
ax1.set_ylabel('weights')

#this is done in order to allow the accuracy to scale separately from the weights
#you could also producea second plot, but this is a bit more convenient
ax2 = ax1.twinx()  # instantiate a second axes that shares the same x-axis
ax2.set_ylabel('accuracy',color="tab:blue" )  # we already handled the x-label with ax1
ax2.plot(time, acc, label="accuracy",color="tab:blue")
ax2.tick_params(axis='y', labelcolor="tab:blue")
fig.tight_layout()  # otherwise the right y-label is slightly clipped
plt.savefig('/Users/lyralee/Documents/Github/fa21_hw4-perceptron-rhit-leey2/example/simple_plot_demo/plot.png')
plt.show()