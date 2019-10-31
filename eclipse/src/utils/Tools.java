package utils;

public class Tools {
	public static boolean strOK(String args) {
		if(args!=null && !args.isEmpty()) {
			return true;
		}
		return false;
	}
}
