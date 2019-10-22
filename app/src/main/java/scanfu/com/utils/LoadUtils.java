package scanfu.com.utils;

/**
 * 工具类
 * @author wanghaifei
 *
 */
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class LoadUtils {

	public static byte[] load(InputStream in)  {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) != -1)
				out.write(buf, 0, len);
			in.close();
			out.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return out.toByteArray();
	}

	
	
	
	
	
}
