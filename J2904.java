import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class J2904 {

	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.print("1つめの数値を入力してください:");
			String line = reader.readLine();
			int a = Integer.parseInt(line);

			System.out.print("2つめの数値を入力してください:");
			line = reader.readLine();
			int b = Integer.parseInt(line);

			int m, n;

			if(a > b) {
				m = a;
				n = b;
			} else {
				m = b;
				n = a;
			}

			int r = m % n;
			while(r != 0) {
				m = n;
				n = r;
				r = m % n;
			}

			System.out.println("最大公約数は" + n + "です");

		} catch(IOException e) {
			System.out.println(e);
		} catch(NumberFormatException e) {
			System.out.println("数値を入力してください");
		}
	}
}
