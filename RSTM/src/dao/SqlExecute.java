package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.EnumMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import interfaces.LogInfoJudgeReturnValue.ReturnValueTag;
import interfaces.ReturnValue;

/**
 * ����Ϊ���ݷ��ʲ�����ݿ�����࣬������һЩ���ݿ�Ĳ�������
 * ��Щ��������������ݿ������������Ӧ����Ϣ
 * �ڱ������������ֻ����ø����еķ���ֱ�ӻ�ý������
 * �ڷ������ݿ�֮ǰ��Ӧ���ȵ��� getStatement() ������ʼ��ִ��������
 * ִ�������ݿ������������Ӧ���ݺ�Ӧ���� closeAll()�����ر�Statement��Connection
 * ע�����ʹ���� ResultSet�����ֶ��ر�
 * @author always
 *
 */
public class SqlExecute {
	
	private static final String nameOfSqlDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	//���ݿ�����������(��ǰΪ SQL server ����)
	//���޸Ĵ˴��ѻ���µ����ݿ�����
	private static final String DatabaseName = "rstm"; //�������ݿ���
	private static final String URL = "jdbc:sqlserver://localhost:1433;DatabaseName="+DatabaseName;
																//���ݿ�����(Ĭ���������ݿ���Ϊ rstm)
	private static final String User = "user1";		//�û���
	private static final String PassWord = "123";	//����
	
	private Connection conn;  	//���ݿ����Ӷ���
	private Statement stmt;   	//���ִ�ж���
	private ResultSet rset;		//�����
	private CallableStatement calState;//����ִ�д洢����

	/**
	 * ���������ݿ����Ӳ����һ��Statement���������øö���ֱ�ӽ������ݿ����
	 * ���Ƿ������ݿ⣬Ӧ������ͨ���÷������һ�� Statement��ֵ����Ա����stmt��
	 * @throws Exception
	 */
	private void getStatement() throws Exception {
		try {
			//������������
			Class.forName(nameOfSqlDriver);  
			//������ݿ�����
			conn = DriverManager.getConnection(URL,User,PassWord);
			stmt = conn.createStatement();
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	/**
	 * �ر�ʹ��������ݿ����ӣ���������ݿ������Ӧ�õ��ô˷����ر�����
	 * Ϊ��ʹ�������������ܹ����ø÷����������޸�Ϊ public
	 * @throws SQLException
	 */
	public void closeAll() throws SQLException {
		try {
			//��������Ƿ��ѹرգ����û�йرգ���ر�
			if( stmt != null) {
				stmt.close();
				stmt = null;
			}
			if( !conn.isClosed()) 
				conn.close();
		}
		catch(SQLException se) {
			throw se;
		}
	}
	
	/**
	 * ִ��sql ��䣬���Ƿǲ�ѯ������ݿ������Ӧ�õ��ø÷���
	 * @param sql ����: Ҫִ�е�sql ���
	 * @return ����: ��Ӱ��ļ�¼�������Ը��ݸ÷��ؽ���ж�ִ�����
	 * 		   �����0������û���κ����ݿ��¼�����仯
	 * @throws Exception ����������쳣�����쳣�����׳�������ڳ�����ô�Ӧ�ô����쳣
	 */
	public int executeSql(String sql) throws Exception {
		//��¼��Ӱ��ļ�¼��
		int recoders = 0;
		//���� Statement���͵ĳ�Ա���� stmt
		getStatement();
		//ִ�����ݿ����
		recoders = stmt.executeUpdate(sql);
		//�ر����д򿪵����ݿ����
		closeAll();
		
		return recoders;
	}
	
	/**
	 * ���ؼ�¼�� ResultSet
	 * �ܶ�ʱ����Ҫ���Ǹ�������������ʹ�ü�¼���е������ɵ��÷������д���
	 * @param sql
	 * @return ResultSet
	 * @throws Exception
	 */
	public ResultSet getResultSet(String sql) throws Exception{
		//���� Statement���͵ĳ�Ա���� stmt
		getStatement();
		//ִ�����ݿ��ѯ����ý����
		ResultSet rs =  stmt.executeQuery(sql);
		//ע: �˴����ܹرգ�������Զ��ر� ResultSet
		//	ͬʱ�ǵ��ڵ��÷����ڹر�
		return rs;
	}
	
	/**
	 * ��ȡsql ��ѯ����Ӧ�Ľ������Ԫ��������������
	 * @param sql(��ѯ���)
	 * @return TupleNum(Ԫ����)
	 * @throws Exception
	 */
	public int getTupleNum(String sql) throws Exception{
		//Ԫ����
		int TupleNum=0;
		//��ý����
		rset = getResultSet(sql);
		//�α�ѭ������
		while(rset.next())TupleNum++;
		//�ֶ��ر� ResultSet
        rset.close();
		//�ر����������򿪵����ݿ����
		closeAll();
		return TupleNum;
	}
	
	/**
	 * ���� getResultSet�������һ�� ResultSet�����ô����ģ�壬Ȼ�󷵻�TableModel����
	 * ������Ҫ�������ݿ���Ϣ���������䵽һ�� JTable �У�����ֱ�ӵ��ø÷���
	 * @param sql: ���������ݿ��ѯ�ַ���
	 * @return ����: һ�����������ݵ� JTableModel
	 * @throws Exception
	 */
	public DefaultTableModel getTableModel(String sql) throws Exception{
		//��ý����
		rset = getResultSet(sql);
		
		ResultSetMetaData rsmd=rset.getMetaData();         //����Ԫ���ݶ���
        
		//�������
		int columns = rsmd.getColumnCount();   
		
        String columntitle[] = new String[columns];        //������������
        for (int j=1; j<=columns; j++)
            columntitle[j-1] = rsmd.getColumnLabel(j);     //�������������������
        //���ģ��
        //ʹ�ñ��ģ�������������������ĺô��ǿ��԰������ֱ���ӱ������
        //���еķ����漰���α����ͣ�Ĭ�ϵ�Statement��õ� ResultSet ��ֻ����ǰ�ģ����ܻع�
        //����������û�õı�������columntitle ����һ�����ģ�壬Ϊ��ָ�����У��ñ���ж�Ӧ����
        DefaultTableModel tm = new DefaultTableModel(columntitle, 0); 

        //���ݽ�����������������飬�������ݽ�����е�һ����¼
        String results[]= new String[columns]; 
        
        //���������������ÿ�ν�һ�м�¼��ӵ� ģ����
        while (rset.next()) {                               
            for(int i=0; i<columns; i++)
            	//����ǰ�е�ֵ�浽���飬���ﶼ�����ַ�����ʽ��ע�� rset �ļ����Ǵ� 1 ��ʼ
            	results[i] = rset.getString(i+1);
            //��һ�м�¼��ӵ���ģ���У�ע���Ǽӵ���ģ����
            tm.addRow(results);
        }
        //�ֶ��ر� ResultSet
        rset.close();
		//�ر����������򿪵����ݿ����
		closeAll();
		//���ر��ģ��
		return tm;
	}
	
	/**
	 * ���� getTableModel�������һ��ģ�壬���ô���� JTable��Ȼ�󷵻�JTable����
	 * ������Ҫ�������ݿ���Ϣ���������ʾ��һ�� JTable �У�����ֱ�ӵ��ø÷���
	 * @param sql ����: Ҫִ�е�  sql���
	 * @return ����: һ�����������ݵ� JTable
	 * @throws Exception
	 */
	public JTable getTable(String sql) throws Exception{
		//��� sql����ѯ�������Ӧ��ģ��
		DefaultTableModel tm = getTableModel(sql);
        JTable tb = new JTable();
        //��ģ���������ѡ�����е����ݾ���ģ���е�
        tb.setModel(tm);
        //�˴�����رգ��ڷ���getTableModel�Ѿ���ɹر�
		return tb;
	}
	
	/**
	 * judgeLogInfo()
	 * �����ж�������˺���Ϣ�Ƿ���ȷ�������ص�ǰ�û�����
	 * (��Ե�ǰ���ݿ�����в���)
	 * @param sano �����˺�
	 * @param pwd  ��������
	 * @return EnumMap<ReturnValueTag, Object> 
	 * 			�Զ���෵��ֵEnumMap(�������interfaces.LogInfoJudgeReturnValue.ReturnValueTag)
	 * @throws Exception
	 */
	public EnumMap<ReturnValueTag, Object> judgeLogInfo(String sano,String pwd) throws Exception{
		getStatement();
	    this.calState=this.conn.prepareCall("{?=call loginfojuge(?,?,?)}");
	    //����ֵ(��¼���)
	    calState.registerOutParameter(1, Types.INTEGER);
	    //�������(�û����,����)
	    calState.setString(2, sano);
	    calState.setString(3, pwd);
	    //�������(�û�����)
	    calState.registerOutParameter(4, Types.VARCHAR);
	    
	    calState.execute();
	    //��÷���ֵ
	    int flag=calState.getInt(1);
	    //����������
	    String saType=calState.getString(4);
	    
	    this.calState.close();
	    closeAll();
	    
	    return new ReturnValue().getReturnValue(flag, saType);
	}
	
	/**
	 * ticnumbering()
	 * ���ú�̨���ݿ����ɳ�Ʊ��ŵĴ洢����
	 * (��Ե�ǰ���ݿ�����в���)
	 * @param tno ���κ�
	 * @return integer ��Ʊ���
	 * @throws Exception
	 */
	public int ticnumbering(String tno)throws Exception{
		getStatement();
	    this.calState=this.conn.prepareCall("{?=call ticnumbering(?)}");
	    //����ֵ(��Ʊ���)
	    calState.registerOutParameter(1, Types.INTEGER);
	    //�������(���κ�)
	    calState.setString(2, tno);
	    calState.execute();
		int res=calState.getInt(1);
		this.calState.close();
	    closeAll();
		return res;
	}
	
	/**
	 * perpriceget()���ڻ�ȡָ�����εĵ�λ��̼۸�
	 * @param tno ���κ�
	 * @return FLoat[],size=3
	 * @throws Exception
	 */
	public Float[] perpriceget(String tno)throws Exception{
		getStatement();
	    this.calState=this.conn.prepareCall("{call perpriceget(?,?,?,?)}");
	    //�������(���κ�)
	    calState.setString(1, tno);
	    //�������(�û�����)
	    calState.registerOutParameter(2, Types.FLOAT);
	    calState.registerOutParameter(3, Types.FLOAT);
	    calState.registerOutParameter(4, Types.FLOAT);
	    calState.execute();
		Float res[]=new Float[3];
		for(int i=0;i<res.length;i++){
			res[i]=calState.getFloat(i+2);
		}
		this.calState.close();
	    closeAll();
		return res;
	}
	
	/**
	 * stationget()���ڴӺ�̨���ݿ��ȡ��վ��Ϣ
	 * @return string[]
	 * @throws Exception
	 */
	public String[] stationget() throws Exception{
		getStatement();
		String sql="exec stnameget";
		ArrayList<String> allLines = new ArrayList<String>();//ArrayList������У�������չ����
		rset = getResultSet(sql);
		//���������
		while (rset.next()) {                               
            allLines.add(rset.getString(1));
        }
		//ArrayList<String>תString����
		String str[]=new String[allLines.size()];
		allLines.toArray(str);
		return str;
	}
	
}
