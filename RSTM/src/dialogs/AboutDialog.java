package dialogs;

import java.awt.GridLayout;
import java.io.IOException;

import interfaces.AboutInfoRead;

import javax.swing.*;

/**
 * AboutDialog为关于信息显示窗体
 * @author always
 *
 */
public class AboutDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7571189229681651679L;
	
	public AboutDialog(JFrame f, String title, boolean model){
		super(f,title,model);
		init();
	}
	
	private void init(){
		String[] texts={};
		try {
			texts = AboutInfoRead.readAllLines();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setLayout(new GridLayout(texts.length,1));
		for(int i=0;i<texts.length;i++){
			JPanel tp=new JPanel();
			tp.add(new JLabel(texts[i]));
			this.add(tp);
		}
	}
	
	/**
	 * show()为窗体显示方法
	 * @param title 窗体标题
	 */
	public void show(String title){
		this.setTitle(title);//窗口标题设置
		this.setSize(250,150);//默认大小设置
	    this.setLocationRelativeTo(null);//居中
	    this.setVisible(true);//可视
	    this.setResizable(false);
	}
	
}
