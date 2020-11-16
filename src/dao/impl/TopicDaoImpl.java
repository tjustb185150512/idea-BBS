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

import dao.TopicDao;
import entity.Topic;

public class TopicDaoImpl extends BaseDao implements TopicDao {

	private Connection        conn  = null;        // �������ݿ�����
    private PreparedStatement pstmt = null;        // ����ִ��SQL���
    private ResultSet         rs    = null ;       // �û������ѯ�����

    /**
     * �������
     * @param topic
     * @return ��������
     */
    public int addTopic(Topic topic) {
        String   sql  = "insert into TBL_TOPIC(title,content,publishTime,modifyTime,uId,boardId) values(?,?,?,?," + topic.getUid() + "," + topic.getBoardId() + ")";
        String   time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());  // ȡ������ʱ��
        String[] parm = { topic.getTitle(), topic.getContent(), time, time };        
        return this.executeSQL(sql, parm);        // ִ��sql��������Ӱ������
    }

    /**
     * ɾ������
     * @param topicId
     * @return ɾ������
     */
    public int deleteTopic(int topicId) {
        String sql = "delete from TBL_TOPIC where topicId=" + topicId;
        return this.executeSQL(sql, null);        // ִ��sql��������Ӱ������
    }

    /**
     * ��������
     * @param topic
     * @return ��������
     */
    public int updateTopic(Topic topic) {
        String   sql  = "update TBL_TOPIC set title=?, content=?, modifyTime=? where topicId="+topic.getTopicId();
        String   time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());  // ȡ������ʱ��
        String[] parm = { topic.getTitle(), topic.getContent(), time };
        return this.executeSQL(sql, parm);        // ִ��sql��������Ӱ������
    }

    /**
     * ����һ���������ϸ��Ϣ
     * @param topicId
     * @return ������Ϣ
     */
    public Topic findTopic(int topicId) {
        String sql  = "select * from TBL_TOPIC where topicId=?";
        Topic topic = null;
        try {
            conn  = this.getConn();                // ������ݿ�����
            pstmt = conn.prepareStatement(sql);    // �õ�һ��PreparedStatement����
            pstmt.setInt(1, topicId);              // ����topicIdΪ����ֵ
            rs    = pstmt.executeQuery();          // ִ��sql��ȡ�ò�ѯ�����

            /*  ��������е���Ϣȡ�����浽topic�����У�ѭ�����ֻ��ִ��һ��  */
            while ( rs.next() ) {
                topic = new Topic();              // �������
                topic.setTopicId(rs.getInt("topicId"));
                topic.setTitle(rs.getString("title"));
                topic.setContent(rs.getString("content"));
                topic.setPublishTime(rs.getDate("publishTime"));
                topic.setModifyTime(rs.getDate("modifyTime"));
                topic.setUid(rs.getInt("uId"));
            }
        } catch (Exception e) {
            e.printStackTrace();                   // �����쳣
        } finally {
            this.closeAll(conn, pstmt, rs);       // �ͷ���Դ
        }
        return topic; 
    }

    /**
     * ��������List
     * @param page
     * @return ����List
     */
    public List findListTopic(int page, int boardId) {
        List list    = new ArrayList();            // ����������������б�
        int rowBegin = 0;                          // ��ʼ��������ʾÿҳ��һ����¼�����ݿ��е�����
        if( page > 1 ) {
            rowBegin = 20 * (page-1);              // ��ҳ��ȡ�ÿ�ʼ��������ÿҳ������ʾ10���ظ�
        }
        String sql = "select top 20 * from TBL_TOPIC where boardId=" + boardId + " and topicId not in(select top "+ rowBegin + " topicId from TBL_TOPIC where boardId=" + boardId + " order by publishTime desc )order by publishTime desc";
        try {
            conn  = this.getConn();                // ������ݿ�����
            pstmt = conn.prepareStatement(sql);    // �õ�һ��PreparedStatement����
            rs    = pstmt.executeQuery();          // ִ��SQL���õ������

            /*  ��������е���Ϣȡ�����浽list��  */
            while ( rs.next() ) {
                Topic topic = new Topic();        // �������
                topic.setTopicId(rs.getInt("topicId"));
                topic.setTitle(rs.getString("title"));
                topic.setPublishTime(rs.getDate("publishTime"));
                topic.setUid(rs.getInt("uid"));
                list.add(topic);
            }
        } catch ( Exception e ) {
            e.printStackTrace();                   // �����쳣
        } finally {
            this.closeAll(conn, pstmt, rs);       // �ͷ���Դ
        }
        return list;
    }
    
    /**
     * ���ݰ��idȡ�øð���������
     * @param boardId
     * @return ������
     */
    public int findCountTopic(int boardId) {
        int    count = 0;                          // ������        
        String  sql   = "select count(*) from TBL_TOPIC where boardId=" + boardId;
        try {
            conn  = this.getConn();
            pstmt = conn.prepareStatement(sql);
            rs    = pstmt.executeQuery();
            while( rs.next() ) {
                count = rs.getInt(1);               // ȡ��������
            }
        } catch ( Exception e) {
            e.printStackTrace();                    // �����쳣
        } finally {
            this.closeAll(conn, pstmt, rs);         // �ͷ���Դ
        }
        return count;
    }

}
