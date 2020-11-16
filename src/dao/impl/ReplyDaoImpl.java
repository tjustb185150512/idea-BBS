/*
 * s2jsp.lg.dao.impl.ReplyDaoImpl.java
 * 2007-7-19
 * ReplyDao��ʵ����
 */
package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.ReplyDao;
import entity.Reply;

public class ReplyDaoImpl extends BaseDao implements ReplyDao {
	private Connection        conn  = null;              // �������ݿ�����
    private PreparedStatement pstmt = null;              // ����ִ��SQL���
    private ResultSet         rs    = null ;             // �û������ѯ�����

    /**
     * ��ӻظ�
     * @param reply
     * @return ��������
     */
    public int addReply(Reply reply) {
        String   sql  = "insert into TBL_REPLY(title,content,publishTime,modifyTime,uId,topicId) values(?,?,?,?," + reply.getUid() + "," + reply.getTopicId() + ")";
        String   time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());  // ȡ������ʱ��
        String[] parm = { reply.getTitle(), reply.getContent(), time, time };        
        return this.executeSQL(sql, parm);               // ִ��sql��������Ӱ������
    }

    /**
     * ɾ���ظ�
     * @param replyId 
     * @return ɾ������
     */
    public int deleteReply(int replyId) {
        String sql = "delete from TBL_REPLY where replyId=" + replyId;
        return this.executeSQL(sql, null);               // ִ��sql��������Ӱ������
    }

    /**
     * ���»ظ�
     * @param reply
     * @return ��������
     */
    public int updateReply(Reply reply) {
        String   sql  = "update TBL_REPLY set title=?, content=?, modifyTime=? where replyId="+reply.getReplyId();
        String   time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());  // ȡ������ʱ��
        String[] parm = { reply.getTitle(), reply.getContent(), time };
        return this.executeSQL(sql, parm);               // ִ��sql��������Ӱ������
    }


    /**
     * ���һظ�List
     * @param page
     * @return ��ѯ���
     */
    public List findListReply(int page, int topicId) {
        List list  = new ArrayList();                  // ��������ظ������б�
        conn  = null;                                  // ���ڱ������ݿ�����
        pstmt = null;                                  // ����ִ��SQL���
        rs    = null;                                  // �û������ѯ�����
        int rowBegin = 0;                               // ��ʼ��������ʾÿҳ��һ����¼�����ݿ��е�����
        if( page > 1 ) {
            rowBegin = 10 * (page-1);                   // ��ҳ��ȡ�ÿ�ʼ��������ÿҳ������ʾ10���ظ�
        }
        try {
            conn = this.getConn();                     // �õ����ݿ�����
            String sql = "select top 10 * from TBL_REPLY where topicId=" + topicId + " and replyId not in(select top "+ rowBegin + " replyId from TBL_REPLY where topicId=" + topicId + "order by publishTime )order by publishTime";
            pstmt = conn.prepareStatement(sql);         // �õ�PreparedStatement����
            rs = pstmt.executeQuery();                  // ִ��sqlȡ�ý����

            /*  ѭ�����ظ���Ϣ��װ��List  */
            while ( rs.next() ) {
                Reply reply = new Reply();              // �ظ�����
                reply.setReplyId(rs.getInt("replyId"));
                reply.setTitle(rs.getString("title"));
                reply.setContent(rs.getString("content"));
                reply.setPublishTime(rs.getDate("publishTime"));
                reply.setModifyTime(rs.getDate("modifyTime"));
                reply.setTopicId(rs.getInt("topicId"));
                reply.setUid(rs.getInt("uId"));
                list.add(reply);
            }
        } catch (Exception e) {
            e.printStackTrace();                        // �����쳣
        } finally {
            this.closeAll(conn, pstmt, rs);             // �ͷ���Դ
        }
        return list;
    }
    
    /**
     * ��������id��ѯ��������Ļظ�����
     * @param topicId ����id
     * @return �ظ�����
     */
    public int findCountReply(int topicId){
        int              count = 0;                    // �ظ�����
        Connection        conn  = null;                // ���ڱ������ݿ�����
        PreparedStatement pstmt = null;                // ����ִ��SQL���
        ResultSet         rs    = null;                // �û������ѯ�����
        String            sql   = "select count(*) from TBL_REPLY where topicId=" + topicId;
        try {
            conn  = this.getConn();
            pstmt = conn.prepareStatement(sql);
            rs    = pstmt.executeQuery();
            while( rs.next() ) {
                count = rs.getInt(1);
            }
        } catch ( Exception e) {
            e.printStackTrace();                        // �����쳣
        } finally {
            this.closeAll(conn, pstmt, rs);             // �ͷ���Դ
        }
        return count;
    }

    /**
     * ��������id�����һظ�����Ϣ
     * @param replyId
     * @return �ظ�
     */
    public Reply findReply(int replyId) {
        String sql  = "select * from TBL_REPLY where replyId=?";
        Reply reply = null;
        try {
            conn  = this.getConn();                // ������ݿ�����
            pstmt = conn.prepareStatement(sql);    // �õ�һ��PreparedStatement����
            pstmt.setInt(1, replyId);              // ����topicIdΪ����ֵ
            rs    = pstmt.executeQuery();          // ִ��sql��ȡ�ò�ѯ�����

            /*  ��������е���Ϣȡ�����浽reply�����У�ѭ�����ֻ��ִ��һ��  */
            while ( rs.next() ) {
                reply = new Reply();              // �ظ�����
                reply.setReplyId(rs.getInt("replyId"));
                reply.setTitle(rs.getString("title"));
                reply.setContent(rs.getString("content"));
                reply.setPublishTime(rs.getDate("publishTime"));
                reply.setModifyTime(rs.getDate("modifyTime"));
                reply.setUid(rs.getInt("uId"));
            }
        } catch (Exception e) {
            e.printStackTrace();                   // �����쳣
        } finally {
            this.closeAll(conn, pstmt, rs);       // �ͷ���Դ
        }
        return reply;
    }

}
