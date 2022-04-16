import pygame as py
import constants as c
from random import uniform
from pie import Pie

class Apple(Pie):
    
    def __init__(self):
        Apple.set_boundary()
        self.wr = False
        
        start_position = py.math.Vector2(
            uniform(0, c.window_width),
            uniform(0, c.window_height))
        start_velocity = py.math.Vector2(
            uniform(-1, 1) * c.min_speed,
            uniform(-1, 1) * c.max_speed)

        super().__init__(start_position, start_velocity, self.wr)

        self.rect = self.image.get_rect(center=self.position)

    
    def cohesion(self, apples):
        steering = py.Vector2()
        for apple in apples:
            steering += apple.position
        steering /= len(apples)
        steering -= self.position
        steering = self.clustering_force(steering)
        return steering / 100
    
    def alignment(self, apples):
        steering = py.Vector2()
        for apple in apples:
            steering += apple.velocity
        steering /= len(apples)
        steering -= self.velocity
        steering = self.clustering_force(steering)
        return steering / 8

    def separation(self, apples):
        steering = py.Vector2()
        for apple in apples:
            dist = self.position.distance_to(apple.position)
            
            separation_velocity_change = [0, 0]
            if dist < c.min_distance:
                separation_velocity_change[0] += self.rect.x - apple.rect.x
                separation_velocity_change[1] += self.rect.y - apple.rect.y
                # newPosX = separation_velocity_change[0] + self.rect.position[0] - apple.rect.position[0]
                # newPosY = separation_velocity_change[1] + self.rect.position[1] - apple.rect.position[1]
                # self.rect.positon = (newPosX, newPosY)
        newVelX = separation_velocity_change[0] / c.separation_weight
        newVelY = separation_velocity_change[1] / c.separation_weight
        self.velocity += (newVelX,newVelY)
        
        steering = self.clustering_force(steering)
        return steering

    def goal(self, mouse_x, mouse_y):
        self.velocity += ((mouse_x - self.rect.x) / c.goal_weight,((mouse_y - self.rect.y) / c.goal_weight))
    
    def update(self, dt, apples):
        steering = py.Vector2()

        if not self.wr:
            steering += self.avoid_edge()
        neighbors = self.find_neighbors(apples)
        
        if neighbors:
            separation = self.separation(neighbors)
            alignment = self.alignment(neighbors)
            cohesion = self.cohesion(neighbors)
            steering += separation + alignment + cohesion
        
        pos = py.mouse.get_pos()
        mouse_x = pos[0]
        mouse_y = pos[1]
        if c.isGoalEnabled:
            self.goal(mouse_x, mouse_y)
        super().update(dt, steering)

    def find_neighbors(self, apples):
        neighbors = []
        for apple in apples:
            if apple != self:
                dis = self.position.distance_to(apple.position)
                if dis < c.visual_range:
                    neighbors.append(apple)
                    
        return neighbors
