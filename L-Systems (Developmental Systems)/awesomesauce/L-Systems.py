import turtle

screen = turtle.Screen()
t = turtle.Turtle()
t.pensize(2)
t.penup()
t.goto(-15, -300)
t.setheading(90)
t.pendown()


class Turtle:

    def __init__(self):
        self.angle = 0
        self.dist = 0
        self.speed = 100
        self.result = ""
        self.axiom = ""
        self.productionRules = {'': ""}
        self.LSystemHistory = []

    def iterateWordFunc(self, ran, axiom, rule_key1, rule_value1, rule_key2, rule_value2, rule_key3, rule_value3, rule_key4, rule_value4):
        self.result = axiom
        for x in range(ran+1):
            self.generateGeneralizedWordFunc(
                rule_key1, rule_value1, rule_key2, rule_value2, rule_key3, rule_value3, rule_key4, rule_value4)

    def iterateWordFunc_long(self, ran, axiom, rule_key1, rule_value1, rule_key2, rule_value2, rule_key3, rule_value3, 
    rule_key4, rule_value4, rule_key5, rule_value5, rule_key6, rule_value6):
        self.result = axiom
        for x in range(ran+1):
            self.generateGeneralizedWordFunc_long(
                rule_key1, rule_value1, rule_key2, rule_value2, rule_key3, rule_value3, rule_key4, rule_value4, rule_key5, rule_value5, rule_key6, rule_value6)

    # last iterated wordfunc is in the global variable result
    def generateGeneralizedWordFunc(self, rule_key1, rule_value1, rule_key2, rule_value2, rule_key3, rule_value3, rule_key4, rule_value4):
        next = ""
        current = self.result
        for c in current:
            if (c == rule_key1):
                next += rule_value1
            elif (c == rule_key2):
                next += rule_value2
            elif (c == rule_key3):
                next += rule_value3
            elif (c == rule_key4):
                next += rule_value4
            else:
                next += c
            current = next

        self.result = current

    def generateGeneralizedWordFunc_long(self, rule_key1, rule_value1, rule_key2, rule_value2, rule_key3, rule_value3, rule_key4, rule_value4, rule_key5, rule_value5, rule_key6, rule_value6):
            next = ""
            current = self.result
            for c in current:
                if (c == rule_key1):
                    next += rule_value1
                elif (c == rule_key2):
                    next += rule_value2
                elif (c == rule_key3):
                    next += rule_value3
                elif (c == rule_key4):
                    next += rule_value4
                elif (c == rule_key5):
                    next += rule_value5
                elif (c == rule_key6):
                    next += rule_value6
                else:
                    next += c
                current = next

            self.result = current

    def drawLSystem(self, constant, angle, dist, str):
        stack = []
        for ch in str:
            if(ch == constant or ch == ' '):
                # move pen forward by certain distance
                t.speed(self.speed)
                t.forward(dist)

            elif(ch == '-'):
                # rotate the pen left by a 90 degrees
                t.left(angle)

            elif(ch == '+'):
                # rotate the pen right by 90 degrees
                t.right(angle)

            elif(ch == '['):
                # push the current state of the turtle onto a stack of states
                stack.append((t.heading(), t.xcor(), t.ycor()))

            elif(ch == ']'):
                # pop the state of the turtle and set position and heading
                t.setheading(stack[-1][0])
                t.goto(stack[-1][1], stack[-1][2])
                stack.pop()
                t.pendown()

    def drawLSystem_long(self, angle1, angle2, dist, str):
        stack = []
        for ch in str:
            if(ch == 'F'):
                t.forward(10)
                t.pencolor("brown")

            elif(ch == 'G'):
                t.forward(5)
                t.pencolor("green")

            elif(ch == 'M'):
                t.forward(2)
                t.pencolor("green")

            elif(ch == 'Y'):
                t.circle(1)
                t.pencolor("yellow")

            elif(ch == 'P'):
                t.circle(2)
                t.pencolor("brown")

            elif(ch == '-'):
                # rotate the pen left by a 90 degrees
                t.left(angle1)

            elif(ch == '+'):
                # rotate the pen right by 90 degrees
                t.right(angle1)
            
            elif(ch == '/'):
                t.left(angle2)

            elif(ch == '\\'):
                t.right(angle2)

            elif(ch == '['):
                # push the current state of the turtle onto a stack of states
                stack.append((t.heading(), t.xcor(), t.ycor()))

            elif(ch == ']'):
                # pop the state of the turtle and set position and heading
                t.setheading(stack[-1][0])
                t.goto(stack[-1][1], stack[-1][2])
                stack.pop()
                t.pendown()

    def drawRoundStar(self):
        screen.title("Drawing Round Star")
        screen.bgcolor("#1e0299")
        t.pencolor("white")

        # position the roundStar
        t.penup()
        t.goto(-75, -300)
        t.setheading(90)
        t.pendown()
        # range, axiom, rule_key1, rule_value1, rule_key2, rule_value2
        self.iterateWordFunc(6, "F", "F", "F++F", "", "", "", "", "", "")
        # constant, angle, dist, str
        self.drawLSystem("F", 77, 600, self.result)

    def drawPentadendrite(self):
        screen.title("Drawing Pentadendrite")
        screen.bgcolor("purple")
        t.pencolor("yellow")

        # position the roundStar
        t.penup()
        t.goto(280, 50) #230, 10, 12
        t.setheading(90)
        t.pendown()

        # range, axiom, rule_key1, rule_value1, rule_key2, rule_value2
        self.iterateWordFunc(3, "F-F-F-F-F", "F", "F-F-F++F+F-F", "", "", "", "", "", "")
        self.drawLSystem("F", 72, 5, self.result)  # constant, angle, dist, str

    def drawTree(self):
        # range, axiom, rule_keys, rule_value
        self.iterateWordFunc_long(6, "S", "S", "FB", "B", "F[+BLMR]F[-BMR]F[+BL][-BLMR]", "F",
                                  "FN", "N", "F", "L", "[G-G][-G+G]--[G-G][-G+G]++++[G-G][-G+G]--", "R", "Y[MP][/MP][\MP]")
        # constant, angle, dist, str
        self.drawLSystem_long(25, 22.5, 36, self.result)

    # draw variety of L-Systems
    def drawForest(self):
        screen.title("Drawing Forest")
        t.penup()
        t.goto(-200, -380)
        t.setheading(90)
        t.pendown()

        # rotate angle of 20 the L-system iterating 6 times
        # range, axiom, rule_key1, rule_value1, rule_key2, rule_value2
        self.iterateWordFunc(5, "A", "A", "A[+A][-A]", "", "")
        # constant, angle, dist, str
        self.drawLSystem("A", 25, 60, self.result)

        t.penup()
        t.goto(-40, -320)
        t.setheading(90)
        t.pendown()

        # draw L-System with angle of 14, iterating 5 times
        # range, axiom, rule_key1, rule_value1, rule_key2, rule_value2
        self.iterateWordFunc(6, "B", "F", "FF", "B", "F[-B] [+ B]")
        self.drawLSystem("F", 30, 5, self.result)  # constant, angle, dist, str
        t.penup()
        t.goto(215, -350)
        t.setheading(90)
        t.pendown()

        # draw L-System with angle of 20 iterating 6 times
        # self.iterateWordFunc(3,"X", "F", "FF","X","F-[[X]+X]+F[+FX]-X") #range, axiom, rule_key1, rule_value1, rule_key2, rule_value2
        self.iterateWordFunc(5, "B", "F", "FF", "B", "F[ + B][ - B]")
        # constant, angle, dist, str
        self.drawLSystem("X", 20, 15, self.result)


# def main(self):
lsys = Turtle()
# lsys.drawForest()
# lsys.drawPlant()
#lsys.drawRoundStar()
#lsys.drawPentadendrite()
lsys.drawTree()

# save this to a png file
screen.exitonclick()
turtle.done
