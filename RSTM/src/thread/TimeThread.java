package thread;

import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.*;

/**
 * TimeThread本类为时间线程类用于显示当前时间，显示时间的方法为：
 * 间隔一秒刷新传入JLabel时间字符串
 * @author always
 * 
 */
public class TimeThread extends Thread {
	private JLabel j;
	public TimeThread(JLabel jt){
		j=jt;
	}
	public void run(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss"); 
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
