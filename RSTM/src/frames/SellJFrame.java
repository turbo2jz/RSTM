package frames;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import thread.TimeThread;
import dao.SqlExecute;
import dialogs.AboutDialog;
import interfaces.RandomTicNo;
/**
 * SellJFrame类为火车站售票管理系统子系统售票管理系统窗口类
 * @author always
 *
 */
public class SellJFrame extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2458183159896722586L;
	private JPanel leftPanel,rightpanel;//左右显示面板
	private JPanel[] optpanelset;		//左面版显示面板集(右面板中部)
	private JTable showTable;			//信息表格(左面板中部)
	private DefaultTableModel tableModel;//表格模板
	private JMenuBar menubar;	//菜单(窗体上部)
	public JLabel timeLabel;	//时间标题(左面板北部)
	protected JMenu menus[];   
	
	private LogJFrame main;		//父窗口对象

	private SqlExecute sqlexec;	//数据库操作类对象
	
	private String curSano;		//当前业务员编号
	
	//购票控件
	private JTextField IDNum,Name,sellTno,/*sStation_sell,eStation_sell,*/seatNo;
	private JComboBox<String> sStation_sellcb,eStation_sellcb;
	private JButton queryBtn,buyBtn;
	private JComboBox<String> stype;
	private JLabel sellinfoLb;
	
	private boolean isquery=false;
	
	//退票控件
	private JTextField refundTno;
	private JButton requeryBtn,confirmBtn;
	private JLabel reinfoLb;
	
	//余票查询控件
	private JTextField sStation_query,eStation_query;
	private JButton ticqueryBtn,queryallBtn;
	
	//车票信息查询控件
	private JTextField IDNum_ticInfo;
	private JButton ticInfoqueryBtn;
	
	//车次信息查询控件
	private JTextField trainNo;
	private JButton trainInfoqueryBtn;
	
	//关于信息子窗口
	private AboutDialog abDialog;
	
	
	public SellJFrame(int width, int height,LogJFrame main){
		super();
		this.setLayout(new BorderLayout());
		sqlexec=new SqlExecute();
		menuBarInit();
		optPanelInit();
		showPanelInit();
		timeLabelInit();
		abDialog=new AboutDialog(this,"关于",true);
		this.main=main;
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				SellJFrame.this.main.refrashLogJFrame();
				SellJFrame.this.main.setVisible(true);
			}
		});
		//设置窗体图标
		Image icon = Toolkit.getDefaultToolkit().getImage("images/train_icon.png"); 
		this.setIconImage(icon);
		this.setSize(width, height);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setResizable(false);
	}
	
	/**
	 * optPanelInit()用于操作面板(左面版集)初始化
	 */
	private void optPanelInit(){
		
		//左面板初始化及设置
		this.leftPanel=new JPanel();
		this.leftPanel.setLayout(new BorderLayout());
		this.add(leftPanel,BorderLayout.WEST);
		
		//操作面板集(JPanel数组初始化)
		this.optpanelset=new JPanel[8];
		
		//optpanelset[0]初始化__购票
		optpanelset[0]=new JPanel();
		optpanelset[0].setBorder(new TitledBorder("购票"));
		optpanelset[0].setLayout(new GridLayout(9,1));
		JPanel buyp1=new JPanel();
		buyp1.setLayout(new FlowLayout());
		buyp1.add(new JLabel("身份证号："));
		IDNum=new JTextField(12);
		buyp1.add(IDNum);
		optpanelset[0].add(buyp1);
		JPanel buyp2=new JPanel();
		buyp2.setLayout(new FlowLayout());
		buyp2.add(new JLabel("姓        名："));
		Name=new JTextField(12);
		buyp2.add(Name);
		optpanelset[0].add(buyp2);
		JPanel buyp3=new JPanel();
		buyp3.setLayout(new FlowLayout());
		buyp3.add(new JLabel("车票编号："));
		sellTno=new JTextField(12);
		sellTno.setEditable(false);
		buyp3.add(sellTno);
		optpanelset[0].add(buyp3);
		JPanel buyp=new JPanel();
		buyp.setLayout(new FlowLayout());
		buyp.add(new JLabel("座位编号："));
		seatNo=new JTextField(12);
		seatNo.setEditable(false);
		buyp.add(seatNo);
		optpanelset[0].add(buyp);
		
		String stname[]={};
		try {
			stname=this.sqlexec.stationget();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JPanel buyp4=new JPanel();
		buyp4.setLayout(new FlowLayout());
		buyp4.add(new JLabel("出发站点："));
		//sStation_sell=new JTextField(12);
		//buyp4.add(sStation_sell);
		sStation_sellcb=new JComboBox<String>(stname);
		sStation_sellcb.setPreferredSize(new Dimension(135, 22));
		buyp4.add(sStation_sellcb);
		optpanelset[0].add(buyp4);
		JPanel buyp5=new JPanel();
		buyp5.setLayout(new FlowLayout());
		buyp5.add(new JLabel("终到站点："));
		//eStation_sell=new JTextField(12);
		//buyp5.add(eStation_sell);
		eStation_sellcb=new JComboBox<String>(stname);
		eStation_sellcb.setPreferredSize(new Dimension(135, 22));
		buyp5.add(eStation_sellcb);
		optpanelset[0].add(buyp5);
		JPanel buyp6=new JPanel();
		String sType[]={"二等座","一等座","商务座"};
		stype=new JComboBox<String>(sType);
		stype.setPreferredSize(new Dimension(135, 22));
		buyp6.add(new JLabel("座位类型："));
		buyp6.add(stype);
		optpanelset[0].add(buyp6);
		JPanel buyp7=new JPanel();
		queryBtn=new JButton("查询");
		queryBtn.addActionListener(this);
		buyBtn=new JButton("购买");
		buyBtn.addActionListener(this);
		buyp7.add(queryBtn);
		buyp7.add(buyBtn);
		optpanelset[0].add(buyp7);
		sellinfoLb=new JLabel("",JLabel.CENTER);
		sellinfoLb.setForeground(Color.RED);
		optpanelset[0].add(sellinfoLb);
		
		//optpanelset[1]初始化__退票
		optpanelset[1]=new JPanel();
		optpanelset[1].setBorder(new TitledBorder("退票"));
		optpanelset[1].setLayout(new GridLayout(10,1));
		JPanel rep1=new JPanel();
		rep1.setLayout(new FlowLayout());
		rep1.add(new JLabel("车票编号："));
		refundTno=new JTextField(12);
		rep1.add(refundTno);
		optpanelset[1].add(rep1);
		JPanel rep2=new JPanel();
		rep2.setLayout(new FlowLayout());
		requeryBtn=new JButton("查询");
		requeryBtn.addActionListener(this);
		confirmBtn=new JButton("确认");
		confirmBtn.addActionListener(this);
		rep2.add(requeryBtn);
		rep2.add(confirmBtn);
		optpanelset[1].add(rep2);
		reinfoLb=new JLabel("",JLabel.CENTER);
		reinfoLb.setForeground(Color.RED);
		optpanelset[1].add(reinfoLb);
		
		//optpanelset[2]初始化__余票查询
		optpanelset[2]=new JPanel();
		optpanelset[2].setBorder(new TitledBorder("余票查询"));
		optpanelset[2].setLayout(new GridLayout(10,1));
		sStation_query=new JTextField(8);
		eStation_query=new JTextField(8);
		JPanel ticp1=new JPanel();
		ticp1.setLayout(new FlowLayout());
		ticp1.add(new JLabel("出发站点："));
		ticp1.add(sStation_query);
		optpanelset[2].add(ticp1);
		JPanel ticp2=new JPanel();
		ticp2.setLayout(new FlowLayout());
		ticp2.add(new JLabel("终到站点："));
		ticp2.add(eStation_query);
		optpanelset[2].add(ticp2);
		JPanel ticp3=new JPanel();
		ticp3.setLayout(new FlowLayout());
		ticqueryBtn=new JButton("查        询");
		ticqueryBtn.addActionListener(this);
		queryallBtn=new JButton("查询全部");
		queryallBtn.addActionListener(this);
		ticp3.add(ticqueryBtn);
		ticp3.add(queryallBtn);
		optpanelset[2].add(ticp3);
		
		//optpanelset[3]初始化__车票信息查询
		optpanelset[3]=new JPanel();
		optpanelset[3].setBorder(new TitledBorder("车票信息查询"));
		optpanelset[3].setLayout(new GridLayout(10,1));
		IDNum_ticInfo=new JTextField(12);
		ticInfoqueryBtn=new JButton("查询");
		ticInfoqueryBtn.addActionListener(this);
		JPanel ticinfop1=new JPanel();
		ticinfop1.setLayout(new FlowLayout());
		ticinfop1.add(new JLabel("身份证号："));
		ticinfop1.add(IDNum_ticInfo);
		optpanelset[3].add(ticinfop1);
		JPanel ticinfop2=new JPanel();
		ticinfop2.setLayout(new FlowLayout());
		ticinfop2.add(ticInfoqueryBtn);
		optpanelset[3].add(ticinfop2);
		
		//optpanelset[4]初始化__车次信息查询
		optpanelset[4]=new JPanel();
		optpanelset[4].setBorder(new TitledBorder("车次信息查询"));
		optpanelset[4].setLayout(new GridLayout(10,1));
		trainNo=new JTextField(7);
		trainInfoqueryBtn=new JButton("查询");
		trainInfoqueryBtn.addActionListener(this);
		JPanel traininfop1=new JPanel();
		traininfop1.setLayout(new FlowLayout());
		traininfop1.add(new JLabel("车次号："));
		traininfop1.add(trainNo);
		optpanelset[4].add(traininfop1);
		JPanel traininfop2=new JPanel();
		traininfop2.setLayout(new FlowLayout());
		traininfop2.add(trainInfoqueryBtn);
		optpanelset[4].add(traininfop2);
		
		//左面板初始置为购票面板
		this.leftPanel.add(optpanelset[0]);
	}
	
	/**
	 * showPanelInit()用于显示面板初始化
	 */
	private void showPanelInit(){
		this.rightpanel=new JPanel();
		this.add(rightpanel,BorderLayout.CENTER);
		rightpanel.setLayout(new BorderLayout());
		this.tableModel=new DefaultTableModel();
		this.showTable=new JTable();
		this.showTable.setModel(tableModel);
		rightpanel.add(showTable,BorderLayout.CENTER);
		rightpanel.add(new JScrollPane(showTable));
	}
	
	/**
	 * timeLabelInit()用于时间标签初始化
	 */
	private void timeLabelInit(){
		this.timeLabel=new JLabel("",SwingConstants.RIGHT);
		this.rightpanel.add(timeLabel,BorderLayout.NORTH);
		TimeThread tThread=new TimeThread(timeLabel);
		tThread.start();
	}
	
	
	/**
	 * menuBarInit()用于菜单栏初始化
	 */
	private void menuBarInit(){
		String menustr[]={"票务","查询","展示"};
		String menuitemstr[][]={{"购票","退票"},
				{"余票查询","车票信息查询","|","车次信息查询"},
				{"全部车次","|","关于"}};
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
	 * show()用于显示SellJFrame窗口
	 * @param sano 登入业务员编号
	 */
	public void show(String sano){
		this.curSano=sano;
		this.setTitle("车票销售系统    业务员:"+sano);
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JMenuItem){
			if(e.getActionCommand().equals("购票"))
			{
				this.leftPanel.removeAll();
				this.leftPanel.add(this.optpanelset[0]);
				this.leftPanel.updateUI();
				this.leftPanel.repaint();
			}
			if(e.getActionCommand().equals("退票"))
			{
				this.leftPanel.removeAll();
				this.leftPanel.add(this.optpanelset[1]);
				this.leftPanel.updateUI();
				this.leftPanel.repaint();
			}
			if(e.getActionCommand().equals("余票查询"))
			{
				this.leftPanel.removeAll();
				this.leftPanel.add(this.optpanelset[2]);
				this.leftPanel.updateUI();
				this.leftPanel.repaint();
			}
			if(e.getActionCommand().equals("车票信息查询"))
			{
				this.leftPanel.removeAll();
				this.leftPanel.add(this.optpanelset[3]);
				this.leftPanel.updateUI();
				this.leftPanel.repaint();
			}
			if(e.getActionCommand().equals("车次信息查询"))
			{
				this.leftPanel.removeAll();
				this.leftPanel.add(this.optpanelset[4]);
				this.leftPanel.updateUI();
				this.leftPanel.repaint();
			}
			if(e.getActionCommand().equals("全部车次"))
			{
				this.leftPanel.removeAll();
				this.leftPanel.updateUI();
				this.leftPanel.repaint();
				String sql="exec showalltrain";
				try {
					this.tableModel=this.sqlexec.getTableModel(sql);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				this.showTable.setModel(tableModel);
			}
			if(e.getActionCommand().equals("关于"))
			{
				this.abDialog.show("关于");
			}
		}
		
		if(e.getSource()==this.queryBtn){
			isquery=true;
			int selectindex=this.stype.getSelectedIndex();
			if(selectindex!=-1){
				String sql="exec trainquery4 "+/*this.sStation_sell.getText().trim()*/
						(String)sStation_sellcb.getSelectedItem()+","+
						/*this.eStation_sell.getText().trim()*/
						(String)eStation_sellcb.getSelectedItem()+","+
						(String)this.stype.getSelectedItem();
				try {
					this.tableModel=this.sqlexec.getTableModel(sql);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				this.showTable.setModel(tableModel);
			}
			else
				JOptionPane.showMessageDialog(this,"请座位类型！");	
		}
		
		if(e.getSource()==this.buyBtn){
			boolean flag=true;
			if(isquery==true){
				int r= showTable.getSelectedRow();
            	if(r!=-1){
            		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            		Date day=new Date(); 
            		String tno,departtime;//所选车次车次号,乘车车站所选车次发车时间
            		float price;//对应座位类型票价
            		//上述数据获取
            		tno=(String)showTable.getValueAt(r, 0);
            		departtime=(String)showTable.getValueAt(r, 2);
            		price=Float.parseFloat(((String)showTable.getValueAt(r, 7)));
            		//调用数据内提供的方法设置座位编号
            		try {
						this.seatNo.setText(Integer.toString(this.sqlexec.ticnumbering(tno)));
					} catch (Exception e2) {
						e2.printStackTrace();
					}
            		//车票随机编号
            		this.sellTno.setText(RandomTicNo.getRandomTicNum());
            		//插入语句
            		String sql="exec ticinfoinsert '"+
            				this.IDNum.getText().trim()+"','"+
            				this.Name.getText().trim()+"',"+
            				this.sellTno.getText().trim()+","+
            				tno+","+
            				/*this.sStation_sell.getText().trim()*/
            				(String)sStation_sellcb.getSelectedItem()+","+
            				/*this.eStation_sell.getText().trim()*/
            				(String)eStation_sellcb.getSelectedItem()+",'"+
            				departtime+"',"+
            				this.seatNo.getText().trim()+","+
            				(String)this.stype.getSelectedItem()+","+
            				price+",'"+
            				df.format(day)+"',"+
            				this.curSano;
            		try {
						this.sqlexec.executeSql(sql);
					} catch (Exception e1) {
						flag=false;
						JOptionPane.showMessageDialog(this,e1.getMessage());
					}
            	}
            	else{
    				JOptionPane.showMessageDialog(this,"请选择想要购买的车次！");
    			}
			}
			else{
				JOptionPane.showMessageDialog(this,"请先查询符合的车次信息！");
			}
			if(flag){
				this.sellinfoLb.setText("购票成功");
			}
			isquery=false;
		}
		
		if(e.getSource()==this.requeryBtn){
			String sql="exec ticketquerybyticno "+this.refundTno.getText().trim();
			try {
				this.tableModel=this.sqlexec.getTableModel(sql);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			this.showTable.setModel(tableModel);
			int row=this.showTable.getRowCount();
			this.reinfoLb.setText("共查到车票信息"+row+"条");
		}
		
		if(e.getSource()==this.confirmBtn){
			boolean flag=true;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		Date day=new Date(); 
			String sql="exec refoundinfoinsert "+this.refundTno.getText().trim()+",'"+
    		df.format(day)+"'";
			try {
				this.sqlexec.executeSql(sql);
			} catch (Exception e1) {
				flag=false;
				JOptionPane.showMessageDialog(this,e1.getMessage());
			}
			if(flag){
				this.reinfoLb.setText("退票成功");
			}
		}
		
		if(e.getSource()==this.trainInfoqueryBtn){
			String sql="exec stationoftrainquary "+this.trainNo.getText().trim();
			try {
				this.tableModel=this.sqlexec.getTableModel(sql);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			this.showTable.setModel(tableModel);
		}
		
		if(e.getSource()==this.queryallBtn){
			String sql="exec trainquery3";
			try {
				this.tableModel=this.sqlexec.getTableModel(sql);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			this.showTable.setModel(tableModel);
		}
		
		if(e.getSource()==this.ticInfoqueryBtn){
			String sql="exec ticketquerybyidnum "+this.IDNum_ticInfo.getText().trim();
			try {
				this.tableModel=this.sqlexec.getTableModel(sql);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			this.showTable.setModel(tableModel);
		}
		
		if(e.getSource()==this.ticqueryBtn){
			String sql="exec trainquery3forone "+this.sStation_query.getText().trim()+","+
					this.eStation_query.getText().trim();
			try {
				this.tableModel=this.sqlexec.getTableModel(sql);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			this.showTable.setModel(tableModel);
		}
	}

}
