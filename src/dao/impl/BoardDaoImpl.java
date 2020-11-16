package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.BoardDao;
import entity.Board;

public class BoardDaoImpl extends BaseDao implements BoardDao {
	private Connection conn = null; // ���ڱ������ݿ�����
	private PreparedStatement pstmt = null; // ����ִ��SQL���
	private ResultSet rs = null; // �û������ѯ�����
	private HashMap map = new HashMap(); // ��������Ϣ��Map
	private int parentId = 0; // �����id��ʼֵΪ0��parentNo����Ϊmap��key
	private List sonList = null; // ��������ͬһ��������һ���Ӱ�飬����Ϊmap��value

	/**
	 * ���Ұ��
	 * 
	 * @return ��װ�˰����Ϣ��Map
	 */
	public Map findBoard() {
		String sql = "select * from TBL_BOARD order by parentId,boardId"; // ��ѯ����sql���
		try {
			conn = this.getConn(); // �õ����ݿ�����
			pstmt = conn.prepareStatement(sql); // �õ�PreparedStatement����
			rs = pstmt.executeQuery(); // ִ��sqlȡ�ý����
			sonList = new ArrayList(); // ʵ����

			/* ѭ���������Ϣ��װ��Map */
			while (rs.next()) {
				if (parentId != rs.getInt("parentId")) {
					map.put(new Integer(parentId), sonList); // ����һ���Ӱ�鱣�浽map��
					sonList = new ArrayList(); // ���²���һ��ArrayList�������ڴ����һ���Ӱ��
					parentId = rs.getInt("parentId"); //ΪparentNo������ֵ������map����keyֵ
				}
				Board board = new Board(); // ������
				board.setBoardId(rs.getInt("boardId")); // ���id
				board.setBoardName(rs.getString("boardName")); // �������
				sonList.add(board); // ��������ͬһ�������Ӱ��
			}
			map.put(new Integer(parentId), sonList); // ���潫���һ��sonList
		} catch (Exception e) {
			e.printStackTrace(); // �����쳣
		} finally {
			closeAll(conn, pstmt, rs); // �ͷ���Դ
		}
		return map;
	}

	/**
     * ���ݰ��id���Ұ��
     * @param boardId
     * @return
     */
    public Board findBoard(int boardId) {
        String sql  =  "select * from TBL_BOARD where boardId=" + boardId;  //��ѯ����sql���
        Board board =  null;                                       // ����һ���յİ�����
        try {
            conn    =  this.getConn();                             // �õ����ݿ�����
            pstmt   =  conn.prepareStatement(sql);                 // �õ�PreparedStatement����
            rs      =  pstmt.executeQuery();                       // ִ��sqlȡ�ý����
            while( rs.next() ) {
                board = new Board();                               // ʵ����������
                board.setBoardId( rs.getInt("boardId") );           // ���id
                board.setBoardName( rs.getString("boardName") );    // �������
                board.setParentId( rs.getInt("parentId") );         // �����id
            }
        } catch ( Exception e ) {
            e.printStackTrace();                                    // �����쳣
        } finally {
            closeAll(conn,pstmt,rs);                                // �ͷ���Դ
        }
        return board;
    }
}
