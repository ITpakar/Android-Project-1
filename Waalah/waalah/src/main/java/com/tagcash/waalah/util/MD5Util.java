package com.tagcash.waalah.util;

import java.security.MessageDigest;

public class MD5Util {
	public static String getMD5(String s) {
		MessageDigest messagedigest = null;
		byte abyte0[];
		int i;
		StringBuffer stringbuffer;
		try {
			messagedigest = MessageDigest.getInstance("MD5");
			messagedigest.reset();
			messagedigest.update(s.getBytes("UTF-8"));
		}
		// Misplaced declaration of an exception variable
		catch (Exception e) {
			e.printStackTrace();
		}

		abyte0 = messagedigest.digest();
		stringbuffer = new StringBuffer();
		i = 0;
		do {
			if (i >= abyte0.length)
				return stringbuffer.toString();
			if (Integer.toHexString(0xff & abyte0[i]).length() == 1)
				stringbuffer.append("0").append(
						Integer.toHexString(0xff & abyte0[i]));
			else
				stringbuffer.append(Integer.toHexString(0xff & abyte0[i]));
			i++;
		} while (true);
	}
}
