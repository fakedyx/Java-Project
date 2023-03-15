package Gobang;
import java.net.*;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// String message = "ANNIU-START";
		// String[] split = message.split("-");
		// System.out.println("数组的长度" + split.length);
		// System.out.println("split[0]=" + split[0]);
		// System.out.println("split[1]==" + split[1]);
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			String localip = ip.getHostAddress();
			System.out.println(localip);
		}catch(Exception e) {
			
		}
	}

}
