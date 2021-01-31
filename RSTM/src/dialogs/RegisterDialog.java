package dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import dao.SqlExecute;

/**
 * RegisterDialog��Ϊ��վƱ���ϵͳ�û�ע��Ի���
 * @author always
 *
 */
public class RegisterDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2893667278896741072L;
	private JLabel userLbl,pwdLbl,typeLbl,ageLbl,sexLbl,nameLbl,infolabel;
	private JTextField userText,nameText;
	private JPasswordField passWordText;
	private JComboBox<String> typeCmBox,sexCmBox;
	private JComboBox<Integer> ageCmBox;
	private JButton confirm;
	
	private SqlExecute sqlexec;
	
	public RegisterDialog(JFrame f, String title, boolean model){
		super(f,title,model);
		init();
		sqlexec=new SqlExecute();
	}
	
	private void init(){
		this.userLbl=new JLabel("�û�����");
		this.pwdLbl=new JLabel("��    �룺");
		this.nameLbl=new JLabel("��    ����");
		this.typeLbl=new JLabel("�û����");
		this.sexLbl=new JLabel("��    ��");
		this.ageLbl=new JLabel("��    �䣺");
		
		this.userText=new JTextField(10);
		this.nameText=new JTextField(10);
		this.passWordText=new JPasswordField(10);
		this.passWordText.setEchoChar('*');
		
		String sex[]={"��","Ů"};
		String type[]={"����Ա","ҵ��Ա"};
		this.typeCmBox=new JComboBox<String>(type);
		this.sexCmBox=new JComboBox<String>(sex);
		this.ageCmBox=new JComboBox<Integer>();
		for(int i=20;i<=60;i++){
			this.ageCmBox.addItem(i);
		}
		
		this.confirm=new JButton("ȷ��");
		this.confirm.addActionListener(this);
		
		JPanel panel1=new JPanel();
		panel1.setLayout(new FlowLayout());
		panel1.add(userLbl);
		panel1.add(userText);
		
		JPanel panel2=new JPanel();
		panel2.setLayout(new FlowLayout());
		panel2.add(pwdLbl);
		panel2.add(passWordText);
		
		JPanel panel3=new JPanel();
		panel3.setLayout(new FlowLayout());
		panel3.add(nameLbl);
		panel3.add(nameText);
		
		JPanel panel4=new JPanel();
		panel4.setLayout(new FlowLayout());
		panel4.add(sexLbl);
		panel4.add(sexCmBox);
		
		JPanel panel5=new JPanel();
		panel5.setLayout(new FlowLayout());
		panel5.add(ageLbl);
		panel5.add(ageCmBox);
		
		JPanel panel6=new JPanel();
		panel6.setLayout(new FlowLayout());
		panel6.add(typeLbl);
		panel6.add(typeCmBox);
		
		JPanel panel7=new JPanel();
		panel7.add(confirm);
		
		infolabel=new JLabel("",JLabel.CENTER);
		infolabel.setForeground(Color.RED);
		
		this.setLayout(new GridLayout(8,1));
		this.add(panel1);this.add(panel2);
		this.add(panel3);this.add(panel4);
		this.add(panel5);this.add(panel6);
		this.add(panel7);this.add(infolabel);
		
	}
	
	/**
	 * show()Ϊ������ʾ����
	 * @param title �������
	 */
	public void show(String title){
		this.setTitle(title);//���ڱ�������
		this.setSize(250,320);//Ĭ�ϴ�С����
	    this.setLocationRelativeTo(null);//����
	    this.setVisible(true);//����
	    this.setResizable(false);
	}

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.confirm){
			boolean flag=true;
			//JPasswordField.getText()����δ��д
			if(!this.userText.getText().equals("")&&!this.passWordText.getText().equals("")&&
					!this.nameText.getText().equals("")&&this.sexCmBox.getSelectedIndex()!=-1&&
					this.ageCmBox.getSelectedIndex()!=-1&&this.typeCmBox.getSelectedIndex()!=-1){
				String sql="exec staffinsert "+
					this.userText.getText()+","+
					this.passWordText.getText()+","+
					this.nameText.getText()+","+
					(String)this.sexCmBox.getSelectedItem()+","+
					(this.ageCmBox.getSelectedIndex()+20)+","+
					(String)this.typeCmBox.getSelectedItem();
				try {
					this.sqlexec.executeSql(sql);
				} catch (Exception e1) {
					flag=false;
					this.infolabel.setText(e1.getMessage());
				}
				if(flag)
					this.infolabel.setText("ע��ɹ���");
			}
			else
				this.infolabel.setText("��������д��Ϣ��");
		}
	}
	
}
