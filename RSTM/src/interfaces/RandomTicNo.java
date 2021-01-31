package interfaces;

import java.util.*;

/**
 * RandomTicNo提供了用于随机生成车票编号的方法
 * @author always
 *
 */
public class RandomTicNo {
	private static String str1="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static String str2="0123456789";
	
	/**
	 * getRandomTicNum()随机生成车票编号
	 * @return String (车票编号构成：首位为字母，其余六位为数字)
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
