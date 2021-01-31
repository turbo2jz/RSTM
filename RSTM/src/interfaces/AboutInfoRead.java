package interfaces;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * AboutInfoRead提供关于信息读取方法
 * @author always
 *
 */
public class AboutInfoRead {
	//关于文本内容路径
	private static String AboutInfo ="aboutinfo/AboutInfo.txt";
	
	//按行读取文件内数据，每行做字符串存放于字符串数组，返回字符串数组
	public static String[] readAllLines() throws IOException{
		FileReader fr = new FileReader(AboutInfo);
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> allLines = new ArrayList<String>();//ArrayList数组队列，便于扩展长度
		String Line;
		while((Line=br.readLine())!=null){
			allLines.add(Line);
		}
		//关闭流
		br.close();
		fr.close();
		//ArrayList<String>转String数组
		String str[]=new String[allLines.size()];
		allLines.toArray(str);
		return str;
	}
}
