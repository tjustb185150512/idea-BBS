package dao.impl;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import dao.UserDao;
import entity.User;

public class UserDaoImpl extends BaseDao implements UserDao {

	private Connection        conn  = null;       // �������ݿ�����
    private PreparedStatement pstmt = null;       // ����ִ��SQL���
    private ResultSet         rs    = null;       // �û������ѯ�����

    /**
     * �����û�
     * @param user
     * @return ��������
     */
    public int addUser(User user) {
        String   sql  = "insert into TBL_USER(uname,upass,gender,head,regTime) values(?,?,"+user.getGender()+",?,?)";
        String   time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());  // ȡ������ʱ��
        String[] parm = { user.getUName(), user.getUPass(),user.getHead(),time };
        return this.executeSQL(sql, parm);        // ִ��sql��������Ӱ������
    }    
    
    /**
     * �޸��û�����
     * @param user
     * @return ��������
     */
    public int updateUser(User user){
        String   sql  = "update TBL_USER set upass=? where uname=?";
        String[] parm = { user.getUPass(),user.getUName() };
        return this.executeSQL(sql, parm);        // ִ��sql��������Ӱ������
    }
    
    /**
     * �����û��������û�
     * @param uName
     * @return �����û�����ѯ���û�����
     */
    public User findUser(String uName) {
        String sql  = "select * from TBL_USER where uName=?";
        User   user = null;
        try {
            conn  = this.getConn();                // ȡ�����ݿ�����
            pstmt = conn.prepareStatement(sql);    // ȡ��PreparedStatement����
            pstmt.setString(1, uName);             // ���ò���
            rs    = pstmt.executeQuery();          // ִ��SQLȡ�ý����
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
            e.printStackTrace();                   // �����쳣
        } finally {
            this.closeAll(conn, pstmt, rs);
        }
        return user;
    }
    
    /**
     * �����û�id�����û�
     * @param uId
     * @return ����uid��ѯ���û�����
     */
    public User findUser(int uId) {
        String sql  = "select * from TBL_USER where uId=?";
        User   user = null;
        try {
            conn  = this.getConn();                  //ȡ�����ݿ�����
            pstmt = conn.prepareStatement(sql);       //ȡ��PreparedStatement����
            pstmt.setInt(1, uId);                     //���ò���
            rs    = pstmt.executeQuery();             //ִ��SQLȡ�ý����
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
            e.printStackTrace();                     // �����쳣
        } finally {
            this.closeAll(conn, pstmt, rs);         // �ͷ���Դ
        }        
        return user;
    }

}
