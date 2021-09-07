import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class J2905 {

	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.print("IPアドレスを入力してください:");
			String ipAddrStr = reader.readLine();

			//IPアドレスの形式チェック
			if(checkAddress(ipAddrStr)) {

				System.out.print("サブネットマスクを入力してください:");
				String subnetMaskStr = reader.readLine();

				//サブネットマスクの形式チェック
				if(checkAddress(subnetMaskStr, true)) {

					//IPアドレスを32ビットに変換
					long ipAddr32 = toAddr32(ipAddrStr);

					//サブネットマスクを32ビットに変換
					long snMask32 = toAddr32(subnetMaskStr);

					//ネットワークアドレスを算出
					long netAddr32 = ipAddr32 & snMask32;
					System.out.println("ネットワークアドレスは" + dotDecimal(netAddr32) + "です");

					//***おまけ*** ブロードキャストアドレスを算出
					long bcAddr32 = ipAddr32 | (~snMask32);
					System.out.println("ブロードキャストアドレスは" + dotDecimal(bcAddr32) + "です");

				} else {
					System.out.println("入力が正しくありません(サブネットマスクの形式)");
				}
			} else {
				System.out.println("入力が正しくありません(IPアドレスの形式)");
			}

		} catch(IOException e) {
			System.out.println(e);
	    }
	}

	//IPアドレスの形式チェック(サブネットフラグなし)
	public static boolean checkAddress(String addr) {
		return checkAddress(addr, false);
	}

	//IPアドレスの形式チェック
	public static boolean checkAddress(String addr, boolean subnetFlag) {

		//エラーチェック用のフラグ(エラーがあればfalse)
		boolean flag = true;

		//アドレスで使用できる文字(数字およびピリオド)
		String checkChar = "0123456789.";

		//ピリオドの個数
		int pCount = 0;

		//オクテット毎の値
		String[] octet = new String[] {"", "", "", ""};

		//サブネットマスクで取り得る値を定義(0,255を除く)
		Set<Integer> set = new HashSet<Integer>();
		set.add(128);
		set.add(192);
		set.add(224);
		set.add(240);
		set.add(248);
		set.add(252);
		set.add(254);

		//アドレスが数字かピリオド以外ならエラー
		for(int i=0; i < addr.length(); i++) {
			char a = addr.charAt(i);
			int n = checkChar.indexOf(a);
			if(n < 0) {
    			System.out.println("*** 数字とピリオド以外が含まれています");
				flag = false;
				break;
			}
			//ピリオド(n:10)が来たらオクテットを一つ進める
			if(n == 10) {
            	pCount++;
    			//ピリオドの個数をチェック(3つ以上ならエラー)
            	if(pCount > 3) {
        			System.out.println("*** ピリオドの数が4つ以上あります");
            		flag = false;
            		break;
            	}
            } else {
            	octet[pCount] += a;
            }
		}

		if(flag) {
			//ピリオドの数が3個でない場合エラー
			if(pCount != 3) {
				System.out.println("*** ピリオドの数が3つではありません");
				flag = false;
			} else {

				try {

					int beforeAddr = 1;		//サブネットマスクの場合、連続した1と0のチェックに用いるフラグ


					for(int i=0; i < octet.length ; i++) {
						int decAddr = Integer.parseInt(octet[i]);
						if(decAddr < 0 || decAddr > 255){
							System.out.println("*** 第" + (i+1) +"オクテットの範囲が0～255ではありません");
							flag = false;
							break;
						}

						//サブネットマスクの場合のチェック
						if(subnetFlag) {
							//サブネットマスクで取り得る値かどうかチェック
							boolean matchFlag = false;
							if(set.contains(decAddr)) {
								matchFlag = true;
							}
							if(decAddr != 0 && decAddr != 255 && !matchFlag) {
								System.out.println("*** オクテット内の数値が連続していません");
								flag = false;
								break;
							}

							//サブネットマスクが連続した1のあとに連即した0となっているかチェック
							switch(beforeAddr) {
							  case 1:		//前のオクテットが255の場合
								break;

							  case 0:		//前のオクテットが0の場合
								if(decAddr != 0) {
									System.out.println("*** サブネットマスクの0が正しく連続していません");
									flag = false;
								}
								break;

							  case -1:		//前のオクテットが0と255以外の場合
								if(decAddr != 0 || matchFlag) {
									System.out.println("*** サブネットマスクの0または1が正しく連続していません");
									flag = false;
								}
								break;
							}

							//現在のオクテットの情報を前のオクテットの情報として記録
							if(decAddr == 255) {
								beforeAddr = 1;
							} else if (decAddr == 0){
								beforeAddr = 0;
							} else {
								beforeAddr = -1;
							}

						}
					}
				} catch (NumberFormatException e) {
					System.out.println("*** オクテット内の数値に誤りがあります");
					flag = false;
				}
			}
		}

		return flag;
	}


	//ドット付き10進数を32ビットアドレスに変換
	public static long toAddr32(String addr) {
		long retAddr32 = 0;
		int[] addr32 = addrSplit(addr);
		for(int i=0; i < addr32.length; i++) {
			retAddr32 += addr32[i] * Math.pow(2, 8 * (3-i));
		}
		return retAddr32;
	}

	//アドレス分割 ※形式チェック済みの前提なのでエラーチェックはしていない
	public static int[] addrSplit(String addr) {
		String checkChar = "0123456789.";
		int num = 0;
		String str = "";
		int[] tmp = new int[4];

		for(int i=0; i < addr.length(); i++) {
			char a = addr.charAt(i);
			int n = checkChar.indexOf(a);
            if(n == 10) {
            	tmp[num] = Integer.parseInt(str);
            	str = "";
            	num++;
            } else {
            	str += a;
            }
		}
		tmp[3] = Integer.parseInt(str);

		return tmp;
	}

	//32ビットアドレスをドット付き10進数に変換
	public static String dotDecimal(long addr32) {
		String tmpStr = "";

		//1オクテットずつ切り出してドット付き10進数に変換
		for(int i=0; i < 4; i++) {
			long tmpAddr = ( addr32 >> (8*(3-i)) ) & 255 ;
			tmpStr += tmpAddr;

			//オクテット毎にドットを追加
			if(i<3) {
				tmpStr += ".";
			}
		}

		return tmpStr;
	}

}
