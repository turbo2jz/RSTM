package datepanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.*;

/**
 * DatePanel��Ϊ��װ��ɵ�����ѡ������࣬��ʵ�����������������ṩ�����ֹ��췽����
 * ��ʵ���˻�ȡѡ�����ڵķ���(�������ڸ�ʽΪyyyy/MM/dd)
 * @author always
 *
 */
public class DatePanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8184540876958235711L;
	//������������
	private JComboBox<Integer> comb_year,comb_month,comb_day;
	
	/**
	 * DatePanel�ຬ�ι��캯��
	 * @param name String DataPanel���ƣ���ʾ��Panelǰ��
	 */
	public DatePanel(String name){
		Init(name);
	}
	
	/**
	 * DatePanel��Ĭ�Ϲ��캯��
	 * Ĭ����nameΪ"���ڣ�"
	 */
	public DatePanel(){
		Init("���ڣ�");
	}
	
	/**
	 * Init()
	 * DatePanel���ʼ������
	 * @param name
	 */
	private void Init(String name){
		this.setLayout(new FlowLayout());
		ComboBoxSet();
		this.add(new JLabel(name));
		this.add(comb_year);
		this.add(new JLabel("��"));
		this.add(comb_month);
		this.add(new JLabel("��"));
		this.add(comb_day);
		this.add(new JLabel("��"));
	}
	
	/**
	 * ComboBoxSet()
	 * DatePanel��ComboBox���ϳ�ʼ������
	 */
	private void ComboBoxSet(){
		int beginYear=2017,curYear=getSysYear();
		Integer month[]={1,2,3,4,5,6,7,8,9,10,11,12};
		//Year(ע�������2017����䵽��ǰ���)
		this.comb_year=new JComboBox<Integer>();
		for(int year=beginYear;year<=curYear;year++){
			this.comb_year.addItem(year);
		}
		this.comb_year.setSelectedItem(curYear);
		//Month
		this.comb_month=new JComboBox<Integer>(month);
		this.comb_month.setSelectedItem(1);
		this.comb_month.addActionListener(this);
		//Day
		this.comb_day=new JComboBox<Integer>();
		for(int day=1;day<=31;day++){
			this.comb_day.addItem(day);
		}
		this.comb_day.setSelectedItem(1);
	}
	
	/**
	 * DatePanel��ѡȡ���ڻ�ȡ����
	 * getSelectedData()
	 * @return String ����(yyyy/MM/dd)
	 */
	public String getSelectedData(){
		StringBuffer sb=new StringBuffer();
		sb.append((int)comb_year.getSelectedItem());
		sb.append('/');
		sb.append((int)comb_month.getSelectedItem());
		sb.append('/');
		sb.append((int)comb_day.getSelectedItem());
		return sb.toString();
	}
	
	/**
	 * getSysYear()Ϊ��ȡ��ǰϵͳ��ݵľ�̬����
	 * @return Integer ���(����)
	 */
	private static int getSysYear(){
		Calendar date = Calendar.getInstance();
		return date.get(Calendar.YEAR);
	}
	
	/**
	 * isLeapYear()Ϊ�ж�����Ƿ�Ϊ����ľ�̬����
	 * @param year
	 * @return
	 */
	private static boolean isLeapYear(int year){
		return year%400==0||year%100!=0&&year%4==0;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.comb_month){
			int i=this.comb_month.getSelectedIndex();
			if(i!=-1){
				this.comb_day.removeAllItems();
				int end_day=0;
				switch (i+1){
					case 1:case 3:case 5:case 7:case 8:case 10:case 12: end_day=31;break;
					case 4:case 6:case 9:case 11: end_day=30;break;
					case 2: end_day=isLeapYear((int) this.comb_year.getSelectedItem())?29:28;break;
				}
				for(int j=1;j<=end_day;j++){
					this.comb_day.addItem(j);
				}
			}
		}
	}
	
}
