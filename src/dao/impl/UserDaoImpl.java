package dao.impl;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import dao.UserDao;
import entity.User;

public class UserDaoImpl extends BaseDao implements UserDao {

	private Connection        conn  = null;       // 保存数据库连接
    private PreparedStatement pstmt = null;       // 用于执行SQL语句
    private ResultSet         rs    = null;       // 用户保存查询结果集

    /**
     * 增加用户
     * @param user
     * @return 增加条数
     */
    public int addUser(User user) {
        String   sql  = "insert into TBL_USER(uname,upass,gender,head,regTime) values(?,?,"+user.getGender()+",?,?)";
        String   time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());  // 取得日期时间
        String[] parm = { user.getUName(), user.getUPass(),user.getHead(),time };
        return this.executeSQL(sql, parm);        // 执行sql，并返回影响行数
    }    
    
    /**
     * 修改用户密码
     * @param user
     * @return 更新条数
     */
    public int updateUser(User user){
        String   sql  = "update TBL_USER set upass=? where uname=?";
        String[] parm = { user.getUPass(),user.getUName() };
        return this.executeSQL(sql, parm);        // 执行sql，并返回影响行数
    }
    
    /**
     * 根据用户名查找用户
     * @param uName
     * @return 根据用户名查询的用户对象
     */
    public User findUser(String uName) {
        String sql  = "select * from TBL_USER where uName=?";
        User   user = null;
        try {
            conn  = this.getConn();                // 取得数据库连接
            pstmt = conn.prepareStatement(sql);    // 取得PreparedStatement对象
            pstmt.setString(1, uName);             // 设置参数
            rs    = pstmt.executeQuery();          // 执行SQL取得结果集
            while( rs.next() ) {
                user = new User();
                user.setUId( rs.getInt("uId") );
                user.setUName( rs.getString("uName") );
                user.setUPass( rs.getString("uPass") );
                user.setGender(rs.getInt("gender"));
                user.setHead( rs.getString("head") );
                user.setRegTime( rs.getDate("regTime") );
            }
        } catch (Exception e) {
            e.printStackTrace();                   // 处理异常
        } finally {
            this.closeAll(conn, pstmt, rs);
        }
        return user;
    }
    
    /**
     * 根据用户id查找用户
     * @param uId
     * @return 根据uid查询的用户对象
     */
    public User findUser(int uId) {
        String sql  = "select * from TBL_USER where uId=?";
        User   user = null;
        try {
            conn  = this.getConn();                  //取得数据库连接
            pstmt = conn.prepareStatement(sql);       //取得PreparedStatement对象
            pstmt.setInt(1, uId);                     //设置参数
            rs    = pstmt.executeQuery();             //执行SQL取得结果集
            while( rs.next() ) {
                user = new User();
                user.setUId( rs.getInt("uId") );
                user.setUName( rs.getString("uName") );
                user.setUPass( rs.getString("uPass") );
                user.setGender(rs.getInt("gender"));
                user.setHead( rs.getString("head") );
                user.setRegTime( rs.getDate("regTime") );
            }
        } catch (Exception e) {
            e.printStackTrace();                     // 处理异常
        } finally {
            this.closeAll(conn, pstmt, rs);         // 释放资源
        }        
        return user;
    }

}
