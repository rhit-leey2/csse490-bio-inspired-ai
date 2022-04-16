
//import java.lang.Math;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Perceptron Class
 */
public class Perceptron {

    Double[] weights;
    double learningRate = 0.01;
    ArrayList<Double[]> weightHistory = new ArrayList<>();
    ArrayList<Double> accuracyHistory = new ArrayList<>();
    ArrayList<Integer[]> testingDataArr = new ArrayList<>();
    double accuracy;

    Perceptron(Integer num) {
        weights = new Double[num];

        for (Integer i = 0; i < weights.length; i++) {
            weights[i] = random(-1, 1);
        }
    }

    Perceptron(Double[] weightsArr) {
        weights = weightsArr;
    }

    public static void main(String[] args) {
        // CheckPoint 1
        // Integer[] input_1 = { 0, 0 };
        // Integer[] input_2 = { 0, 1 };
        // Integer[] input_3 = { 1, 0 };
        // Integer[] input_4 = { 1, 1 };

        // double[] ANDweights = { 0.6, 0.6, -1 }; // for AND logical operator
        // Perceptron andPer = new Perceptron(ANDweights);
        // System.out.println("Created a perceptron that solves logical AND");
        // System.out.println("Input: (0,0) / Output: " + andPer.feedforward(input_1));
        // System.out.println("Input: (0,1) / Output: " + andPer.feedforward(input_2));
        // System.out.println("Input: (1,0) / Output: " + andPer.feedforward(input_3));
        // System.out.println("Input: (1,1) / Output: " + andPer.feedforward(input_4));

        // double[] ORweights = { 1.2, 1.5, -1 }; // for OR logical operator
        // Perceptron orPer = new Perceptron(ORweights);
        // System.out.println("Created a perceptron that solves logical OR");
        // System.out.println("Input: (0,0) / Output: " + orPer.feedforward(input_1));
        // System.out.println("Input: (0,1) / Output: " + orPer.feedforward(input_2));
        // System.out.println("Input: (1,0) / Output: " + orPer.feedforward(input_3));
        // System.out.println("Input: (1,1) / Output: " + orPer.feedforward(input_4));

        // CheckPoint 2
        // Integer[] trainingSetA = { 0, 0, 1 };
        // Integer[] trainingSetB = { 0, 0, -1 };
        // Integer[] trainingSetC = { 1, 1, -1 };

        // Part A
        // System.out.println("Checkpoint 2: Part A");
        // System.out.println(
        // "Perceptron with w1:" + orPer.weights[0] + " w2:" + orPer.weights[1] + "
        // bias:" + orPer.weights[2]);
        // orPer.train(trainingSetA[0], trainingSetA[1], trainingSetA[2]);
        // System.out.println("Trained with data: " + "x=" + trainingSetA[0] + " / y=" +
        // trainingSetA[1] + " / answer="
        // + trainingSetA[2]);
        // System.out.println(
        // "Perceptron with w1:" + orPer.weights[0] + " w2:" + orPer.weights[1] + "
        // bias:" + orPer.weights[2]);
        // System.out.println("------------------------------------------");

        // Part B
        // System.out.println("Checkpoint 2: Part B");
        // System.out.println(
        // "Perceptron with w1:" + orPer.weights[0] + " w2:" + orPer.weights[1] + "
        // bias:" + orPer.weights[2]);
        // orPer.train(trainingSetB[0], trainingSetB[1], trainingSetB[2]);
        // System.out.println("Trained with data: " + "x=" + trainingSetB[0] + " / y=" +
        // trainingSetB[1] + " / answer="
        // + trainingSetB[2]);
        // System.out.println(
        // "Perceptron with w1:" + orPer.weights[0] + " w2:" + orPer.weights[1] + "
        // bias:" + orPer.weights[2]);
        // System.out.println("------------------------------------------");

        // Part C
        // System.out.println("Checkpoint 2: Part C");
        // System.out.println(
        // "Perceptron with w1:" + orPer.weights[0] + " w2:" + orPer.weights[1] + "
        // bias:" + orPer.weights[2]);
        // orPer.train(trainingSetC[0], trainingSetC[1], trainingSetC[2]);
        // System.out.println("Trained with data: " + "x=" + trainingSetC[0] + " / y=" +
        // trainingSetC[1] + " / answer="
        // + trainingSetC[2]);
        // System.out.println(
        // "Perceptron with w1:" + orPer.weights[0] + " w2:" + orPer.weights[1] + "
        // bias:" + orPer.weights[2]);
        // System.out.println("------------------------------------------");

        // Checkpoint 3
        Perceptron per = new Perceptron(3); // Randomly generate a perceptron
        per.generateTestingData();
        System.out.println("Accuracy Before Training: " + per.accuracyTest() * 100 + "%");

        // Train for every sample
        for (int i = 0; i < per.testingDataArr.size(); i++) {

            per.train(per.testingDataArr.get(i)[0], per.testingDataArr.get(i)[1], per.testingDataArr.get(i)[2]);

            // Solved deep copy problem
            Double[] weightsRecord = new Double[3];
            weightsRecord[0] = per.weights[0];
            weightsRecord[1] = per.weights[1];
            weightsRecord[2] = per.weights[2];
            per.weightHistory.add(weightsRecord);

            per.accuracyHistory.add(per.accuracyTest());
        }

        System.out.println("Accuracy After Training: " + per.accuracy * 100 + "%");
        per.writeCSVFile();
    }

    // Returns a double between min and max
    private double random(int min, int max) {
        Random r = new Random();
        double randomNum = min + (max - min) * r.nextDouble();
        return randomNum;
    }

    // Feedforward method
    public double feedforward(Integer[] inputs) {
        float sum = 0;

        for (int i = 0; i < weights.length - 1; i++) {
            sum += inputs[i] * weights[i];
        }
        return activateFunc(sum);
    }

    // Activation Function method which compute the output based on that sum passed
    // through an activation function
    public double activateFunc(float sum) {

        double output = 0;

        // Step Function
        if (sum > 0) {
            output = 1;
        } else {
            output = -1;
        }

        // Tanh Function
        // output = (Math.exp(sum) - Math.exp(-sum)) / (Math.exp(sum) + Math.exp(-sum));
        // if (output > 0) {
        // output = 1;
        // } else {
        // output = -1;
        // }
        return output;
    }

    // Trained based upon a supervised learning algorithm
    public void train(int x, int y, double desired) {

        Integer vals[] = { x, y };
        double inputs[] = { x, y, desired };
        double guess = feedforward(vals);

        // compute the error
        double error = desired - guess;

        // adjust the weights
        for (int i = 0; i < weights.length; i++) {
            weights[i] += error * inputs[i] * learningRate;
        }

    }// end of train method

    public int getRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public int getAnswer(int x, int y) {
        int answer = 0;
        if (x - y > 0) {
            answer = 1;
        } else if (x - y <= 0) {
            answer = -1;
        }

        // only for using tanh function as the activation function

        // only using for XOR
        // if ((x > 0 && y <= 0) || (x <= 0 && y > 0)) {
        // answer = 1;
        // } else {
        // answer = -1;
        // }

        return answer;
    }

    public void generateTestingData() {
        for (int i = 0; i < 1000; i++) {
            int first = getRandomInt(-100, 100);
            int second = getRandomInt(-100, 100);
            int a = getAnswer(first, second);
            Integer[] inputs = { first, second, a };
            testingDataArr.add(inputs);
        }
    }

    public Double accuracyTest() {
        int correctNum = 0;
        for (int i = 0; i < testingDataArr.size(); i++) {
            double compare = feedforward(testingDataArr.get(i));
            if (compare == testingDataArr.get(i)[2]) {
                correctNum++;
            }
        }
        accuracy = (double) correctNum / testingDataArr.size();
        return accuracy;
    }

    public void writeCSVFile() {

        PrintWriter csvWriter;
        File csvFile = new File("source_code/plot_data.csv");

        try {
            csvFile.createNewFile();
        } catch (IOException e) {
            System.err.println("Error! File not created");
        }

        try {
            csvWriter = new PrintWriter(csvFile);

            csvWriter.print("time,w1,w2,wb,acc\n");
            for (int i = 0; i < this.testingDataArr.size(); i++) {
                csvWriter.printf("%d,%f,%f,%f,%f\n", i, this.weightHistory.get(i)[0], this.weightHistory.get(i)[1],
                        this.weightHistory.get(i)[2], this.accuracyHistory.get(i));
            }

            System.out.println("Perceptron data successfully written to plot_data.csv");
            System.out.println();

            csvWriter.flush();
            csvWriter.close();

        } catch (FileNotFoundException e) {
            System.err.println("CSV file not found");
            // } catch (IndexOutOfBoundsException i) {
            // System.err.println("Error! Index out of bounds");
        }
    }

}
