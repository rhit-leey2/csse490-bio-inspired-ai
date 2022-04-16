import java.util.ArrayList;
import java.util.HashMap;

public class LSystems {

    String axiom;
    String result;
    HashMap<String, String> productionRules = new HashMap<String, String>();
    ArrayList<String> LSystemHistory = new ArrayList<>();

    public static void main(String[] args) {
        LSystems ls = new LSystems();
        ls.checkPoint1();
    }

    public void checkPoint1() {
        this.axiom = "FX";
        this.productionRules.put("X", "X+YF");
        this.productionRules.put("Y", "FX-Y");
        this.result = this.axiom;

        System.out.println("----------------------- CheckPoint 1 -----------------------");
        for (int num = 1; num <= 10; num++) {
            System.out.println("Generation " + num + " : " + generateWordFunction(result));
        }
    }

    public String generateWordFunction(String current) {
        StringBuffer next = new StringBuffer();
        for (int i = 0; i < current.length(); i++) {
            char c = current.charAt(i);
            if (c == 'X') {
                next.append("X+YF");
            } else if (c == 'Y') {
                next.append("FX-Y");
            } else {
                next.append(c);
            }
        }
        current = next.toString();
        this.result = current;
        LSystemHistory.add(current);
        return current;
    }

}