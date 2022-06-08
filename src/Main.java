import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        ProcessInput in = new ProcessInput();
        do {
            String input = sc.nextLine();
            in.inputProcessor(input);
        } while (sc.hasNextLine());
        sc.close();
    }

}

class ProcessInput{
    IData material;
    IData roman;
    ProcessOutput out;
    public final String err = "I have no idea what you are talking about";
    public void inputProcessor(String data){
        String[] in = data.split("\\s+");

        if (in.length == 3 && in[1].equalsIgnoreCase("is")){
            if (roman == null)
                roman = new Roman();
            roman.parse(in);
        }else if (data. toLowerCase().endsWith("credits")) {
            if (material == null)
                material = new Materials();
            material.parse(in);
        }else if (data.toLowerCase().endsWith("?")){
            if (out == null){
                out = new ProcessOutput();
            }
            if (data.toLowerCase().startsWith("how much is")){
                if ((!out.howMuch(in))){
                    System.out.println(err);
                }
            }else if (data.toLowerCase().startsWith("how many credits")){
                if (!(out.howMany(in))){
                    System.out.println(err);
                }
            }else {
                System.out.println(err);
            }
        }else{
            System.out.println("\n"+err);
        }
    }
}

interface IData{
    void parse(String[] in);
}

class Roman implements IData{
    public static Map<String,String> romanTrans = new HashMap<>();
    @Override
    public void parse(String[] in) {
        romanTrans.put(in[0],in[2]);
    }
    public int getValue(String[] in,int start, int end){
        int sum = 0;
        for (int i = start; i <= end; i++){
            char nextChar = ' ';
            char rChar = romanTrans.get(in[i]).charAt(0);
            if (i != end){
                nextChar = romanTrans.get(in[i+1]).charAt(0);
            }
            switch (rChar) {
                case 'M':
                    sum = sum + 1000;
                    break;

                case 'D':
                    sum = sum + 500;
                    break;

                case 'C':
                    if (nextChar == 'M' || nextChar == 'D')
                        sum = sum - 100;
                    else
                        sum = sum + 100;
                    break;

                case 'L':
                    sum = sum + 50;
                    break;

                case 'X':
                    if (nextChar == 'C' || nextChar == 'L')
                        sum = sum - 10;
                    else
                        sum = sum + 10;
                    break;

                case 'V':
                    sum = sum + 5;
                    break;

                case 'I':
                    if (nextChar == 'X' || nextChar == 'V')
                        sum = sum - 1;
                    else
                        sum = sum + 1;
                    break;
            }
        }
        return sum;
    }
}

class Materials implements IData{
    public static Map<String,Double> materialsValue = new HashMap<>();
    @Override
    public void parse(String[] in) {
        int len = in.length;
        double value = Double.parseDouble(in[len-2]);
        Roman roman = new Roman();
        int qty = roman.getValue(in,0,len-5);
        materialsValue.put(in[len-4],value/qty);
    }
    public double getValue(String str){
        if (materialsValue.containsKey(str)){
            return materialsValue.get(str);
        }
        else
            return -1;
    }
}

class ProcessOutput{
    Roman roman = new Roman();
    public boolean howMuch(String[] in){
        int val = roman.getValue(in,3,in.length - 2);

        StringBuffer sb =new StringBuffer();
        for (int i = 3;i < in.length - 1; i++){
            sb.append(in[i]).append(" ");
        }
        sb.append("is ").append(val);
        System.out.println(sb);
        return true;
    }
    public boolean howMany(String[] in){
        int qty = roman.getValue(in,4,in.length - 3);
        StringBuffer sb =new StringBuffer();
        for (int i = 4;i < in.length - 1; i++){
            sb.append(in[i]).append(" ");
        }
        Materials materials = new Materials();
        double val = materials.getValue(in[in.length-2]);
        if (val <= 0){
            return false;
        }
        sb.append("is ").append((qty*val)).append(" Credits");
        System.out.println(sb);
        return true;
    }
}
