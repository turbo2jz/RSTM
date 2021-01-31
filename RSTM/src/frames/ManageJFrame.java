package frames;

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;

import checkboxlist.CheckBoxList;
import dao.SqlExecute;
import datepanel.DatePanel;
/**
 * ManageJFrame类为火车站售票管理系统子系统管理系统窗口类
 * @author always
 *
 */
public class ManageJFrame extends JFrame implements ActionListener,CaretListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7226973959769654742L;
	private JMenuBar menubar;	//菜单(窗体上部)
	protected JMenu menus[]; 	//菜单数组
	
	private LogJFrame main;		//父窗口对象

	private SqlExecute sqlexec;	//数据库操作类对象
	
	@SuppressWarnings("unused")
	private String curSano;		//当前业务员编号
	
	private JPanel optPanel;	//顶部面板(放置操作面板)
	private JPanel showpanel;	//底部表格显示面板
	private JPanel panel1,panel2,panel3,panel4;
	private JTable showTable;
	private DefaultTableModel tableModel;
	
	//panel1组件
	private JTextField perPrice1,perPrice2,perPrice3;
	private CheckBoxList cblist_t;
	private JButton btnitemselect_t,confirmBtn_train,updateBtn_train;
	private JLabel tnolbl;
	private boolean isTrainInfoQuery=false,isTrainConfrim=false;
	private JLabel traininfomodlbl;
	
	//panel2组件
	private JTextField sano,saname;
	private JComboBox<String> satype,sex;
	private JComboBox<Integer> age,award;
	private CheckBoxList cblist;
	private JButton btnitemselect,btnstaffconfirm,btnupdate;
	private JLabel staffinfomodlbl;
	private boolean isStaffInfoQuery=false,isStaffConfrim=false;
	
	//panel3组件
	private ButtonGroup bp;
	private JPanel centerPanel;
	private JPanel tnoPanel;
	private JTextField tno;
	private JButton confrimBtn_train;
	private DatePanel datePanel1;
	
	//panel4组件
	private DatePanel datePanel2;
	private JButton confrimBtn_staff;
	
	public ManageJFrame(int width, int height,LogJFrame main){
		super();
		this.setLayout(new BorderLayout());
		
		this.main=main;
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				ManageJFrame.this.main.refrashLogJFrame();
				ManageJFrame.this.main.setVisible(true);
			}
		});
		sqlexec=new SqlExecute();
		menuBarInit();
		optPanelInit();
		showPanelInit();
		panel1init();
		panel2init();
		panel3init();
		panel4init();
		//初始操作界面初始化为车次价格管理
		this.optPanel.add(panel1,BorderLayout.CENTER);
		//设置窗体图标
		Image icon = Toolkit.getDefaultToolkit().getImage("images/train_icon.png"); 
		this.setIconImage(icon);
		this.setSize(width, height);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setResizable(false);
	}
	
	
	/**
	 * menuBarInit()用于菜单栏初始化
	 */
	private void menuBarInit(){
		String menustr[]={"车次管理","业务员管理","销售信息查询"};
		String menuitemstr[][]={{"全部车次信息","车次价格管理"},
				{"全部业务员信息","业务员信息管理"},
				{"车票销售信息","业务员销售信息"}};
		this.menubar=new JMenuBar();
        JMenuBar menubar = new JMenuBar();                 //菜单栏
        this.setJMenuBar(menubar);                         //框架上添加菜单栏
        this.menus = new JMenu[menustr.length];            //菜单数组
        JMenuItem menuitems[][] = new JMenuItem[menuitemstr.length][]; //菜单项二维数组
        for (int i=0; i<menuitemstr.length; i++)           //添加菜单和菜单项
        {
            this.menus[i] = new JMenu(menustr[i]);         //菜单
            menubar.add(this.menus[i]);
            //菜单栏中加入菜单
            menuitems[i] = new JMenuItem[menuitemstr[i].length];
            for (int j=0; j<menuitemstr[i].length; j++)
                if (menuitemstr[i][j].equals("|"))
                    this.menus[i].addSeparator();          //加分隔线
                else 
                {   menuitems[i][j] = new JMenuItem(menuitemstr[i][j]); //创建菜单项
                    this.menus[i].add(menuitems[i][j]);    //菜单项加入到菜单
                    menuitems[i][j].addActionListener(this);//菜单项注册动作事件监听器
                }
        }
        this.add(this.menubar);
	}
	
	/**
	 * showPanelInit()用于显示面板初始化
	 */
	private void showPanelInit(){
		this.showpanel=new JPanel();
		this.add(showpanel,BorderLayout.CENTER);
		showpanel.setLayout(new BorderLayout());
		this.tableModel=new DefaultTableModel();
		
		this.showTable=new JTable();
		this.showTable.setModel(tableModel);
		showpanel.add(showTable,BorderLayout.CENTER);
		showpanel.add(new JScrollPane(showTable));
	}
	
	/**
	 * optPanelInit()用于操作面板初始化
	 */
	private void optPanelInit(){
		this.optPanel=new JPanel();
		this.add(optPanel,BorderLayout.NORTH);
		this.optPanel.setLayout(new BorderLayout());
	}
	
	private void panel1init(){
		panel1=new JPanel();
		panel1.setLayout(new BorderLayout());
		JPanel panel1_l=new JPanel();
		panel1_l.setBorder(new TitledBorder("车次单位里程价格管理"));
		panel1_l.setLayout(new BorderLayout());
		JPanel panel1_l1=new JPanel();
		panel1_l1.setLayout(new GridLayout(4,1));
		JPanel infopanel=new JPanel();
		infopanel.add(new JLabel("选择车次为："));
		tnolbl=new JLabel();
		infopanel.add(tnolbl);
		panel1_l1.add(infopanel);
		JPanel pp1=new JPanel();
		pp1.add(new JLabel("二等座单位里程价格(元/KM)："));
		perPrice1=new JTextField(5);
		perPrice1.setEditable(false);
		perPrice1.addCaretListener(this);
		pp1.add(perPrice1);
		panel1_l1.add(pp1);
		JPanel pp2=new JPanel();
		pp2.add(new JLabel("一等座单位里程价格(元/KM)："));
		perPrice2=new JTextField(5);
		perPrice2.setEditable(false);
		perPrice2.addCaretListener(this);
		pp2.add(perPrice2);
		panel1_l1.add(pp2);
		JPanel pp3=new JPanel();
		pp3.add(new JLabel("商务座单位里程价格(元/KM)："));
		perPrice3=new JTextField(5);
		perPrice3.setEditable(false);
		perPrice3.addCaretListener(this);
		pp3.add(perPrice3);
		panel1_l1.add(pp3);
		panel1_l.add(panel1_l1,BorderLayout.CENTER);
		confirmBtn_train=new JButton("查询");
		confirmBtn_train.addActionListener(this);
		updateBtn_train=new JButton("修改");
		updateBtn_train.addActionListener(this);
		traininfomodlbl=new JLabel();
		JPanel panel1_l2=new JPanel();
		panel1_l2.setLayout(new BorderLayout());
		JPanel panel1_l2u=new JPanel();
		panel1_l2u.add(confirmBtn_train);
		panel1_l2u.add(updateBtn_train);
		panel1_l2.add(panel1_l2u,BorderLayout.CENTER);
		JPanel panel1_l2d=new JPanel();
		panel1_l2d.add(traininfomodlbl);
		panel1_l2.add(panel1_l2d,BorderLayout.SOUTH);
		panel1_l.add(panel1_l2,BorderLayout.EAST);
		panel1.add(panel1_l,BorderLayout.CENTER);
		JPanel panel1_r=new JPanel();
		panel1_r.setBorder(new TitledBorder("修改选项"));
		panel1_r.setLayout(new BorderLayout());
		String Items[]={"二等座","一等座","商务座"};
		cblist_t=new CheckBoxList(Items);
		JScrollPane ps = new JScrollPane();
		ps.getViewport().add(cblist_t);
		panel1_r.add(ps,BorderLayout.CENTER);
		btnitemselect_t=new JButton("确认");
		btnitemselect_t.addActionListener(this);
		JPanel btnPanel=new JPanel();
		btnPanel.add(btnitemselect_t);
		panel1_r.add(btnPanel,BorderLayout.SOUTH);
		panel1.add(panel1_r,BorderLayout.EAST);
	}
	
	private void panel2init(){
		//业务员信息修改面板
		JPanel panel2_l=new JPanel();
		//业务员各项信息修改模块面板
		panel2_l.setBorder(new TitledBorder("业务员信息管理"));
		panel2_l.setLayout(new BorderLayout());
		JPanel panel2_l1=new JPanel();
		panel2_l1.setLayout(new GridLayout(4,1));
		//编号(p1)
		JPanel p1=new JPanel();
		p1.add(new JLabel("编号："));
		this.sano=new JTextField(12);
		sano.setEditable(false);
		p1.add(sano);
		panel2_l1.add(p1);
		//姓名(p2)
		JPanel p2=new JPanel();
		p2.add(new JLabel("姓名："));
		this.saname=new JTextField(12);
		saname.setEditable(false);
		p2.add(saname);
		panel2_l1.add(p2);
		//性别(p3)
		JPanel p3=new JPanel();
		p3.add(new JLabel("性别："));
		String[] sex_str={"男","女"};
		this.sex=new JComboBox<String>(sex_str);
		sex.setSelectedIndex(0);
		sex.setEnabled(false);
		p3.add(sex);
		//年龄(p3)
		p3.add(new JLabel("年龄："));
		this.age=new JComboBox<Integer>();
		for(int i=20;i<=60;i++){
			age.addItem(i);
		}
		age.setSelectedIndex(0);
		age.setEnabled(false);
		p3.add(age);
		panel2_l1.add(p3);
		JPanel p4=new JPanel();
		//类型(p4)
		p4.add(new JLabel("类型："));
		String[] satype_str={"管理员","业务员"};
		this.satype=new JComboBox<String>(satype_str);
		satype.setSelectedIndex(0);
		satype.setEnabled(false);
		p4.add(satype);
		//奖励等级(p4)
		p4.add(new JLabel("奖励等级："));
		this.award=new JComboBox<Integer>();
		for(int i=1;i<=5;i++){
			award.addItem(i);
		}
		award.setSelectedIndex(0);
		award.setEnabled(false);
		p4.add(award);
		panel2_l1.add(p4);
		panel2_l.add(panel2_l1,BorderLayout.CENTER);
		
		//业务员信息修改按钮及提示面板
		JPanel panel2_l2=new JPanel();
		panel2_l2.setLayout(new BorderLayout());
		//按钮
		btnstaffconfirm=new JButton("确认修改员工");
		btnstaffconfirm.addActionListener(this);
		btnupdate=new JButton("更新信息");
		btnupdate.addActionListener(this);
		JPanel modbtnpanel=new JPanel();
		modbtnpanel.add(btnstaffconfirm);
		modbtnpanel.add(btnupdate);
		panel2_l2.add(modbtnpanel,BorderLayout.CENTER);
		//信息显示标签
		this.staffinfomodlbl=new JLabel("");
		JPanel lblPanel=new JPanel();
		lblPanel.add(staffinfomodlbl);
		panel2_l2.add(lblPanel,BorderLayout.SOUTH);
		panel2_l.add(panel2_l2,BorderLayout.EAST);
		panel2=new JPanel();
		panel2.setLayout(new BorderLayout());
		panel2.add(panel2_l,BorderLayout.CENTER);
		JPanel panel2_r=new JPanel();
		
		//右面板修改选项选择面板
		panel2_r.setBorder(new TitledBorder("修改选项"));
		panel2_r.setLayout(new BorderLayout());
		String Items[]={"姓名","类型","性别","年龄","奖励等级"};
		cblist=new CheckBoxList(Items);
		JScrollPane ps = new JScrollPane();
		ps.getViewport().add(cblist);
		panel2_r.add(ps,BorderLayout.CENTER);
		btnitemselect=new JButton("确认");
		btnitemselect.addActionListener(this);
		JPanel btnPanel=new JPanel();
		btnPanel.add(btnitemselect);
		panel2_r.add(btnPanel,BorderLayout.SOUTH);
		panel2.add(panel2_r,BorderLayout.EAST);
	}
	
	private void panel3init(){
		String str[]={"按时间查询所有车次车票销售情况","指定车次查询车票销售情况"};
		JRadioButton rbtn[]=new JRadioButton[str.length];
		this.panel3=new JPanel();
		datePanel1=new DatePanel();
		panel3.setBorder(new TitledBorder("车票销售信息查询"));
		this.panel3.setLayout(new BorderLayout());
		bp=new ButtonGroup();
		JPanel rbtPanel=new JPanel();
		rbtPanel.setLayout(new GridLayout(str.length,1));
		for(int i=0;i<str.length;i++)
		{
			rbtn[i]=new JRadioButton(str[i]);
			rbtn[i].addActionListener(this);
			bp.add(rbtn[i]);
			rbtPanel.add(rbtn[i]);
		}
		rbtn[0].setSelected(true);
		panel3.add(rbtPanel,BorderLayout.WEST);
		this.centerPanel=new JPanel();
		centerPanel.setLayout(new BorderLayout());
		panel3.add(centerPanel,BorderLayout.CENTER);
		this.tnoPanel=new JPanel();
		tnoPanel.setLayout(new FlowLayout());
		tnoPanel.add(new JLabel("车次号："));
		tno=new JTextField(10);
		tnoPanel.add(tno);
		centerPanel.add(datePanel1,BorderLayout.SOUTH);
		panel3.add(centerPanel,BorderLayout.CENTER);
		confrimBtn_train=new JButton("确定");
		confrimBtn_train.addActionListener(this);
		JPanel btPanel=new JPanel();
		btPanel.setLayout(new BorderLayout());
		btPanel.add(confrimBtn_train,BorderLayout.CENTER);
		panel3.add(btPanel,BorderLayout.EAST);
	}
	
	private void panel4init(){
		this.panel4=new JPanel();
		panel4.setBorder(new TitledBorder("业务员销售信息查询"));
		panel4.setLayout(new BorderLayout(1,2));
		datePanel2=new DatePanel("请输入想要查询的日期：");
		panel4.add(datePanel2,BorderLayout.CENTER);
		confrimBtn_staff=new JButton("确认");
		confrimBtn_staff.addActionListener(this);
		JPanel btnPanel=new JPanel();
		btnPanel.add(confrimBtn_staff);
		panel4.add(btnPanel,BorderLayout.EAST);
	}
	
	/**
	 * staffInfoModSetEnable()用于将员工信息修改各项全部设置为不可编辑，
	 * 在指定员工修改完信息后调用
	 */
	private void staffInfoModSetEnable(){
		this.sano.setEditable(false);
		this.saname.setEditable(false);
		this.sex.setEnabled(false);
		this.age.setEnabled(false);
		this.satype.setEnabled(false);
		this.award.setEnabled(false);
	}
	
	/**
	 * trainPerPriceModSetEnable()用于将车次各个座位类型单位里程价格
	 * 栏设置为不可编辑，在指定车次单位里程价格修改完之后调用
	 */
	private void trainPerPriceModSetEnable(){
		this.perPrice1.setEditable(false);
		this.perPrice2.setEditable(false);
		this.perPrice3.setEditable(false);
	}
	
	public void show(String sano){
		this.curSano=sano;
		this.setTitle("管理系统    管理员:"+sano);
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JMenuItem){
			if(e.getActionCommand().equals("全部车次信息"))
			{
				try {
					String sql="exec showalltrain";
					this.tableModel=this.sqlexec.getTableModel(sql);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				this.showTable.setModel(tableModel);
				isStaffInfoQuery=false;
				isTrainInfoQuery=true;
			}
			if(e.getActionCommand().equals("车次价格管理"))
			{
				this.optPanel.removeAll();
				this.optPanel.add(panel1,BorderLayout.CENTER);
				this.optPanel.updateUI();
				this.optPanel.repaint();
				this.showTable.setModel(new DefaultTableModel());
				isStaffInfoQuery=false;
				isTrainInfoQuery=false;
			}
			if(e.getActionCommand().equals("全部业务员信息"))
			{
				try {
					String sql="exec showstaffinfo";
					this.tableModel=this.sqlexec.getTableModel(sql);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				this.showTable.setModel(tableModel);
				isStaffInfoQuery=true;
				isTrainInfoQuery=false;
			}
			if(e.getActionCommand().equals("业务员信息管理"))
			{
				this.optPanel.removeAll();
				this.optPanel.add(panel2,BorderLayout.CENTER);
				this.optPanel.updateUI();
				this.optPanel.repaint();
				this.showTable.setModel(new DefaultTableModel());
				isStaffInfoQuery=false;
				isTrainInfoQuery=false;
			}
			if(e.getActionCommand().equals("车票销售信息"))
			{
				this.optPanel.removeAll();
				this.optPanel.add(panel3,BorderLayout.CENTER);
				this.optPanel.updateUI();
				this.optPanel.repaint();
				this.showTable.setModel(new DefaultTableModel());
				isStaffInfoQuery=false;
				isTrainInfoQuery=false;
			}
			if(e.getActionCommand().equals("业务员销售信息"))
			{
				this.optPanel.removeAll();
				this.optPanel.add(panel4,BorderLayout.CENTER);
				this.optPanel.updateUI();
				this.optPanel.repaint();
				this.showTable.setModel(new DefaultTableModel());
				isStaffInfoQuery=false;
				isTrainInfoQuery=false;
			}
		}
		if(e.getSource() instanceof JRadioButton){
			if(e.getActionCommand().equals("按时间查询所有车次车票销售情况")){
				this.centerPanel.removeAll();
				this.centerPanel.add(datePanel1,BorderLayout.SOUTH);
				this.centerPanel.updateUI();
				this.centerPanel.repaint();
			}
			if(e.getActionCommand().equals("指定车次查询车票销售情况")){
				this.centerPanel.removeAll();
				this.centerPanel.add(tnoPanel,BorderLayout.SOUTH);
				this.centerPanel.updateUI();
				this.centerPanel.repaint();
			}
		}
		if(e.getSource() instanceof JButton){
			if(e.getSource()==this.confrimBtn_train){
			    String selected="";  
			    Enumeration<AbstractButton> radioBtns=bp.getElements();  
			    while (radioBtns.hasMoreElements()) {  
			        AbstractButton btn = radioBtns.nextElement();  
			        if(btn.isSelected()){  
			        	selected=btn.getText();  
			            break;  
			        }  
			    }
			    if(selected.equals("按时间查询所有车次车票销售情况")){
			    	String selectdate=this.datePanel1.getSelectedData();
			    	try {
						String sql="exec incomeofalltrain '"+selectdate+"'";
						this.tableModel=this.sqlexec.getTableModel(sql);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
			    }
			    else if(selected.equals("指定车次查询车票销售情况")){
			    	String selecttno=this.tno.getText().trim();
			    	try {
						String sql="exec incomeoftrainperday "+selecttno;
						this.tableModel=this.sqlexec.getTableModel(sql);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
			    }
			    this.showTable.setModel(tableModel);
			}
			
			if(e.getSource()==this.confrimBtn_staff){
				String selectdate=this.datePanel2.getSelectedData();
		    	try {
					String sql="exec incomeofsalesman '"+selectdate+"'";
					this.tableModel=this.sqlexec.getTableModel(sql);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		    	this.showTable.setModel(tableModel);
			}
			if(e.getSource()==this.btnitemselect){
				Integer res[]=this.cblist.getSelectedItemsIndex();
				for(int i=0;i<res.length;i++){
					switch (res[i]){
						case 0:saname.setEditable(true); break;
						case 1:satype.setEnabled(true); break;
						case 2:sex.setEnabled(true); break;
						case 3:age.setEnabled(true); break;
						case 4:award.setEnabled(true); break;
					}
				}
			}
			if(e.getSource()==this.btnstaffconfirm){
				this.staffinfomodlbl.setText("");
				if(this.isStaffInfoQuery){
					int r=this.showTable.getSelectedRow();
					if(r!=-1){
						isStaffConfrim=true;
						String sano_v,saname_v,satype_v,sasex_v;
						int saage,saaward;
						sano_v=(String)showTable.getValueAt(r, 0);
						saname_v=(String)showTable.getValueAt(r, 1);
						satype_v=(String)showTable.getValueAt(r, 2);
						sasex_v=(String)showTable.getValueAt(r, 3);
						saage=Integer.parseInt(((String)showTable.getValueAt(r, 4)));
						saaward=Integer.parseInt(((String)showTable.getValueAt(r, 5)));
						sano.setText(sano_v);
						saname.setText(saname_v);
						satype.setSelectedItem(satype_v);
						sex.setSelectedItem(sasex_v);
						age.setSelectedItem(saage);
						award.setSelectedItem(saaward);
						this.staffinfomodlbl.setText("");
					}
					else{
						this.staffinfomodlbl.setText("未选择员工！");
					}
				}
				else{
					this.staffinfomodlbl.setText("未查询所有员工信息！");
				}
			}
			if(e.getSource()==this.btnupdate){
				if(isStaffConfrim){
					String sql="exec staffinfomodify "+sano.getText().trim()+","+
							saname.getText().trim()+","+(String)sex.getSelectedItem()+","+
							(String)satype.getSelectedItem()+","+(Integer)age.getSelectedItem()+","+
							(Integer)award.getSelectedItem();
					try {
						this.sqlexec.executeSql(sql);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(this,e1.getMessage());
					}
					this.staffinfomodlbl.setText("员工信息修改成功！");
					isStaffConfrim=false;
					staffInfoModSetEnable();
				}
				else{
					this.staffinfomodlbl.setText("未确定员工！");
				}
			}
			if(e.getSource()==this.btnitemselect_t){
				Integer res[]=this.cblist_t.getSelectedItemsIndex();
				for(int i=0;i<res.length;i++){
					switch (res[i]){
						case 0:perPrice1.setEditable(true); break;
						case 1:perPrice2.setEditable(true); break;
						case 2:perPrice3.setEditable(true); break;
					}
				}
			}
			if(e.getSource()==this.confirmBtn_train){
				traininfomodlbl.setText("");
				if(this.isTrainInfoQuery){
					int r=this.showTable.getSelectedRow();
					if(r!=-1){
						isTrainConfrim=true;
						String tno;
						tno=(String)showTable.getValueAt(r, 0);
						this.tnolbl.setText(tno);
						Float res[];
						try {
							res=this.sqlexec.perpriceget(tno);
							this.perPrice1.setText(res[0].toString());
							this.perPrice2.setText(res[1].toString());
							this.perPrice3.setText(res[2].toString());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					else{
						traininfomodlbl.setText("未选择车次！");
					}
				}
				else{
					traininfomodlbl.setText("未查询全部车次信息！");
				}
			}
			if(e.getSource()==this.updateBtn_train){
				if(isTrainConfrim){
					String sql="exec perpricemodify "+this.tnolbl.getText().trim()+","+
							this.perPrice1.getText()+","+
							this.perPrice2.getText()+","+
							this.perPrice3.getText();
					try {
						this.sqlexec.executeSql(sql);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(this,e1.getMessage());
					}
					isTrainConfrim=false;
					trainPerPriceModSetEnable();
					traininfomodlbl.setText("车次单位里程价格信息修改成功！");
				}
				else{
					traininfomodlbl.setText("未确认车次！");
				}
			}
		}
	}


	public void caretUpdate(CaretEvent e) {
		if(e.getSource()==this.perPrice1){
			try{
				Float.parseFloat(this.perPrice1.getText());
			}
			catch(NumberFormatException ne){
				JOptionPane.showMessageDialog(this,"二等座单位里程价格："+this.perPrice1.getText()+" 不能转成整数，请重新输入！");
			}
			finally{}
		}
		if(e.getSource()==this.perPrice2){
			try{
				Float.parseFloat(this.perPrice2.getText());
			}
			catch(NumberFormatException ne){
				JOptionPane.showMessageDialog(this,"一等座单位里程价格："+this.perPrice2.getText()+" 不能转成整数，请重新输入！");
			}
			finally{}
		}
		if(e.getSource()==this.perPrice3){
			try{
				Float.parseFloat(this.perPrice3.getText());
			}
			catch(NumberFormatException ne){
				JOptionPane.showMessageDialog(this,"商务座单位里程价格："+this.perPrice3.getText()+" 不能转成整数，请重新输入！");
			}
			finally{}
		}
	}

}
