import pygame as py
window_width = 1200
window_height = 800

fps = 65.0
num_apples = 80

bg_color = py.color.Color("#f9c799")
apples_color = py.color.Color("#ed5565")

min_speed = .02
max_speed = .2
max_force = 1
max_turn = 5
min_distance = 10
separation_weight = 100
turn_factor = 8
swarming_factor = 15
edge_distance = 5
visual_range = 60
goal_weight = 20
isGoalEnabled = True