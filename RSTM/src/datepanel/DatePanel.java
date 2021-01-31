package datepanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.*;

/**
 * DatePanel类为封装完成的日期选择面板类，可实现日期联动。该类提供了两种构造方法，
 * 并实现了获取选择日期的方法(返回日期格式为yyyy/MM/dd)
 * @author always
 *
 */
public class DatePanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8184540876958235711L;
	//年月日下拉框
	private JComboBox<Integer> comb_year,comb_month,comb_day;
	
	/**
	 * DatePanel类含参构造函数
	 * @param name String DataPanel名称，显示在Panel前部
	 */
	public DatePanel(String name){
		Init(name);
	}
	
	/**
	 * DatePanel类默认构造函数
	 * 默认置name为"日期："
	 */
	public DatePanel(){
		Init("日期：");
	}
	
	/**
	 * Init()
	 * DatePanel类初始化方法
	 * @param name
	 */
	private void Init(String name){
		this.setLayout(new FlowLayout());
		ComboBoxSet();
		this.add(new JLabel(name));
		this.add(comb_year);
		this.add(new JLabel("年"));
		this.add(comb_month);
		this.add(new JLabel("月"));
		this.add(comb_day);
		this.add(new JLabel("日"));
	}
	
	/**
	 * ComboBoxSet()
	 * DatePanel类ComboBox集合初始化方法
	 */
	private void ComboBoxSet(){
		int beginYear=2017,curYear=getSysYear();
		Integer month[]={1,2,3,4,5,6,7,8,9,10,11,12};
		//Year(注：年份由2017年填充到当前年份)
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
	 * DatePanel类选取日期获取方法
	 * getSelectedData()
	 * @return String 日期(yyyy/MM/dd)
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
	 * getSysYear()为获取当前系统年份的静态方法
	 * @return Integer 年份(整数)
	 */
	private static int getSysYear(){
		Calendar date = Calendar.getInstance();
		return date.get(Calendar.YEAR);
	}
	
	/**
	 * isLeapYear()为判断年份是否为闰年的静态方法
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
