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
 * LogJFrame类为火车站售票管理系统登录窗口类
 * @author always
 *
 */
public class LogJFrame extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1479943163191422276L;
	private JPanel optJPanel;				//操作面板
	private JLabel userNum,passWord,infoLbl;//用户编号,密码,提示信息标签
	private JTextField userNumText;			//用户编号文本框
	private JPasswordField passWordText;	//用户密码文本框(密码框)
	private JButton confirm,cancel,register;//确认,取消,注册按钮
	
	private RegisterDialog rDialog;			//注册对话框
	private SellJFrame sJFrame;				//车票销售窗口
	private ManageJFrame mJFrame;			//管理窗口
	
	protected SqlExecute sqlexec;				//数据库操作对象
	
	public LogJFrame(){
		super("火车站售票管理系统");
		init();
		rstmDateBaseRefresh();
	}
	
	/**
	* 统一设置字体，父界面设置之后，所有由父界面进入的子界面都不需要再次设置字体
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
	 * init()用于登录窗体初始化
	 */
	private void init(){
		//窗体包含组件初始化
		this.userNum=new JLabel("账户：");
		this.passWord=new JLabel("密码：");
		this.infoLbl=new JLabel("",JLabel.CENTER);
		this.infoLbl.setForeground(Color.RED);
		this.userNumText=new JTextField(15);
		this.passWordText=new JPasswordField(15);
		this.passWordText.setEchoChar('*');
		this.confirm=new JButton("确定");
		this.confirm.addActionListener(this);
		this.cancel=new JButton("取消");
		this.cancel.addActionListener(this);
		this.register=new JButton("注册");
		this.register.addActionListener(this);
		
		//设置窗体布局为边布局
		this.setLayout(new BorderLayout());
		
		//背景图片设置
		ImageIcon img = new ImageIcon("images/train2.jpg");//要设置的背景图片
		JLabel imgLabel = new JLabel(img);//将背景图放在标签里。
		this.getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));
		//将背景标签添加到JFrame的LayeredPane面板里。
		imgLabel.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		// 设置背景标签的位置

		//将内容面板设为透明。将LayeredPane面板中的背景显示出来。
		Container layPanel = this.getContentPane();
		((JPanel) layPanel).setOpaque(false);

		//用户名局部面板
		JPanel user	=new JPanel();
		user.setLayout(new FlowLayout());
		user.setOpaque(false);//将user面板设为透明
		user.add(userNum);
		user.add(userNumText);
		//密码局部面板
		JPanel pwd = new JPanel();
		pwd.setLayout(new FlowLayout());
		pwd.setOpaque(false);//将pwd面板设为透明
		pwd.add(passWord);
		pwd.add(passWordText);
		//按钮局部面板
		JPanel bt = new JPanel();
		bt.setLayout(new FlowLayout());
		bt.setOpaque(false);//将bt面板设为透明
		bt.add(confirm);
		bt.add(cancel);
		bt.add(register);
		
		//操作面板
		optJPanel=new JPanel();
		optJPanel.setOpaque(false);//将optJPanel设为透明
		optJPanel.setLayout(new GridLayout(4,1));//网格布局
		optJPanel.add(user);
		optJPanel.add(pwd);
		optJPanel.add(bt);
		optJPanel.add(infoLbl);
		
		//将操作面板添加到下部
		this.add(optJPanel,BorderLayout.SOUTH);
		this.setSize(img.getIconWidth(), img.getIconHeight());//设置窗体大小为背景图片大小
		//设置窗体图标
		Image icon = Toolkit.getDefaultToolkit().getImage("images/train_icon.png"); 
        this.setIconImage(icon);
        //窗体居中
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		
		//数据库操作对象初始化
		this.sqlexec=new SqlExecute();
		
		//子窗体初始化
		this.rDialog=new RegisterDialog(this,"注册",true);
		this.sJFrame=new SellJFrame(this.getWidth()*4/5,this.getHeight()*4/5,this);
		this.mJFrame=new ManageJFrame(this.getWidth()*3/5,this.getHeight()*4/5,this);
	}
	
	/**
	 * refrashLogJFrame()用于登录窗体刷新
	 * 由子窗口及对话窗关闭时调用
	 */
	public void refrashLogJFrame(){
		this.userNumText.setText("");
		this.passWordText.setText("");
		this.infoLbl.setText("");
	}
	
	/**
	 * rstmDateBaseRefresh()用于后台数据库刷新
	 * 在进入该系统时调用
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
		InitGlobalFont(new Font("华文新魏", Font.PLAIN, 16)); // 统一设置字体
		new LogJFrame();
	}

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==this.confirm){
			try {
				String sano=this.userNumText.getText(),pwd=this.passWordText.getText();
				if(sano.equals("")||pwd.equals("")){
					this.infoLbl.setText("请正确填写用户名和密码！");
				}
				else{
					EnumMap<ReturnValueTag,Object> returnValue;
					returnValue=this.sqlexec.judgeLogInfo(sano,pwd);
					int flag=(int)returnValue.get(ReturnValueTag.Flag);
					String saType=(String)returnValue.get(ReturnValueTag.saType);
					if(flag==-1){
						this.infoLbl.setText("用户不存在！");
					}
					else if(flag==0){
						this.infoLbl.setText("密码错误！");
					}
					else{
						this.infoLbl.setText("登陆成功");
						if(saType.equals("业务员")){
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
			this.infoLbl.setText("已清除输入内容，请重新输入");
		}
		
		else if(e.getSource()==this.register){
			rDialog.show("用户注册");
		}
		
	}

}
