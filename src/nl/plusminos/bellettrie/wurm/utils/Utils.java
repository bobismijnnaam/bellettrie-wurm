package nl.plusminos.bellettrie.wurm.utils;

public class Utils {
	public static int lameStringHash(String str) {
		byte[] bytes = str.getBytes();
		int goodLength = (int) (Math.ceil(bytes.length / 4.0) * 4.0);
		byte[] padded = new byte[goodLength];
		System.arraycopy(bytes, 0, padded, 0, bytes.length);
		
		// Loop through quadrupels
		for(int i = 0)
	}
}
