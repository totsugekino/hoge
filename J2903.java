import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class J2903 {

	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.print("数値を入力してください:");
			String line = reader.readLine();
			int n = Integer.parseInt(line);
			System.out.println(n + "の階乗は" + factorial(n) + "です");
		} catch(IOException e) {
			System.out.println(e);
		} catch(NumberFormatException e) {
			System.out.println("数値を入力してください");
	    }
	}

	public static int factorial(int n) {
		if(n<=1) {
			return 1;
		} else {
			return n*factorial(n-1);
		}
	}
}
