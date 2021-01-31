package dialogs;

import java.awt.GridLayout;
import java.io.IOException;

import interfaces.AboutInfoRead;

import javax.swing.*;

/**
 * AboutDialogΪ������Ϣ��ʾ����
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
	 * show()Ϊ������ʾ����
	 * @param title �������
	 */
	public void show(String title){
		this.setTitle(title);//���ڱ�������
		this.setSize(250,150);//Ĭ�ϴ�С����
	    this.setLocationRelativeTo(null);//����
	    this.setVisible(true);//����
	    this.setResizable(false);
	}
	
}
