package interfaces;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * NewWorkday类提供了判别是否是新工作日的静态方法isNewWorkdayJudge()
 * @author always
 * 
 */
public class NewWorkday {
	//文件夹名
	private final static String foldername = "refreshdate";
	//文件夹下存储日期的文件
	private final static String filename = "refreshdate/refresh.dat";
	/**
	 * datefoundandwrite()用于判断日期文件及其所在路径是否存在，不存在则创建。
	 * 然后写入参数date(日期)
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
		//覆盖写入日期
		FileWriter fw=new FileWriter(filename,false);
		fw.write(date+"\r\n");
		fw.close();
	}
	
	/**
	 * isNewWorkdayJudge()用于判断是否为新工作日
	 * @return
	 * @throws Exception
	 */
	public static boolean isNewWorkdayJudge() throws Exception{
		//当前日期获取
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date day=new Date();
		String date=df.format(day);
		//日期存储文件
		File file = new File(filename);
		//日期存储文件不存在，创建
		if(!file.exists()){
			datefoundandwrite(date);
			return true;
		}
		//字符流读取
		FileReader fr= new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String recordDate;
		recordDate=br.readLine();
		br.close();//字符流关闭
		//日期比对
		if(recordDate.equals(date))return false;
		else{
			datefoundandwrite(date);
			return true;
		}
	}
}
