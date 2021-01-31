package thread;

import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.*;

/**
 * TimeThread����Ϊʱ���߳���������ʾ��ǰʱ�䣬��ʾʱ��ķ���Ϊ��
 * ���һ��ˢ�´���JLabelʱ���ַ���
 * @author always
 * 
 */
public class TimeThread extends Thread {
	private JLabel j;
	public TimeThread(JLabel jt){
		j=jt;
	}
	public void run(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy��MM��dd��  HH:mm:ss"); 
		while(true){
			Date day=new Date(); 
			this.j.setText(df.format(day));
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
