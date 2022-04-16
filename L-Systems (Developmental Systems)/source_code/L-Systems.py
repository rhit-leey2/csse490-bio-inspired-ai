import turtle

screen = turtle.Screen()
t = turtle.Turtle()
t.pensize(2)
t.color("green")
t.penup()
t.goto(0,-400)
t.setheading(90)
t.pendown()


class Turtle:

    def __init__(self):
        self.angle = 0
        self.dist = 0
        self.speed = 0
        self.branchNum = 2
        self.result = ""
        self.axiom = ""
        self.productionRules = {'': ""}
        self.LSystemHistory = []

    def checkPoint1(self):
        self.axiom = "FX"
        self.result = self.axiom
        self.productionRules = {'X': "X+YF", 'Y': "FX-Y"}
        for i in range(1, 6):
            print("Generation " + str(i) + " : " +
                  self.generateWordFunc(self.result))

    def checkPoint2a(self):
        self.axiom = "FX"
        self.result = self.axiom
        for i in range(1, 11):
            self.generateWordFunc(self.result)

    def checkPoint2b(self):
        self.axiom = "F++F++F"
        self.angle = 60
        self.result = self.axiom
        self.productionRules = {'F': "F-G+F+G-F", 'G': "GG"}
        for i in range(1, 5):
            self.generateWordFunc2(self.result)

    def checkPoint3a(self):
        self.axiom = "F"
        self.result = self.axiom
        self.branchNum = 1
        self.productionRules = {'F': 'F[+F][-F]F'}
        for i in range(1, 6):
            print("Generation " + str(i) + " : " +
                  self.generateWordFuncWithBranches(self.result))

    def checkPoint3b(self):
        self.axiom = "B"
        self.result = self.axiom
        self.branchNum = 2
        self.productionRules = {'B': 'F[-B] [+ B]', 'F': 'FF'}
        for i in range(1, 3):
            print("Generation " + str(i) + " : " +
                  self.generateWordFuncWithBranches(self.result))

    def generateWordFunc(self, current):
        next = ""
        for c in current:
            if (c == 'X'):
                next += "X+YF"
            elif (c == 'Y'):
                next += "FX-Y"
            else:
                next += c

        current = next
        self.result = current
        self.LSystemHistory.append(current)
        return current

    def generateWordFunc2(self, current):
        next = ""
        for c in current:
            if (c == 'F'):
                next += "F-F++F-F"
            elif (c == 'X'):
                next += "FF"
            else:
                next += c

        current = next
        self.result = current
        self.LSystemHistory.append(current)
        return current

    def generateWordFuncWithBranches(self, current):
        next = ""
        for c in current:
            if (c == 'F'):
                if(self.branchNum == 1):
                    next += "F[+F][-F]F"
                elif(self.branchNum == 2):
                    next += "FF"
            elif (c == 'B'):
                next += "F[-B] [+ B]"
            else:
                next += c

        current = next
        self.result = current
        self.LSystemHistory.append(current)
        return current

    def drawDragonCurve(self, str):
        screen.title("Dragon Curve")

        for ch in str:
            if(ch == 'F'):
                # move pen forward by certain distance
                t.speed(self.speed)
                t.forward(self.dist)

            elif(ch == '-'):
                # rotate the pen left by a 90 degrees
                t.left(self.angle)

            elif(ch == '+'):
                # rotate the pen right by 90 degrees
                t.right(self.angle)

    def drawKochSnowflake(self, str):
        screen.title("Koch Snowflake")
        for ch in str:
            if(ch == 'F' or ch == 'X'):
                # move pen forward by certain distance
                t.speed(self.speed)
                t.forward(self.dist)

            elif(ch == '-'):
                # rotate the pen left by a 90 degrees
                t.left(self.angle)

            elif(ch == '+'):
                # rotate the pen right by 90 degrees
                t.right(self.angle)

    def drawLSystem(self, constant, angle, dist, str):
        screen.title("Drawing Plants")
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

    def iterateWordFunc(self, ran, axiom, rule_key1, rule_value1, rule_key2, rule_value2):
        self.result = axiom
        for x in range(ran+1):
         self.generateGeneralizedWordFunc(rule_key1, rule_value1, rule_key2, rule_value2)

    # last iterated wordfunc is in the global variable result
    def generateGeneralizedWordFunc(self, rule_key1, rule_value1, rule_key2, rule_value2):
        next = ""
        current = self.result
        for c in current:
            if (c == rule_key1):
                next += rule_value1                
            elif (c == rule_key2):
                next += rule_value2
            else:
                next += c
            current = next

        self.result = current

    # draw variety of L-Systems
    def drawForest(self):
        screen.title("Drawing Forest")
        t.penup()
        t.goto(-200, -380)
        t.setheading(90)
        t.pendown()

        #rotate angle of 20 the L-system iterating 6 times
        self.iterateWordFunc(5,"A", "A", "A[+A][-A]","","") #range, axiom, rule_key1, rule_value1, rule_key2, rule_value2
        self.drawLSystem("A", 25, 60, self.result) # constant, angle, dist, str

        t.penup()
        t.goto(-40, -320)
        t.setheading(90)
        t.pendown()

        # draw L-System with angle of 14, iterating 5 times
        self.iterateWordFunc(6,"B", "F", "FF","B","F[-B] [+ B]") #range, axiom, rule_key1, rule_value1, rule_key2, rule_value2
        self.drawLSystem("F", 30, 5, self.result) # constant, angle, dist, str
        t.penup()
        t.goto(215, -350)
        t.setheading(90)
        t.pendown()

        # draw L-System with angle of 20 iterating 6 times
        #self.iterateWordFunc(3,"X", "F", "FF","X","F-[[X]+X]+F[+FX]-X") #range, axiom, rule_key1, rule_value1, rule_key2, rule_value2
        self.iterateWordFunc(5,"B", "F", "FF","B","F[ + B][ - B]")
        self.drawLSystem("X", 20, 15, self.result) # constant, angle, dist, str



# def main(self):
lsys = Turtle()
# lsys.checkPoint2a()
# lsys.drawDragonCurve(last)
# lsys.checkPoint2b()
# lsys.checkPoint3a()


#save the screen to a png file
#screen.getcanvas().postscript(file="LSystem.png")

#lsys.drawForest()
lsys.draw
# save this to a png file
screen.exitonclick()
turtle.done
