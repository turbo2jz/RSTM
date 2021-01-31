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
 * ManageJFrame��Ϊ��վ��Ʊ����ϵͳ��ϵͳ����ϵͳ������
 * @author always
 *
 */
public class ManageJFrame extends JFrame implements ActionListener,CaretListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7226973959769654742L;
	private JMenuBar menubar;	//�˵�(�����ϲ�)
	protected JMenu menus[]; 	//�˵�����
	
	private LogJFrame main;		//�����ڶ���

	private SqlExecute sqlexec;	//���ݿ���������
	
	@SuppressWarnings("unused")
	private String curSano;		//��ǰҵ��Ա���
	
	private JPanel optPanel;	//�������(���ò������)
	private JPanel showpanel;	//�ײ������ʾ���
	private JPanel panel1,panel2,panel3,panel4;
	private JTable showTable;
	private DefaultTableModel tableModel;
	
	//panel1���
	private JTextField perPrice1,perPrice2,perPrice3;
	private CheckBoxList cblist_t;
	private JButton btnitemselect_t,confirmBtn_train,updateBtn_train;
	private JLabel tnolbl;
	private boolean isTrainInfoQuery=false,isTrainConfrim=false;
	private JLabel traininfomodlbl;
	
	//panel2���
	private JTextField sano,saname;
	private JComboBox<String> satype,sex;
	private JComboBox<Integer> age,award;
	private CheckBoxList cblist;
	private JButton btnitemselect,btnstaffconfirm,btnupdate;
	private JLabel staffinfomodlbl;
	private boolean isStaffInfoQuery=false,isStaffConfrim=false;
	
	//panel3���
	private ButtonGroup bp;
	private JPanel centerPanel;
	private JPanel tnoPanel;
	private JTextField tno;
	private JButton confrimBtn_train;
	private DatePanel datePanel1;
	
	//panel4���
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
		//��ʼ���������ʼ��Ϊ���μ۸����
		this.optPanel.add(panel1,BorderLayout.CENTER);
		//���ô���ͼ��
		Image icon = Toolkit.getDefaultToolkit().getImage("images/train_icon.png"); 
		this.setIconImage(icon);
		this.setSize(width, height);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setResizable(false);
	}
	
	
	/**
	 * menuBarInit()���ڲ˵�����ʼ��
	 */
	private void menuBarInit(){
		String menustr[]={"���ι���","ҵ��Ա����","������Ϣ��ѯ"};
		String menuitemstr[][]={{"ȫ��������Ϣ","���μ۸����"},
				{"ȫ��ҵ��Ա��Ϣ","ҵ��Ա��Ϣ����"},
				{"��Ʊ������Ϣ","ҵ��Ա������Ϣ"}};
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
	 * showPanelInit()������ʾ����ʼ��
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
	 * optPanelInit()���ڲ�������ʼ��
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
		panel1_l.setBorder(new TitledBorder("���ε�λ��̼۸����"));
		panel1_l.setLayout(new BorderLayout());
		JPanel panel1_l1=new JPanel();
		panel1_l1.setLayout(new GridLayout(4,1));
		JPanel infopanel=new JPanel();
		infopanel.add(new JLabel("ѡ�񳵴�Ϊ��"));
		tnolbl=new JLabel();
		infopanel.add(tnolbl);
		panel1_l1.add(infopanel);
		JPanel pp1=new JPanel();
		pp1.add(new JLabel("��������λ��̼۸�(Ԫ/KM)��"));
		perPrice1=new JTextField(5);
		perPrice1.setEditable(false);
		perPrice1.addCaretListener(this);
		pp1.add(perPrice1);
		panel1_l1.add(pp1);
		JPanel pp2=new JPanel();
		pp2.add(new JLabel("һ������λ��̼۸�(Ԫ/KM)��"));
		perPrice2=new JTextField(5);
		perPrice2.setEditable(false);
		perPrice2.addCaretListener(this);
		pp2.add(perPrice2);
		panel1_l1.add(pp2);
		JPanel pp3=new JPanel();
		pp3.add(new JLabel("��������λ��̼۸�(Ԫ/KM)��"));
		perPrice3=new JTextField(5);
		perPrice3.setEditable(false);
		perPrice3.addCaretListener(this);
		pp3.add(perPrice3);
		panel1_l1.add(pp3);
		panel1_l.add(panel1_l1,BorderLayout.CENTER);
		confirmBtn_train=new JButton("��ѯ");
		confirmBtn_train.addActionListener(this);
		updateBtn_train=new JButton("�޸�");
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
		panel1_r.setBorder(new TitledBorder("�޸�ѡ��"));
		panel1_r.setLayout(new BorderLayout());
		String Items[]={"������","һ����","������"};
		cblist_t=new CheckBoxList(Items);
		JScrollPane ps = new JScrollPane();
		ps.getViewport().add(cblist_t);
		panel1_r.add(ps,BorderLayout.CENTER);
		btnitemselect_t=new JButton("ȷ��");
		btnitemselect_t.addActionListener(this);
		JPanel btnPanel=new JPanel();
		btnPanel.add(btnitemselect_t);
		panel1_r.add(btnPanel,BorderLayout.SOUTH);
		panel1.add(panel1_r,BorderLayout.EAST);
	}
	
	private void panel2init(){
		//ҵ��Ա��Ϣ�޸����
		JPanel panel2_l=new JPanel();
		//ҵ��Ա������Ϣ�޸�ģ�����
		panel2_l.setBorder(new TitledBorder("ҵ��Ա��Ϣ����"));
		panel2_l.setLayout(new BorderLayout());
		JPanel panel2_l1=new JPanel();
		panel2_l1.setLayout(new GridLayout(4,1));
		//���(p1)
		JPanel p1=new JPanel();
		p1.add(new JLabel("��ţ�"));
		this.sano=new JTextField(12);
		sano.setEditable(false);
		p1.add(sano);
		panel2_l1.add(p1);
		//����(p2)
		JPanel p2=new JPanel();
		p2.add(new JLabel("������"));
		this.saname=new JTextField(12);
		saname.setEditable(false);
		p2.add(saname);
		panel2_l1.add(p2);
		//�Ա�(p3)
		JPanel p3=new JPanel();
		p3.add(new JLabel("�Ա�"));
		String[] sex_str={"��","Ů"};
		this.sex=new JComboBox<String>(sex_str);
		sex.setSelectedIndex(0);
		sex.setEnabled(false);
		p3.add(sex);
		//����(p3)
		p3.add(new JLabel("���䣺"));
		this.age=new JComboBox<Integer>();
		for(int i=20;i<=60;i++){
			age.addItem(i);
		}
		age.setSelectedIndex(0);
		age.setEnabled(false);
		p3.add(age);
		panel2_l1.add(p3);
		JPanel p4=new JPanel();
		//����(p4)
		p4.add(new JLabel("���ͣ�"));
		String[] satype_str={"����Ա","ҵ��Ա"};
		this.satype=new JComboBox<String>(satype_str);
		satype.setSelectedIndex(0);
		satype.setEnabled(false);
		p4.add(satype);
		//�����ȼ�(p4)
		p4.add(new JLabel("�����ȼ���"));
		this.award=new JComboBox<Integer>();
		for(int i=1;i<=5;i++){
			award.addItem(i);
		}
		award.setSelectedIndex(0);
		award.setEnabled(false);
		p4.add(award);
		panel2_l1.add(p4);
		panel2_l.add(panel2_l1,BorderLayout.CENTER);
		
		//ҵ��Ա��Ϣ�޸İ�ť����ʾ���
		JPanel panel2_l2=new JPanel();
		panel2_l2.setLayout(new BorderLayout());
		//��ť
		btnstaffconfirm=new JButton("ȷ���޸�Ա��");
		btnstaffconfirm.addActionListener(this);
		btnupdate=new JButton("������Ϣ");
		btnupdate.addActionListener(this);
		JPanel modbtnpanel=new JPanel();
		modbtnpanel.add(btnstaffconfirm);
		modbtnpanel.add(btnupdate);
		panel2_l2.add(modbtnpanel,BorderLayout.CENTER);
		//��Ϣ��ʾ��ǩ
		this.staffinfomodlbl=new JLabel("");
		JPanel lblPanel=new JPanel();
		lblPanel.add(staffinfomodlbl);
		panel2_l2.add(lblPanel,BorderLayout.SOUTH);
		panel2_l.add(panel2_l2,BorderLayout.EAST);
		panel2=new JPanel();
		panel2.setLayout(new BorderLayout());
		panel2.add(panel2_l,BorderLayout.CENTER);
		JPanel panel2_r=new JPanel();
		
		//������޸�ѡ��ѡ�����
		panel2_r.setBorder(new TitledBorder("�޸�ѡ��"));
		panel2_r.setLayout(new BorderLayout());
		String Items[]={"����","����","�Ա�","����","�����ȼ�"};
		cblist=new CheckBoxList(Items);
		JScrollPane ps = new JScrollPane();
		ps.getViewport().add(cblist);
		panel2_r.add(ps,BorderLayout.CENTER);
		btnitemselect=new JButton("ȷ��");
		btnitemselect.addActionListener(this);
		JPanel btnPanel=new JPanel();
		btnPanel.add(btnitemselect);
		panel2_r.add(btnPanel,BorderLayout.SOUTH);
		panel2.add(panel2_r,BorderLayout.EAST);
	}
	
	private void panel3init(){
		String str[]={"��ʱ���ѯ���г��γ�Ʊ�������","ָ�����β�ѯ��Ʊ�������"};
		JRadioButton rbtn[]=new JRadioButton[str.length];
		this.panel3=new JPanel();
		datePanel1=new DatePanel();
		panel3.setBorder(new TitledBorder("��Ʊ������Ϣ��ѯ"));
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
		tnoPanel.add(new JLabel("���κţ�"));
		tno=new JTextField(10);
		tnoPanel.add(tno);
		centerPanel.add(datePanel1,BorderLayout.SOUTH);
		panel3.add(centerPanel,BorderLayout.CENTER);
		confrimBtn_train=new JButton("ȷ��");
		confrimBtn_train.addActionListener(this);
		JPanel btPanel=new JPanel();
		btPanel.setLayout(new BorderLayout());
		btPanel.add(confrimBtn_train,BorderLayout.CENTER);
		panel3.add(btPanel,BorderLayout.EAST);
	}
	
	private void panel4init(){
		this.panel4=new JPanel();
		panel4.setBorder(new TitledBorder("ҵ��Ա������Ϣ��ѯ"));
		panel4.setLayout(new BorderLayout(1,2));
		datePanel2=new DatePanel("��������Ҫ��ѯ�����ڣ�");
		panel4.add(datePanel2,BorderLayout.CENTER);
		confrimBtn_staff=new JButton("ȷ��");
		confrimBtn_staff.addActionListener(this);
		JPanel btnPanel=new JPanel();
		btnPanel.add(confrimBtn_staff);
		panel4.add(btnPanel,BorderLayout.EAST);
	}
	
	/**
	 * staffInfoModSetEnable()���ڽ�Ա����Ϣ�޸ĸ���ȫ������Ϊ���ɱ༭��
	 * ��ָ��Ա���޸�����Ϣ�����
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
	 * trainPerPriceModSetEnable()���ڽ����θ�����λ���͵�λ��̼۸�
	 * ������Ϊ���ɱ༭����ָ�����ε�λ��̼۸��޸���֮�����
	 */
	private void trainPerPriceModSetEnable(){
		this.perPrice1.setEditable(false);
		this.perPrice2.setEditable(false);
		this.perPrice3.setEditable(false);
	}
	
	public void show(String sano){
		this.curSano=sano;
		this.setTitle("����ϵͳ    ����Ա:"+sano);
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JMenuItem){
			if(e.getActionCommand().equals("ȫ��������Ϣ"))
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
			if(e.getActionCommand().equals("���μ۸����"))
			{
				this.optPanel.removeAll();
				this.optPanel.add(panel1,BorderLayout.CENTER);
				this.optPanel.updateUI();
				this.optPanel.repaint();
				this.showTable.setModel(new DefaultTableModel());
				isStaffInfoQuery=false;
				isTrainInfoQuery=false;
			}
			if(e.getActionCommand().equals("ȫ��ҵ��Ա��Ϣ"))
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
			if(e.getActionCommand().equals("ҵ��Ա��Ϣ����"))
			{
				this.optPanel.removeAll();
				this.optPanel.add(panel2,BorderLayout.CENTER);
				this.optPanel.updateUI();
				this.optPanel.repaint();
				this.showTable.setModel(new DefaultTableModel());
				isStaffInfoQuery=false;
				isTrainInfoQuery=false;
			}
			if(e.getActionCommand().equals("��Ʊ������Ϣ"))
			{
				this.optPanel.removeAll();
				this.optPanel.add(panel3,BorderLayout.CENTER);
				this.optPanel.updateUI();
				this.optPanel.repaint();
				this.showTable.setModel(new DefaultTableModel());
				isStaffInfoQuery=false;
				isTrainInfoQuery=false;
			}
			if(e.getActionCommand().equals("ҵ��Ա������Ϣ"))
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
			if(e.getActionCommand().equals("��ʱ���ѯ���г��γ�Ʊ�������")){
				this.centerPanel.removeAll();
				this.centerPanel.add(datePanel1,BorderLayout.SOUTH);
				this.centerPanel.updateUI();
				this.centerPanel.repaint();
			}
			if(e.getActionCommand().equals("ָ�����β�ѯ��Ʊ�������")){
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
			    if(selected.equals("��ʱ���ѯ���г��γ�Ʊ�������")){
			    	String selectdate=this.datePanel1.getSelectedData();
			    	try {
						String sql="exec incomeofalltrain '"+selectdate+"'";
						this.tableModel=this.sqlexec.getTableModel(sql);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
			    }
			    else if(selected.equals("ָ�����β�ѯ��Ʊ�������")){
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
						this.staffinfomodlbl.setText("δѡ��Ա����");
					}
				}
				else{
					this.staffinfomodlbl.setText("δ��ѯ����Ա����Ϣ��");
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
					this.staffinfomodlbl.setText("Ա����Ϣ�޸ĳɹ���");
					isStaffConfrim=false;
					staffInfoModSetEnable();
				}
				else{
					this.staffinfomodlbl.setText("δȷ��Ա����");
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
						traininfomodlbl.setText("δѡ�񳵴Σ�");
					}
				}
				else{
					traininfomodlbl.setText("δ��ѯȫ��������Ϣ��");
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
					traininfomodlbl.setText("���ε�λ��̼۸���Ϣ�޸ĳɹ���");
				}
				else{
					traininfomodlbl.setText("δȷ�ϳ��Σ�");
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
				JOptionPane.showMessageDialog(this,"��������λ��̼۸�"+this.perPrice1.getText()+" ����ת�����������������룡");
			}
			finally{}
		}
		if(e.getSource()==this.perPrice2){
			try{
				Float.parseFloat(this.perPrice2.getText());
			}
			catch(NumberFormatException ne){
				JOptionPane.showMessageDialog(this,"һ������λ��̼۸�"+this.perPrice2.getText()+" ����ת�����������������룡");
			}
			finally{}
		}
		if(e.getSource()==this.perPrice3){
			try{
				Float.parseFloat(this.perPrice3.getText());
			}
			catch(NumberFormatException ne){
				JOptionPane.showMessageDialog(this,"��������λ��̼۸�"+this.perPrice3.getText()+" ����ת�����������������룡");
			}
			finally{}
		}
	}

}
