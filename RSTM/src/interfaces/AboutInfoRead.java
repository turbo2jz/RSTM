package interfaces;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * AboutInfoRead�ṩ������Ϣ��ȡ����
 * @author always
 *
 */
public class AboutInfoRead {
	//�����ı�����·��
	private static String AboutInfo ="aboutinfo/AboutInfo.txt";
	
	//���ж�ȡ�ļ������ݣ�ÿ�����ַ���������ַ������飬�����ַ�������
	public static String[] readAllLines() throws IOException{
		FileReader fr = new FileReader(AboutInfo);
		BufferedReader br = new BufferedReader(fr);
		ArrayList<String> allLines = new ArrayList<String>();//ArrayList������У�������չ����
		String Line;
		while((Line=br.readLine())!=null){
			allLines.add(Line);
		}
		//�ر���
		br.close();
		fr.close();
		//ArrayList<String>תString����
		String str[]=new String[allLines.size()];
		allLines.toArray(str);
		return str;
	}
}
