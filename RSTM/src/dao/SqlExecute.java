package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.EnumMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import interfaces.LogInfoJudgeReturnValue.ReturnValueTag;
import interfaces.ReturnValue;

/**
 * 本类为数据访问层的数据库操作类，集成了一些数据库的操作方法
 * 这些方法负责访问数据库操作并返回相应的信息
 * 在本层的其它类中只需调用该类中的方法直接获得结果即可
 * 在访问数据库之前，应该先调用 getStatement() 方法初始化执行语句对象
 * 执行完数据库操作、处理相应数据后，应调用 closeAll()方法关闭Statement和Connection
 * 注意如果使用了 ResultSet，需手动关闭
 * @author always
 *
 */
public class SqlExecute {
	
	private static final String nameOfSqlDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	//数据库驱动程序名(当前为 SQL server 驱动)
	//可修改此处已获得新的数据库链接
	private static final String DatabaseName = "rstm"; //连接数据库名
	private static final String URL = "jdbc:sqlserver://localhost:1433;DatabaseName="+DatabaseName;
																//数据库链接(默认链接数据库设为 rstm)
	private static final String User = "user1";		//用户名
	private static final String PassWord = "123";	//密码
	
	private Connection conn;  	//数据库连接对象
	private Statement stmt;   	//语句执行对象
	private ResultSet rset;		//结果集
	private CallableStatement calState;//用于执行存储过程

	/**
	 * 创建了数据库连接并获得一个Statement，可以利用该对象直接进行数据库操作
	 * 凡是访问数据库，应该首先通过该方法获得一个 Statement赋值给成员变量stmt。
	 * @throws Exception
	 */
	private void getStatement() throws Exception {
		try {
			//加载驱动程序
			Class.forName(nameOfSqlDriver);  
			//获得数据库连接
			conn = DriverManager.getConnection(URL,User,PassWord);
			stmt = conn.createStatement();
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	/**
	 * 关闭使用完的数据库连接，当完成数据库操作后应该调用此方法关闭连接
	 * 为了使本层中其它类能够调用该方法，将其修改为 public
	 * @throws SQLException
	 */
	public void closeAll() throws SQLException {
		try {
			//检查连接是否已关闭，如果没有关闭，则关闭
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
	 * 执行sql 语句，凡是非查询类的数据库操作，应该调用该方法
	 * @param sql 参数: 要执行的sql 语句
	 * @return 返回: 受影响的记录数，可以根据该返回结果判断执行情况
	 * 		   如果是0，表明没有任何数据库记录发生变化
	 * @throws Exception 如果出现了异常，将异常继续抛出，因此在程序调用处应该处理异常
	 */
	public int executeSql(String sql) throws Exception {
		//记录受影响的记录数
		int recoders = 0;
		//设置 Statement类型的成员变量 stmt
		getStatement();
		//执行数据库操作
		recoders = stmt.executeUpdate(sql);
		//关闭所有打开的数据库对象
		closeAll();
		
		return recoders;
	}
	
	/**
	 * 返回记录集 ResultSet
	 * 很多时候需要我们根据情况决定如何使用记录集中的数据由调用方法自行处理
	 * @param sql
	 * @return ResultSet
	 * @throws Exception
	 */
	public ResultSet getResultSet(String sql) throws Exception{
		//设置 Statement类型的成员变量 stmt
		getStatement();
		//执行数据库查询，获得结果集
		ResultSet rs =  stmt.executeQuery(sql);
		//注: 此处不能关闭，否则会自动关闭 ResultSet
		//	同时记得在调用方法内关闭
		return rs;
	}
	
	/**
	 * 获取sql 查询语句对应的结果集的元组数量，并返回
	 * @param sql(查询语句)
	 * @return TupleNum(元组数)
	 * @throws Exception
	 */
	public int getTupleNum(String sql) throws Exception{
		//元组数
		int TupleNum=0;
		//获得结果集
		rset = getResultSet(sql);
		//游标循环计数
		while(rset.next())TupleNum++;
		//手动关闭 ResultSet
        rset.close();
		//关闭所有其它打开的数据库对象
		closeAll();
		return TupleNum;
	}
	
	/**
	 * 调用 getResultSet方法获得一个 ResultSet，并用此填充模板，然后返回TableModel对象
	 * 凡是需要访问数据库信息并将结果填充到一个 JTable 中，可以直接调用该方法
	 * @param sql: 参数，数据库查询字符串
	 * @return 返回: 一个已填充好数据的 JTableModel
	 * @throws Exception
	 */
	public DefaultTableModel getTableModel(String sql) throws Exception{
		//获得结果集
		rset = getResultSet(sql);
		
		ResultSetMetaData rsmd=rset.getMetaData();         //返回元数据对象
        
		//获得列数
		int columns = rsmd.getColumnCount();   
		
        String columntitle[] = new String[columns];        //创建列名数组
        for (int j=1; j<=columns; j++)
            columntitle[j-1] = rsmd.getColumnLabel(j);     //获得列名填充表格标题数组
        //表格模板
        //使用表格模板对象来创建表格，这样的好处是可以按行来分别添加表格数据
        //书中的方法涉及到游标类型，默认的Statement获得的 ResultSet 是只读向前的，不能回滚
        //下面语句先用获得的标题数组columntitle 定义一个表的模板，为其指定了列，该表具有对应的列
        DefaultTableModel tm = new DefaultTableModel(columntitle, 0); 

        //根据结果集的列数创建数组，保存数据结果集中的一条记录
        String results[]= new String[columns]; 
        
        //迭代遍历结果集，每次将一行记录添加到 模板中
        while (rset.next()) {                               
            for(int i=0; i<columns; i++)
            	//将当前行的值存到数组，这里都用了字符串格式，注意 rset 的计数是从 1 开始
            	results[i] = rset.getString(i+1);
            //将一行记录添加到表模板中，注意是加到了模板里
            tm.addRow(results);
        }
        //手动关闭 ResultSet
        rset.close();
		//关闭所有其它打开的数据库对象
		closeAll();
		//返回表格模板
		return tm;
	}
	
	/**
	 * 调用 getTableModel方法获得一个模板，并用此填充 JTable，然后返回JTable对象
	 * 凡是需要访问数据库信息并将结果显示在一个 JTable 中，可以直接调用该方法
	 * @param sql 参数: 要执行的  sql语句
	 * @return 返回: 一个已填充好数据的 JTable
	 * @throws Exception
	 */
	public JTable getTable(String sql) throws Exception{
		//获得 sql语句查询结果集对应的模板
		DefaultTableModel tm = getTableModel(sql);
        JTable tb = new JTable();
        //用模板填充表格，则选择表格中的数据就是模板中的
        tb.setModel(tm);
        //此处无需关闭，在方法getTableModel已经完成关闭
		return tb;
	}
	
	/**
	 * judgeLogInfo()
	 * 用于判断输入的账号信息是否正确，并返回当前用户类型
	 * (针对当前数据库的特有操作)
	 * @param sano 输入账号
	 * @param pwd  输入密码
	 * @return EnumMap<ReturnValueTag, Object> 
	 * 			自定义多返回值EnumMap(定义详见interfaces.LogInfoJudgeReturnValue.ReturnValueTag)
	 * @throws Exception
	 */
	public EnumMap<ReturnValueTag, Object> judgeLogInfo(String sano,String pwd) throws Exception{
		getStatement();
	    this.calState=this.conn.prepareCall("{?=call loginfojuge(?,?,?)}");
	    //返回值(登录标记)
	    calState.registerOutParameter(1, Types.INTEGER);
	    //输入参数(用户编号,密码)
	    calState.setString(2, sano);
	    calState.setString(3, pwd);
	    //输出参数(用户类型)
	    calState.registerOutParameter(4, Types.VARCHAR);
	    
	    calState.execute();
	    //获得返回值
	    int flag=calState.getInt(1);
	    //获得输出参数
	    String saType=calState.getString(4);
	    
	    this.calState.close();
	    closeAll();
	    
	    return new ReturnValue().getReturnValue(flag, saType);
	}
	
	/**
	 * ticnumbering()
	 * 调用后台数据库生成车票编号的存储过程
	 * (针对当前数据库的特有操作)
	 * @param tno 车次号
	 * @return integer 车票编号
	 * @throws Exception
	 */
	public int ticnumbering(String tno)throws Exception{
		getStatement();
	    this.calState=this.conn.prepareCall("{?=call ticnumbering(?)}");
	    //返回值(车票编号)
	    calState.registerOutParameter(1, Types.INTEGER);
	    //输入参数(车次号)
	    calState.setString(2, tno);
	    calState.execute();
		int res=calState.getInt(1);
		this.calState.close();
	    closeAll();
		return res;
	}
	
	/**
	 * perpriceget()用于获取指定车次的单位里程价格
	 * @param tno 车次号
	 * @return FLoat[],size=3
	 * @throws Exception
	 */
	public Float[] perpriceget(String tno)throws Exception{
		getStatement();
	    this.calState=this.conn.prepareCall("{call perpriceget(?,?,?,?)}");
	    //输入参数(车次号)
	    calState.setString(1, tno);
	    //输出参数(用户类型)
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
	 * stationget()用于从后台数据库获取车站信息
	 * @return string[]
	 * @throws Exception
	 */
	public String[] stationget() throws Exception{
		getStatement();
		String sql="exec stnameget";
		ArrayList<String> allLines = new ArrayList<String>();//ArrayList数组队列，便于扩展长度
		rset = getResultSet(sql);
		//迭代结果集
		while (rset.next()) {                               
            allLines.add(rset.getString(1));
        }
		//ArrayList<String>转String数组
		String str[]=new String[allLines.size()];
		allLines.toArray(str);
		return str;
	}
	
}
