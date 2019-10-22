package scanfu.com.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore.Video.Thumbnails;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 * 针对三星手机的 拍照处理
 * 
 */
public class BitmapsUtil {

	// 1.调用拍照后，读取临时存储的图片
	private static final int DEFAULT_REQUIRED_SIZE = 70;

	public static Bitmap decodeFile(File f, int size) {
		try {
			// BitmapFactory.Options option = new BitmapFactory.Options();
			// option.inJustDecodeBounds = true;
			// FileInputStream stream1 = new FileInputStream(f);
			// BitmapFactory.decodeStream(stream1, null, option);
			// stream1.close();
			// final int REQUIRED_SIZE = size > 0 ? size :
			// DEFAULT_REQUIRED_SIZE;
			// int width_tmp = option.outWidth, height_tmp = option.outHeight;
			// int scale = 1;
			// while (true) {
			// if (width_tmp / 2 < REQUIRED_SIZE
			// || height_tmp / 2 < REQUIRED_SIZE)
			// break;
			// width_tmp /= 2;
			// height_tmp /= 2;
			// scale *= 2;
			// }
			// if (scale >= 2) {
			// scale /= 2;
			// }
			BitmapFactory.Options option2 = new BitmapFactory.Options();
			// option2.inSampleSize = scale;
			option2.inPreferredConfig = Config.RGB_565;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, option2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 2.获取图片旋转的角度，然后给它旋转回来

	/**
	 * 获取图片信息
	 * 
	 * @param path
	 * @return
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	// 3..根据指定旋转度数进行图片旋转
	/**
	 * 图片旋转
	 * 
	 * @param angle
	 * @param bitmap
	 * @return
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		System.out.println("angle=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	// 4 储旋转后图片

	public static File compressHeadPhoto(final Bitmap bm, File filename) {
		File imgFile = null;
		if (filename == null)
			return imgFile;

		String path = filename.getAbsolutePath();
		if (filename.exists()) {
			filename.delete();
		}

		FileOutputStream fos;
		try {
			// filename.createNewFile();
			imgFile = new File(path);
			fos = new FileOutputStream(imgFile);
			bm.compress(Bitmap.CompressFormat.JPEG, 95, fos);
			fos.flush();
			fos.close();
			// } catch (FileNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imgFile;
	}

	public static void createVedioImage(String VedioPath) {
		if (VedioPath == null)
			return;

		String ImageVedioPath = VedioPath.substring(0,
				VedioPath.lastIndexOf("."));

		File f = new File(ImageVedioPath);
		if (!f.exists()) {
			Bitmap bm = null;
			bm = ThumbnailUtils.createVideoThumbnail(VedioPath,
					Thumbnails.MINI_KIND);
			if (f.exists()) {
				f.delete();
			}
			try {
				FileOutputStream out = new FileOutputStream(f);
				bm.compress(Bitmap.CompressFormat.PNG, 90, out);
				out.flush();
				out.close();
			} catch (Exception e) {

			}
			String brand = (Build.BRAND).toLowerCase();//获取手机品牌， 华为手机不做以下操作
			
			if(!brand.contains("huawei")){
				 f = new File(ImageVedioPath);
				 // 1
				 bm = BitmapsUtil.decodeFile(f, 100);
				 // 2
				 int degree = BitmapsUtil.readPictureDegree(f.getAbsolutePath());
				 // 3
				 Bitmap bitmap = BitmapsUtil.rotaingImageView(degree, bm);
				 // 4
				 BitmapsUtil.compressHeadPhoto(bm, f);
			}
		}
	}



	// 获得压缩图片
	public static void setCompressedImage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 600f;// 这里设置宽度为480f
		// float hh = 1920f;// 这里设置高度为800f
		// float ww = 1080f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(srcPath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	// 获得压缩图片
	public static Bitmap getCompressedBitmap(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 600f;// 这里设置宽度为480f
		// float hh = 1920f;// 这里设置高度为800f
		// float ww = 1080f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;

	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);

		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		if (roundPx == 0)
			roundPx = bitmap.getWidth() / 2;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 
	 * @param bitmap
	 *            原图
	 * @param edgeLength
	 *            希望得到的正方形部分的边长
	 * @return 缩放截取正中部分后的位图。
	 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
		if (null == bitmap || edgeLength <= 0) {
			return null;
		}

		Bitmap result = bitmap;
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();

		if (widthOrg >= edgeLength && heightOrg >= edgeLength) {
			// 压缩到一个最小长度是edgeLength的bitmap
			int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math
					.min(widthOrg, heightOrg));
			int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
			int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
			Bitmap scaledBitmap;

			try {
				scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
						scaledHeight, true);
			} catch (Exception e) {
				return null;
			}

			// 从图中截取正中间的正方形部分。
			int xTopLeft = (scaledWidth - edgeLength) / 2;
			int yTopLeft = (scaledHeight - edgeLength) / 2;

			try {
				result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft,
						edgeLength, edgeLength);
				scaledBitmap.recycle();
			} catch (Exception e) {
				return null;
			}
		}

		return result;
	}

	/**
	 * 
	 * @param bitmap
	 *            原图
	 * 
	 * @return 缩放截取正中部分后的位图。
	 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap) {

		if (null == bitmap) {
			return null;
		}

		Bitmap result = bitmap;
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();
		int edgeLength = widthOrg > heightOrg ? heightOrg : widthOrg;
		if (widthOrg >= edgeLength && heightOrg >= edgeLength) {
			// 压缩到一个最小长度是edgeLength的bitmap
			int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math
					.min(widthOrg, heightOrg));
			int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
			int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
			Bitmap scaledBitmap;

			try {
				scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
						scaledHeight, true);
			} catch (Exception e) {
				return null;
			}

			// 从图中截取正中间的正方形部分。
			int xTopLeft = (scaledWidth - edgeLength) / 2;
			int yTopLeft = (scaledHeight - edgeLength) / 2;

			try {
				result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft,
						edgeLength, edgeLength);
				scaledBitmap.recycle();
			} catch (Exception e) {
				return null;
			}
		}

		return result;
	}
}
