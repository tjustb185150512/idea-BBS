/*
 * s2jsp.lg.dao.impl.ReplyDaoImpl.java
 * 2007-7-19
 * ReplyDao的实现类
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
	private Connection        conn  = null;              // 保存数据库连接
    private PreparedStatement pstmt = null;              // 用于执行SQL语句
    private ResultSet         rs    = null ;             // 用户保存查询结果集

    /**
     * 添加回复
     * @param reply
     * @return 增加条数
     */
    public int addReply(Reply reply) {
        String   sql  = "insert into TBL_REPLY(title,content,publishTime,modifyTime,uId,topicId) values(?,?,?,?," + reply.getUid() + "," + reply.getTopicId() + ")";
        String   time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());  // 取得日期时间
        String[] parm = { reply.getTitle(), reply.getContent(), time, time };        
        return this.executeSQL(sql, parm);               // 执行sql，并返回影响行数
    }

    /**
     * 删除回复
     * @param replyId 
     * @return 删除条数
     */
    public int deleteReply(int replyId) {
        String sql = "delete from TBL_REPLY where replyId=" + replyId;
        return this.executeSQL(sql, null);               // 执行sql，并返回影响行数
    }

    /**
     * 更新回复
     * @param reply
     * @return 更新条数
     */
    public int updateReply(Reply reply) {
        String   sql  = "update TBL_REPLY set title=?, content=?, modifyTime=? where replyId="+reply.getReplyId();
        String   time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());  // 取得日期时间
        String[] parm = { reply.getTitle(), reply.getContent(), time };
        return this.executeSQL(sql, parm);               // 执行sql，并返回影响行数
    }


    /**
     * 查找回复List
     * @param page
     * @return 查询结果
     */
    public List findListReply(int page, int topicId) {
        List list  = new ArrayList();                  // 用来保存回复对象列表
        conn  = null;                                  // 用于保存数据库连接
        pstmt = null;                                  // 用于执行SQL语句
        rs    = null;                                  // 用户保存查询结果集
        int rowBegin = 0;                               // 开始行数，表示每页第一条记录在数据库中的行数
        if( page > 1 ) {
            rowBegin = 10 * (page-1);                   // 按页数取得开始行数，设每页可以显示10条回复
        }
        try {
            conn = this.getConn();                     // 得到数据库连接
            String sql = "select top 10 * from TBL_REPLY where topicId=" + topicId + " and replyId not in(select top "+ rowBegin + " replyId from TBL_REPLY where topicId=" + topicId + "order by publishTime )order by publishTime";
            pstmt = conn.prepareStatement(sql);         // 得到PreparedStatement对象
            rs = pstmt.executeQuery();                  // 执行sql取得结果集

            /*  循环将回复信息封装成List  */
            while ( rs.next() ) {
                Reply reply = new Reply();              // 回复对象
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
            e.printStackTrace();                        // 处理异常
        } finally {
            this.closeAll(conn, pstmt, rs);             // 释放资源
        }
        return list;
    }
    
    /**
     * 根据主题id查询出该主题的回复条数
     * @param topicId 主题id
     * @return 回复条数
     */
    public int findCountReply(int topicId){
        int              count = 0;                    // 回复条数
        Connection        conn  = null;                // 用于保存数据库连接
        PreparedStatement pstmt = null;                // 用于执行SQL语句
        ResultSet         rs    = null;                // 用户保存查询结果集
        String            sql   = "select count(*) from TBL_REPLY where topicId=" + topicId;
        try {
            conn  = this.getConn();
            pstmt = conn.prepareStatement(sql);
            rs    = pstmt.executeQuery();
            while( rs.next() ) {
                count = rs.getInt(1);
            }
        } catch ( Exception e) {
            e.printStackTrace();                        // 处理异常
        } finally {
            this.closeAll(conn, pstmt, rs);             // 释放资源
        }
        return count;
    }

    /**
     * 根据主题id，查找回复的信息
     * @param replyId
     * @return 回复
     */
    public Reply findReply(int replyId) {
        String sql  = "select * from TBL_REPLY where replyId=?";
        Reply reply = null;
        try {
            conn  = this.getConn();                // 获得数据库连接
            pstmt = conn.prepareStatement(sql);    // 得到一个PreparedStatement对象
            pstmt.setInt(1, replyId);              // 设置topicId为参数值
            rs    = pstmt.executeQuery();          // 执行sql，取得查询结果集

            /*  将结果集中的信息取出保存到reply对象中，循环最多只会执行一次  */
            while ( rs.next() ) {
                reply = new Reply();              // 回复对象
                reply.setReplyId(rs.getInt("replyId"));
                reply.setTitle(rs.getString("title"));
                reply.setContent(rs.getString("content"));
                reply.setPublishTime(rs.getDate("publishTime"));
                reply.setModifyTime(rs.getDate("modifyTime"));
                reply.setUid(rs.getInt("uId"));
            }
        } catch (Exception e) {
            e.printStackTrace();                   // 处理异常
        } finally {
            this.closeAll(conn, pstmt, rs);       // 释放资源
        }
        return reply;
    }

}
