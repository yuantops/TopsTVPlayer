package com.yuantops.tvplayer.util;

import java.util.Hashtable;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class GetBitmap {

	  /* 
	    * @author ���λ�
	    * date 2013-9-6
	    * ��ά�����ͼƬ�е�bitmap
	    * */
	public static Bitmap createQRImage(String url){
		
		Bitmap bitmap = null;
		int QR_WIDTH = 200, QR_HEIGHT = 200;
		try
		{
			//�ж�URL�Ϸ���
			if (url == null || "".equals(url) || url.length() < 1)
			{
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			//ͼ�����ת����ʹ���˾���ת��
			BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			//�������ﰴ�ն�ά����㷨�������ɶ�ά���ͼƬ��
			//����forѭ����ͼƬ����ɨ��Ľ��
			for (int y = 0; y < QR_HEIGHT; y++)
			{
				for (int x = 0; x < QR_WIDTH; x++)
				{
					if (bitMatrix.get(x, y))
					{
						pixels[y * QR_WIDTH + x] = 0xff000000;
					}
					else
					{
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			//��ɶ�ά��ͼƬ�ĸ�ʽ��ʹ��ARGB_8888
			bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			//��ʾ��һ��ImageView����
		}
		catch (WriterException e)
		{
			e.printStackTrace();
		}
		return bitmap;
	}
}
