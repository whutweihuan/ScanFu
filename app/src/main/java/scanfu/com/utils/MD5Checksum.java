/**
 * 作者: 和志刚
 * 日期: 2013-6-3
 * 时间: 上午10:08:44
 */
package scanfu.com.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * 生成文件的MD5摘要串
 * Create a checksum
 * http://www.rgagnon.com/javadetails/java-0416.html
 */
public class MD5Checksum {
	private static byte[] createChecksum(String filename) throws Exception {
		InputStream fis = new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;
		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		fis.close();
		return complete.digest();
	}
	
	private static byte[] createChecksum(File f) throws Exception {
		InputStream fis = new FileInputStream(f);
		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;
		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		fis.close();
		return complete.digest();
	}	

	// see this How-to for a faster way to convert
	// a byte array to a HEX string
	public static String getMD5Checksum(String filename) {
		String result = "";
		try {
			byte[] b = createChecksum(filename);
			for (int i = 0; i < b.length; i++) {
				result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getMD5Checksum(File f) {
		String result = "";
		try {
			byte[] b = createChecksum(f);
			for (int i = 0; i < b.length; i++) {
				result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}	
	
	
	
	
	/**
	 * 对字符串进行MD5
	 * @param string
	 * @return
	 */
	public static String getMD5String(String string) {
	    byte[] hash = null;
	    try {
	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    StringBuilder hex = new StringBuilder(hash.length * 2);
	    for (byte b : hash) {
	        if ((b & 0xFF) < 0x10) hex.append("0");
	        hex.append(Integer.toHexString(b & 0xFF));
	    }
	    return hex.toString();
	}
}
