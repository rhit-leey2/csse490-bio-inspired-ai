import pygame as py
import constants as c

class Pie(py.sprite.Sprite):
    image = py.Surface((20, 20), py.SRCALPHA)
    py.draw.circle(image, c.apples_color, (20, 20), 20)
    def __init__(self, position, velocity, wr):
        super().__init__()
        self.heading = 0.0
        self.position = py.Vector2(position)
        self.velocity = py.Vector2(velocity)
        self.acceleration = py.Vector2(0, 0)
        self.wr = wr
        self.rect = self.image.get_rect(center=self.position)

    def update(self, dt, steering):
        self.acceleration = steering * dt
        _, prevHeading = self.velocity.as_polar()
        new_velocity = self.velocity + self.acceleration * dt
        speed, newHeading = new_velocity.as_polar()

        heading_diff = 180 - (180 - newHeading + prevHeading) % 360
        if abs(heading_diff) > c.max_turn:
            if heading_diff > c.max_turn:
                newHeading = prevHeading + c.max_turn
            else:
                newHeading = prevHeading - c.max_turn

        self.velocity.from_polar((speed, newHeading))
        speed, self.heading = self.velocity.as_polar()
        
        if speed < c.min_speed:
            self.velocity.scale_to_length(c.min_speed)

        if speed > c.max_speed:
            self.velocity.scale_to_length(c.max_speed)

        self.position += self.velocity * dt

        if self.wr:
            self.wrap()

        self.image = py.transform.rotate(Pie.image, -self.heading)
        self.rect = self.image.get_rect(center=self.position)

    def avoid_edge(self):
        left = self.edges[0] - self.position.x
        up = self.edges[1] - self.position.y
        right = self.position.x - self.edges[2]
        down = self.position.y - self.edges[3]

        scale = max(left, up, right, down)

        if scale > 0:
            center = (c.window_width / 2, c.window_height / 2)
            steering = py.Vector2(center)
            steering -= self.position
        else:
            steering = py.Vector2()

        return steering

    def wrap(self):
        if self.position.x < 0:
            self.position.x += c.window_width
        elif self.position.x > c.window_width:
            self.position.x -= c.window_width

        if self.position.y < 0:
            self.position.y += c.window_height
        elif self.position.y > c.window_height:
            self.position.y -= c.window_height

    def set_boundary():
        margin_w = c.window_width * c.edge_distance / 100
        margin_h = c.window_height * c.edge_distance / 100
        Pie.edges = [margin_w, margin_h, c.window_width - margin_w,c.window_height - margin_h]

    def clustering_force(self, force):
        if 0 < force.magnitude() > c.max_force:
            force.scale_to_length(c.max_force)

        return force
