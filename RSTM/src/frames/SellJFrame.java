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
 * SellJFrame��Ϊ��վ��Ʊ����ϵͳ��ϵͳ��Ʊ����ϵͳ������
 * @author always
 *
 */
public class SellJFrame extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2458183159896722586L;
	private JPanel leftPanel,rightpanel;//������ʾ���
	private JPanel[] optpanelset;		//�������ʾ��弯(������в�)
	private JTable showTable;			//��Ϣ���(������в�)
	private DefaultTableModel tableModel;//���ģ��
	private JMenuBar menubar;	//�˵�(�����ϲ�)
	public JLabel timeLabel;	//ʱ�����(����山��)
	protected JMenu menus[];   
	
	private LogJFrame main;		//�����ڶ���

	private SqlExecute sqlexec;	//���ݿ���������
	
	private String curSano;		//��ǰҵ��Ա���
	
	//��Ʊ�ؼ�
	private JTextField IDNum,Name,sellTno,/*sStation_sell,eStation_sell,*/seatNo;
	private JComboBox<String> sStation_sellcb,eStation_sellcb;
	private JButton queryBtn,buyBtn;
	private JComboBox<String> stype;
	private JLabel sellinfoLb;
	
	private boolean isquery=false;
	
	//��Ʊ�ؼ�
	private JTextField refundTno;
	private JButton requeryBtn,confirmBtn;
	private JLabel reinfoLb;
	
	//��Ʊ��ѯ�ؼ�
	private JTextField sStation_query,eStation_query;
	private JButton ticqueryBtn,queryallBtn;
	
	//��Ʊ��Ϣ��ѯ�ؼ�
	private JTextField IDNum_ticInfo;
	private JButton ticInfoqueryBtn;
	
	//������Ϣ��ѯ�ؼ�
	private JTextField trainNo;
	private JButton trainInfoqueryBtn;
	
	//������Ϣ�Ӵ���
	private AboutDialog abDialog;
	
	
	public SellJFrame(int width, int height,LogJFrame main){
		super();
		this.setLayout(new BorderLayout());
		sqlexec=new SqlExecute();
		menuBarInit();
		optPanelInit();
		showPanelInit();
		timeLabelInit();
		abDialog=new AboutDialog(this,"����",true);
		this.main=main;
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				SellJFrame.this.main.refrashLogJFrame();
				SellJFrame.this.main.setVisible(true);
			}
		});
		//���ô���ͼ��
		Image icon = Toolkit.getDefaultToolkit().getImage("images/train_icon.png"); 
		this.setIconImage(icon);
		this.setSize(width, height);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setResizable(false);
	}
	
	/**
	 * optPanelInit()���ڲ������(����漯)��ʼ��
	 */
	private void optPanelInit(){
		
		//������ʼ��������
		this.leftPanel=new JPanel();
		this.leftPanel.setLayout(new BorderLayout());
		this.add(leftPanel,BorderLayout.WEST);
		
		//������弯(JPanel�����ʼ��)
		this.optpanelset=new JPanel[8];
		
		//optpanelset[0]��ʼ��__��Ʊ
		optpanelset[0]=new JPanel();
		optpanelset[0].setBorder(new TitledBorder("��Ʊ"));
		optpanelset[0].setLayout(new GridLayout(9,1));
		JPanel buyp1=new JPanel();
		buyp1.setLayout(new FlowLayout());
		buyp1.add(new JLabel("���֤�ţ�"));
		IDNum=new JTextField(12);
		buyp1.add(IDNum);
		optpanelset[0].add(buyp1);
		JPanel buyp2=new JPanel();
		buyp2.setLayout(new FlowLayout());
		buyp2.add(new JLabel("��        ����"));
		Name=new JTextField(12);
		buyp2.add(Name);
		optpanelset[0].add(buyp2);
		JPanel buyp3=new JPanel();
		buyp3.setLayout(new FlowLayout());
		buyp3.add(new JLabel("��Ʊ��ţ�"));
		sellTno=new JTextField(12);
		sellTno.setEditable(false);
		buyp3.add(sellTno);
		optpanelset[0].add(buyp3);
		JPanel buyp=new JPanel();
		buyp.setLayout(new FlowLayout());
		buyp.add(new JLabel("��λ��ţ�"));
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
		buyp4.add(new JLabel("����վ�㣺"));
		//sStation_sell=new JTextField(12);
		//buyp4.add(sStation_sell);
		sStation_sellcb=new JComboBox<String>(stname);
		sStation_sellcb.setPreferredSize(new Dimension(135, 22));
		buyp4.add(sStation_sellcb);
		optpanelset[0].add(buyp4);
		JPanel buyp5=new JPanel();
		buyp5.setLayout(new FlowLayout());
		buyp5.add(new JLabel("�յ�վ�㣺"));
		//eStation_sell=new JTextField(12);
		//buyp5.add(eStation_sell);
		eStation_sellcb=new JComboBox<String>(stname);
		eStation_sellcb.setPreferredSize(new Dimension(135, 22));
		buyp5.add(eStation_sellcb);
		optpanelset[0].add(buyp5);
		JPanel buyp6=new JPanel();
		String sType[]={"������","һ����","������"};
		stype=new JComboBox<String>(sType);
		stype.setPreferredSize(new Dimension(135, 22));
		buyp6.add(new JLabel("��λ���ͣ�"));
		buyp6.add(stype);
		optpanelset[0].add(buyp6);
		JPanel buyp7=new JPanel();
		queryBtn=new JButton("��ѯ");
		queryBtn.addActionListener(this);
		buyBtn=new JButton("����");
		buyBtn.addActionListener(this);
		buyp7.add(queryBtn);
		buyp7.add(buyBtn);
		optpanelset[0].add(buyp7);
		sellinfoLb=new JLabel("",JLabel.CENTER);
		sellinfoLb.setForeground(Color.RED);
		optpanelset[0].add(sellinfoLb);
		
		//optpanelset[1]��ʼ��__��Ʊ
		optpanelset[1]=new JPanel();
		optpanelset[1].setBorder(new TitledBorder("��Ʊ"));
		optpanelset[1].setLayout(new GridLayout(10,1));
		JPanel rep1=new JPanel();
		rep1.setLayout(new FlowLayout());
		rep1.add(new JLabel("��Ʊ��ţ�"));
		refundTno=new JTextField(12);
		rep1.add(refundTno);
		optpanelset[1].add(rep1);
		JPanel rep2=new JPanel();
		rep2.setLayout(new FlowLayout());
		requeryBtn=new JButton("��ѯ");
		requeryBtn.addActionListener(this);
		confirmBtn=new JButton("ȷ��");
		confirmBtn.addActionListener(this);
		rep2.add(requeryBtn);
		rep2.add(confirmBtn);
		optpanelset[1].add(rep2);
		reinfoLb=new JLabel("",JLabel.CENTER);
		reinfoLb.setForeground(Color.RED);
		optpanelset[1].add(reinfoLb);
		
		//optpanelset[2]��ʼ��__��Ʊ��ѯ
		optpanelset[2]=new JPanel();
		optpanelset[2].setBorder(new TitledBorder("��Ʊ��ѯ"));
		optpanelset[2].setLayout(new GridLayout(10,1));
		sStation_query=new JTextField(8);
		eStation_query=new JTextField(8);
		JPanel ticp1=new JPanel();
		ticp1.setLayout(new FlowLayout());
		ticp1.add(new JLabel("����վ�㣺"));
		ticp1.add(sStation_query);
		optpanelset[2].add(ticp1);
		JPanel ticp2=new JPanel();
		ticp2.setLayout(new FlowLayout());
		ticp2.add(new JLabel("�յ�վ�㣺"));
		ticp2.add(eStation_query);
		optpanelset[2].add(ticp2);
		JPanel ticp3=new JPanel();
		ticp3.setLayout(new FlowLayout());
		ticqueryBtn=new JButton("��        ѯ");
		ticqueryBtn.addActionListener(this);
		queryallBtn=new JButton("��ѯȫ��");
		queryallBtn.addActionListener(this);
		ticp3.add(ticqueryBtn);
		ticp3.add(queryallBtn);
		optpanelset[2].add(ticp3);
		
		//optpanelset[3]��ʼ��__��Ʊ��Ϣ��ѯ
		optpanelset[3]=new JPanel();
		optpanelset[3].setBorder(new TitledBorder("��Ʊ��Ϣ��ѯ"));
		optpanelset[3].setLayout(new GridLayout(10,1));
		IDNum_ticInfo=new JTextField(12);
		ticInfoqueryBtn=new JButton("��ѯ");
		ticInfoqueryBtn.addActionListener(this);
		JPanel ticinfop1=new JPanel();
		ticinfop1.setLayout(new FlowLayout());
		ticinfop1.add(new JLabel("���֤�ţ�"));
		ticinfop1.add(IDNum_ticInfo);
		optpanelset[3].add(ticinfop1);
		JPanel ticinfop2=new JPanel();
		ticinfop2.setLayout(new FlowLayout());
		ticinfop2.add(ticInfoqueryBtn);
		optpanelset[3].add(ticinfop2);
		
		//optpanelset[4]��ʼ��__������Ϣ��ѯ
		optpanelset[4]=new JPanel();
		optpanelset[4].setBorder(new TitledBorder("������Ϣ��ѯ"));
		optpanelset[4].setLayout(new GridLayout(10,1));
		trainNo=new JTextField(7);
		trainInfoqueryBtn=new JButton("��ѯ");
		trainInfoqueryBtn.addActionListener(this);
		JPanel traininfop1=new JPanel();
		traininfop1.setLayout(new FlowLayout());
		traininfop1.add(new JLabel("���κţ�"));
		traininfop1.add(trainNo);
		optpanelset[4].add(traininfop1);
		JPanel traininfop2=new JPanel();
		traininfop2.setLayout(new FlowLayout());
		traininfop2.add(trainInfoqueryBtn);
		optpanelset[4].add(traininfop2);
		
		//������ʼ��Ϊ��Ʊ���
		this.leftPanel.add(optpanelset[0]);
	}
	
	/**
	 * showPanelInit()������ʾ����ʼ��
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
	 * timeLabelInit()����ʱ���ǩ��ʼ��
	 */
	private void timeLabelInit(){
		this.timeLabel=new JLabel("",SwingConstants.RIGHT);
		this.rightpanel.add(timeLabel,BorderLayout.NORTH);
		TimeThread tThread=new TimeThread(timeLabel);
		tThread.start();
	}
	
	
	/**
	 * menuBarInit()���ڲ˵�����ʼ��
	 */
	private void menuBarInit(){
		String menustr[]={"Ʊ��","��ѯ","չʾ"};
		String menuitemstr[][]={{"��Ʊ","��Ʊ"},
				{"��Ʊ��ѯ","��Ʊ��Ϣ��ѯ","|","������Ϣ��ѯ"},
				{"ȫ������","|","����"}};
		this.menubar=new JMenuBar();
        JMenuBar menubar = new JMenuBar();                 //�˵���
        this.setJMenuBar(menubar);                         //�������Ӳ˵���
        this.menus = new JMenu[menustr.length];            //�˵�����
        JMenuItem menuitems[][] = new JMenuItem[menuitemstr.length][]; //�˵����ά����
        for (int i=0; i<menuitemstr.length; i++)           //��Ӳ˵��Ͳ˵���
        {
            this.menus[i] = new JMenu(menustr[i]);         //�˵�
            menubar.add(this.menus[i]);
            //�˵����м���˵�
            menuitems[i] = new JMenuItem[menuitemstr[i].length];
            for (int j=0; j<menuitemstr[i].length; j++)
                if (menuitemstr[i][j].equals("|"))
                    this.menus[i].addSeparator();          //�ӷָ���
                else 
                {   menuitems[i][j] = new JMenuItem(menuitemstr[i][j]); //�����˵���
                    this.menus[i].add(menuitems[i][j]);    //�˵�����뵽�˵�
                    menuitems[i][j].addActionListener(this);//�˵���ע�ᶯ���¼�������
                }
        }
        this.add(this.menubar);
	}
	
	/**
	 * show()������ʾSellJFrame����
	 * @param sano ����ҵ��Ա���
	 */
	public void show(String sano){
		this.curSano=sano;
		this.setTitle("��Ʊ����ϵͳ    ҵ��Ա:"+sano);
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JMenuItem){
			if(e.getActionCommand().equals("��Ʊ"))
			{
				this.leftPanel.removeAll();
				this.leftPanel.add(this.optpanelset[0]);
				this.leftPanel.updateUI();
				this.leftPanel.repaint();
			}
			if(e.getActionCommand().equals("��Ʊ"))
			{
				this.leftPanel.removeAll();
				this.leftPanel.add(this.optpanelset[1]);
				this.leftPanel.updateUI();
				this.leftPanel.repaint();
			}
			if(e.getActionCommand().equals("��Ʊ��ѯ"))
			{
				this.leftPanel.removeAll();
				this.leftPanel.add(this.optpanelset[2]);
				this.leftPanel.updateUI();
				this.leftPanel.repaint();
			}
			if(e.getActionCommand().equals("��Ʊ��Ϣ��ѯ"))
			{
				this.leftPanel.removeAll();
				this.leftPanel.add(this.optpanelset[3]);
				this.leftPanel.updateUI();
				this.leftPanel.repaint();
			}
			if(e.getActionCommand().equals("������Ϣ��ѯ"))
			{
				this.leftPanel.removeAll();
				this.leftPanel.add(this.optpanelset[4]);
				this.leftPanel.updateUI();
				this.leftPanel.repaint();
			}
			if(e.getActionCommand().equals("ȫ������"))
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
			if(e.getActionCommand().equals("����"))
			{
				this.abDialog.show("����");
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
				JOptionPane.showMessageDialog(this,"����λ���ͣ�");	
		}
		
		if(e.getSource()==this.buyBtn){
			boolean flag=true;
			if(isquery==true){
				int r= showTable.getSelectedRow();
            	if(r!=-1){
            		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            		Date day=new Date(); 
            		String tno,departtime;//��ѡ���γ��κ�,�˳���վ��ѡ���η���ʱ��
            		float price;//��Ӧ��λ����Ʊ��
            		//�������ݻ�ȡ
            		tno=(String)showTable.getValueAt(r, 0);
            		departtime=(String)showTable.getValueAt(r, 2);
            		price=Float.parseFloat(((String)showTable.getValueAt(r, 7)));
            		//�����������ṩ�ķ���������λ���
            		try {
						this.seatNo.setText(Integer.toString(this.sqlexec.ticnumbering(tno)));
					} catch (Exception e2) {
						e2.printStackTrace();
					}
            		//��Ʊ������
            		this.sellTno.setText(RandomTicNo.getRandomTicNum());
            		//�������
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
    				JOptionPane.showMessageDialog(this,"��ѡ����Ҫ����ĳ��Σ�");
    			}
			}
			else{
				JOptionPane.showMessageDialog(this,"���Ȳ�ѯ���ϵĳ�����Ϣ��");
			}
			if(flag){
				this.sellinfoLb.setText("��Ʊ�ɹ�");
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
			this.reinfoLb.setText("���鵽��Ʊ��Ϣ"+row+"��");
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
				this.reinfoLb.setText("��Ʊ�ɹ�");
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
