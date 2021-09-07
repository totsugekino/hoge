import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class J2902 {

	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.print("数値を入力してください:");
			String line = reader.readLine();
			int n = Integer.parseInt(line);
			boolean flag = true;
			for(int i=2; i<n; i++) {
				if(n%i==0) {
					flag = false;
				}
			}
			if(flag) {
				System.out.println(n + "は素数です");
			} else {
				System.out.println(n + "は素数ではありません");
			}
		} catch(IOException e) {
			System.out.println(e);
		} catch(NumberFormatException e) {
			System.out.println("入力が正しくありません");
	    }
	}

}
