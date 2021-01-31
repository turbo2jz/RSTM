package frames;

import java.awt.*;
import java.awt.event.*;
import java.util.EnumMap;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import dao.SqlExecute;
import dialogs.RegisterDialog;
import interfaces.LogInfoJudgeReturnValue.ReturnValueTag;
import interfaces.NewWorkday;
/**
 * LogJFrame��Ϊ��վ��Ʊ����ϵͳ��¼������
 * @author always
 *
 */
public class LogJFrame extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1479943163191422276L;
	private JPanel optJPanel;				//�������
	private JLabel userNum,passWord,infoLbl;//�û����,����,��ʾ��Ϣ��ǩ
	private JTextField userNumText;			//�û�����ı���
	private JPasswordField passWordText;	//�û������ı���(�����)
	private JButton confirm,cancel,register;//ȷ��,ȡ��,ע�ᰴť
	
	private RegisterDialog rDialog;			//ע��Ի���
	private SellJFrame sJFrame;				//��Ʊ���۴���
	private ManageJFrame mJFrame;			//������
	
	protected SqlExecute sqlexec;				//���ݿ��������
	
	public LogJFrame(){
		super("��վ��Ʊ����ϵͳ");
		init();
		rstmDateBaseRefresh();
	}
	
	/**
	* ͳһ�������壬����������֮�������ɸ����������ӽ��涼����Ҫ�ٴ���������
	*/
	private static void InitGlobalFont(Font font) {
		FontUIResource fontRes = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontRes);
			}
		}
	}
	
	/**
	 * init()���ڵ�¼�����ʼ��
	 */
	private void init(){
		//������������ʼ��
		this.userNum=new JLabel("�˻���");
		this.passWord=new JLabel("���룺");
		this.infoLbl=new JLabel("",JLabel.CENTER);
		this.infoLbl.setForeground(Color.RED);
		this.userNumText=new JTextField(15);
		this.passWordText=new JPasswordField(15);
		this.passWordText.setEchoChar('*');
		this.confirm=new JButton("ȷ��");
		this.confirm.addActionListener(this);
		this.cancel=new JButton("ȡ��");
		this.cancel.addActionListener(this);
		this.register=new JButton("ע��");
		this.register.addActionListener(this);
		
		//���ô��岼��Ϊ�߲���
		this.setLayout(new BorderLayout());
		
		//����ͼƬ����
		ImageIcon img = new ImageIcon("images/train2.jpg");//Ҫ���õı���ͼƬ
		JLabel imgLabel = new JLabel(img);//������ͼ���ڱ�ǩ�
		this.getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));
		//��������ǩ��ӵ�JFrame��LayeredPane����
		imgLabel.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		// ���ñ�����ǩ��λ��

		//�����������Ϊ͸������LayeredPane����еı�����ʾ������
		Container layPanel = this.getContentPane();
		((JPanel) layPanel).setOpaque(false);

		//�û����ֲ����
		JPanel user	=new JPanel();
		user.setLayout(new FlowLayout());
		user.setOpaque(false);//��user�����Ϊ͸��
		user.add(userNum);
		user.add(userNumText);
		//����ֲ����
		JPanel pwd = new JPanel();
		pwd.setLayout(new FlowLayout());
		pwd.setOpaque(false);//��pwd�����Ϊ͸��
		pwd.add(passWord);
		pwd.add(passWordText);
		//��ť�ֲ����
		JPanel bt = new JPanel();
		bt.setLayout(new FlowLayout());
		bt.setOpaque(false);//��bt�����Ϊ͸��
		bt.add(confirm);
		bt.add(cancel);
		bt.add(register);
		
		//�������
		optJPanel=new JPanel();
		optJPanel.setOpaque(false);//��optJPanel��Ϊ͸��
		optJPanel.setLayout(new GridLayout(4,1));//���񲼾�
		optJPanel.add(user);
		optJPanel.add(pwd);
		optJPanel.add(bt);
		optJPanel.add(infoLbl);
		
		//�����������ӵ��²�
		this.add(optJPanel,BorderLayout.SOUTH);
		this.setSize(img.getIconWidth(), img.getIconHeight());//���ô����СΪ����ͼƬ��С
		//���ô���ͼ��
		Image icon = Toolkit.getDefaultToolkit().getImage("images/train_icon.png"); 
        this.setIconImage(icon);
        //�������
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		
		//���ݿ���������ʼ��
		this.sqlexec=new SqlExecute();
		
		//�Ӵ����ʼ��
		this.rDialog=new RegisterDialog(this,"ע��",true);
		this.sJFrame=new SellJFrame(this.getWidth()*4/5,this.getHeight()*4/5,this);
		this.mJFrame=new ManageJFrame(this.getWidth()*3/5,this.getHeight()*4/5,this);
	}
	
	/**
	 * refrashLogJFrame()���ڵ�¼����ˢ��
	 * ���Ӵ��ڼ��Ի����ر�ʱ����
	 */
	public void refrashLogJFrame(){
		this.userNumText.setText("");
		this.passWordText.setText("");
		this.infoLbl.setText("");
	}
	
	/**
	 * rstmDateBaseRefresh()���ں�̨���ݿ�ˢ��
	 * �ڽ����ϵͳʱ����
	 */
	private void rstmDateBaseRefresh(){
		try {
			if(NewWorkday.isNewWorkdayJudge()){
				String sql="exec rstmrefresh";
				this.sqlexec.executeSql(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InitGlobalFont(new Font("������κ", Font.PLAIN, 16)); // ͳһ��������
		new LogJFrame();
	}

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==this.confirm){
			try {
				String sano=this.userNumText.getText(),pwd=this.passWordText.getText();
				if(sano.equals("")||pwd.equals("")){
					this.infoLbl.setText("����ȷ��д�û��������룡");
				}
				else{
					EnumMap<ReturnValueTag,Object> returnValue;
					returnValue=this.sqlexec.judgeLogInfo(sano,pwd);
					int flag=(int)returnValue.get(ReturnValueTag.Flag);
					String saType=(String)returnValue.get(ReturnValueTag.saType);
					if(flag==-1){
						this.infoLbl.setText("�û������ڣ�");
					}
					else if(flag==0){
						this.infoLbl.setText("�������");
					}
					else{
						this.infoLbl.setText("��½�ɹ�");
						if(saType.equals("ҵ��Ա")){
							this.sJFrame.show(sano);
							this.setVisible(false);
						}
						else{
							this.mJFrame.show(sano);
							this.setVisible(false);
						}
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		else if(e.getSource()== this.cancel){
			this.userNumText.setText("");
			this.passWordText.setText("");
			this.infoLbl.setText("������������ݣ�����������");
		}
		
		else if(e.getSource()==this.register){
			rDialog.show("�û�ע��");
		}
		
	}

}
