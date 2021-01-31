package interfaces;

import java.util.*;

/**
 * RandomTicNo�ṩ������������ɳ�Ʊ��ŵķ���
 * @author always
 *
 */
public class RandomTicNo {
	private static String str1="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static String str2="0123456789";
	
	/**
	 * getRandomTicNum()������ɳ�Ʊ���
	 * @return String (��Ʊ��Ź��ɣ���λΪ��ĸ��������λΪ����)
	 */
	public static String getRandomTicNum(){
		Random rand = new Random();
		StringBuffer res = new StringBuffer();
		res.append(str1.charAt(rand.nextInt(26)) + "");
		for (int j = 0; j < 6; j++) 
		{
			res.append(str2.charAt(rand.nextInt(9)) + "");
		}
		return res.toString();
	}
}
