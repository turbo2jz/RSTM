package interfaces;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * NewWorkday���ṩ���б��Ƿ����¹����յľ�̬����isNewWorkdayJudge()
 * @author always
 * 
 */
public class NewWorkday {
	//�ļ�����
	private final static String foldername = "refreshdate";
	//�ļ����´洢���ڵ��ļ�
	private final static String filename = "refreshdate/refresh.dat";
	/**
	 * datefoundandwrite()�����ж������ļ���������·���Ƿ���ڣ��������򴴽���
	 * Ȼ��д�����date(����)
	 * @param date
	 * @throws Exception
	 */
	private static void datefoundandwrite(String date)throws Exception{
		File flolder = new File(foldername);
		if (!flolder.exists()) {
			flolder.mkdirs();
		}
		File file = new File(filename);
		if (!file.exists()) {
			file.createNewFile();
		}
		//����д������
		FileWriter fw=new FileWriter(filename,false);
		fw.write(date+"\r\n");
		fw.close();
	}
	
	/**
	 * isNewWorkdayJudge()�����ж��Ƿ�Ϊ�¹�����
	 * @return
	 * @throws Exception
	 */
	public static boolean isNewWorkdayJudge() throws Exception{
		//��ǰ���ڻ�ȡ
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date day=new Date();
		String date=df.format(day);
		//���ڴ洢�ļ�
		File file = new File(filename);
		//���ڴ洢�ļ������ڣ�����
		if(!file.exists()){
			datefoundandwrite(date);
			return true;
		}
		//�ַ�����ȡ
		FileReader fr= new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String recordDate;
		recordDate=br.readLine();
		br.close();//�ַ����ر�
		//���ڱȶ�
		if(recordDate.equals(date))return false;
		else{
			datefoundandwrite(date);
			return true;
		}
	}
}
