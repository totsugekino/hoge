import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class J2901 {

	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.print("数値を入力してください:");
			String line = reader.readLine();
			int n = Integer.parseInt(line);
			if(n%2==0) {
				System.out.println(n + "は偶数です");
			} else {
				System.out.println(n + "は奇数です");
			}
		} catch(IOException e) {
			System.out.println(e);
		} catch(NumberFormatException e) {
			System.out.println("入力が正しくありません");
	    }
	}

}
