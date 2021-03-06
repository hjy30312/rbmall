package qy.rb.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import qy.rb.common.ServerResponse;
import qy.rb.dao.PartBaseInfoDao;
import qy.rb.domain.PageEntity;
import qy.rb.domain.PartBaseInfo;
import qy.rb.service.PartBaseInfoService;
import qy.rb.util.Pagenation;

import javax.annotation.Resource;

/**
 * @author hjy
 * @create 2018/02/15
 **/

@Service("PartBaseInfoService")
public class PartBaseInfoServiceImpl implements PartBaseInfoService {

	@Resource
	PartBaseInfoDao partBaseInfoDao;


	@Override
	public Pagenation selectPartBaseInfoList(PageEntity pageEntity) {
		//算出所需数据的总条数
		int cout = partBaseInfoDao.listPartBaseInfoDataRawCount(pageEntity);
		//通过（当前页、每页显示条数、总条数） 初始化分页信息
		Pagenation pagenation = new Pagenation(pageEntity.getPageSize(), pageEntity.getPageNum(), cout);
		//通过上步骤算出要查询的 开始条数，边set 到分页入参实体类中。
		pageEntity.setStartRow(pagenation.getStartRow());
		//在查询 list 的时候，让传入的startRow 和 pageSize 作为limit 条件，添加至 sql。
		pagenation.setList(partBaseInfoDao.selectPartBaseInfoList(pageEntity));
		return pagenation;
	}



	@Override
	public Pagenation selectPartBaseInfoListByIdOrName(String partModel, String partName, PageEntity pageEntity) {
		if (StringUtils.isNoneBlank(partModel)) {
			//customerId为空不执行
			partModel = new StringBuilder().append(partModel).append("%").toString();
		} else {
			partModel = null;
		}
		if (StringUtils.isNoneBlank(partName)) {
			//partCategoryName
			partName = new StringBuilder().append(partName).append("%").toString();
		} else {
			partName = null;
		}
		//算出所需数据的总条数
		int cout = partBaseInfoDao.listPartBaseInfoDataRawCount(partModel,partName,pageEntity);

		//通过（当前页、每页显示条数、总条数） 初始化分页信息
		Pagenation pagenation = new Pagenation(pageEntity.getPageSize(), pageEntity.getPageNum(), cout);
		//通过上步骤算出要查询的 开始条数，边set 到分页入参实体类中。
		pageEntity.setStartRow(pagenation.getStartRow());
		//在查询 list 的时候，让传入的startRow 和 pageSize 作为limit 条件，添加至 sql。
		pagenation.setList(partBaseInfoDao.selectPartBaseInfoListByModelOrName(partModel,partName,pageEntity));
		return pagenation;
	}

	@Override
	public ServerResponse insertPartBaseInfo(PartBaseInfo partBaseInfo) {
		boolean flag = partBaseInfoDao.insertPartBaseInfo(partBaseInfo);
		if (flag) {
			return ServerResponse.createBySuccessMessage("添加成功");
		}
		return ServerResponse.createByErrorMessage("添加失败");
	}

	@Override
	public ServerResponse updatePartBaseInfo(PartBaseInfo partBaseInfo) {
		boolean flag = partBaseInfoDao.updatePartBaseInfo(partBaseInfo);
		if (flag) {
			return ServerResponse.createBySuccessMessage("修改成功");
		}
		return ServerResponse.createByErrorMessage("修改失败");
	}
}
