package qy.rb.dao.impl;

import org.junit.Test;
import org.springframework.stereotype.Repository;
import qy.rb.dao.BaseInfoDao;
import qy.rb.domain.PageEntity;
import qy.rb.util.DBPoolUtil;
import qy.rb.util.Pagenation;
import qy.rb.vo.BaseInfo;
import qy.rb.vo.BaseInfoDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hjy
 * @create 2018/03/06
 **/
@Repository //扫描Dao
public class BaseInfoDaoImpl implements BaseInfoDao {

	Connection conn = null;
	CallableStatement cstmt  = null;
	ResultSet rs = null;

	@Override
	public List<BaseInfo> getList(String partName,String autoStylingName,PageEntity pageEntity) {
		List<BaseInfo> baseInfoList = new ArrayList<>();
		conn = DBPoolUtil.getConnection();
		int result = 0;
		try {
			cstmt = conn.prepareCall("{call spPortalBaseInfoByPartNameOrAutoStylingName(?,?,?,?)}");
			cstmt.setString(1,partName);
			cstmt.setString(2,autoStylingName);
			cstmt.setInt(3,pageEntity.getStartRow());
			cstmt.setInt(4,pageEntity.getPageSize());
			rs = cstmt.executeQuery();
			while(rs.next()) {
				BaseInfo baseInfo = new BaseInfo();
				baseInfo.setRbPartID(rs.getString("RBPartID"));
				baseInfo.setPartName(rs.getString("PartName"));
				baseInfo.setPartModel(rs.getString("PartModel"));
				baseInfo.setPartSubtitle(rs.getString("PartSubtitle"));
				baseInfo.setOrdinaryPrice(rs.getString("OrdinaryPrice"));
				baseInfoList.add(baseInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBPoolUtil.closeConnection(conn);
		}
		return baseInfoList;
	}

	@Override
	public BaseInfoDetail getDetail(String rbPartID) {
		BaseInfoDetail baseInfoDetail = new BaseInfoDetail();
		conn = DBPoolUtil.getConnection();

		try {
			cstmt = conn.prepareCall("{call spPortalBaseInfoDetailByRBPartName(?)}");
			cstmt.setString(1, rbPartID);
			rs = cstmt.executeQuery();
			rs.next();
			baseInfoDetail.setRbPartID(rs.getString("RBPartID"));
			baseInfoDetail.setPartName(rs.getString("PartName"));
			baseInfoDetail.setPartSubtitle(rs.getString("PartSubtitle"));
			baseInfoDetail.setOrdinaryPrice(rs.getString("OrdinaryPrice"));
			baseInfoDetail.setPartModel(rs.getString("PartModel"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return baseInfoDetail;
	}


	@Override
	public List<BaseInfo> getListByPartCategoryName(String partCategoryName, PageEntity pageEntity) {

		List<BaseInfo> baseInfoList = new ArrayList<>();
		conn = DBPoolUtil.getConnection();
		try {
			cstmt = conn.prepareCall("{call spPortalBaseInfoByPartCategoryName(?,?,?)}");
			cstmt.setString(1,partCategoryName);
			cstmt.setInt(2,pageEntity.getStartRow());
			cstmt.setInt(3,pageEntity.getPageSize());
			rs = cstmt.executeQuery();
			while(rs.next()) {
				BaseInfo baseInfo = new BaseInfo();
				baseInfo.setRbPartID(rs.getString("RBPartID"));
				baseInfo.setPartName(rs.getString("PartName"));
				baseInfo.setPartModel(rs.getString("PartModel"));
				baseInfo.setPartSubtitle(rs.getString("PartSubtitle"));
				baseInfo.setOrdinaryPrice(rs.getString("OrdinaryPrice"));
				baseInfoList.add(baseInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBPoolUtil.closeConnection(conn);
		}
		return baseInfoList;
	}


	@Override
	public int getListCount(String partName, String autoStylingName, PageEntity pageEntity) {
		return 10;
	}
}
