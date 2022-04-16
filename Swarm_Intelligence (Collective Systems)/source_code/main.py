import sys
import pygame as py
import constants as c
from pygame.locals import *
from matplotlib.widgets import Slider, Button
from apple import Apple

def update(dt, apples):
    for event in py.event.get():
        if event.type == QUIT:
            py.quit()
            sys.exit(0)
            
    for b in apples:
        b.update(dt, apples)


def draw(screen, background, apples):
    apples.clear(screen, background)
    newScreen = apples.draw(screen)
    py.display.update(newScreen)

def add_apples(apples, num_apples):
    for _ in range(num_apples):
        apples.add(Apple())

def main():
    py.init()
    
    logo = py.image.load("pie.png")
    py.display.set_icon(logo)
    py.display.set_caption("Swarming Apples on Apple Pie Simulator")

    screen = py.display.set_mode((c.window_width, c.window_height))
    screen.set_alpha(None)
    background = py.Surface(screen.get_size()).convert()
    background.fill(c.bg_color)
    screen.fill(c.bg_color)

    fpsClock = py.time.Clock()
    py.event.set_allowed([py.QUIT])
    apples = py.sprite.RenderUpdates()
    add_apples(apples, c.num_apples)
    dt = 1/c.fps  

    while True:
        update(dt, apples)
        draw(screen, background, apples)
        dt = fpsClock.tick(c.fps)

main()

#---BUTTON-----
# xposition, yposition, width, height
# ax_co = plt.axes([1000, 100, 200, 50])
# ax_reset = plt.axes([1000, 800, 80, 50])
# resetButton = Button(ax=ax_reset, label="Reset", color='red', hovercolor='black')
# #place resetButton on screen where x=1100 and y=200
# cohesion_slider = Slider(ax=ax_co,label="Cohesion",valmin=0, valmax=10,valinit=5,orientation="horizontal")
# #resetButton.on_clicked(resetSlider)