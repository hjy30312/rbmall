package qy.rb.controller.backend;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import qy.rb.common.Const;
import qy.rb.common.ResponseCode;
import qy.rb.common.ServerResponse;
import qy.rb.domain.*;
import qy.rb.service.FileService;
import qy.rb.service.RBPartBaseInfoService;
import qy.rb.util.Pagenation;
import qy.rb.util.PropertiesUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author hjy
 * @create 2018/02/18
 **/

@Controller
@RequestMapping("/manage/rb_part_base_info/")
public class RBPartBaseInfoController {

	@Resource
	FileService fileService;

	@Resource
	RBPartBaseInfoService rbPartBaseInfoService;

	@RequestMapping(value = "/list.do",method = RequestMethod.GET)
	public String RBPartBaseInfoList(ModelMap modelMap, @RequestParam(value = "pageNum", defaultValue = "1")int pageNum) {
		PageEntity pageEntity = new PageEntity();
		pageEntity.setPageNum(pageNum);
		Pagenation pagenation = rbPartBaseInfoService.selectRBPartBaseInfoList(pageEntity);
		modelMap.addAttribute("pagenation", pagenation);
		pagenation.setQueryUrl("/manage/rb_part_base_info/list.do?");
		return "back_rb_part_base_info";
	}


	@RequestMapping("add_rb_part_base_info.do")
	@ResponseBody
	public ServerResponse addRBPartBaseInfo(
			HttpSession session,
			@RequestParam(value = "partModel",required = false)String partModel,
			@RequestParam(value = "partBrand",required = false)String partBrand ,
			@RequestParam(value = "producerID",required = false)String producerID,
			@RequestParam(value = "partImagesAddress",required = false) MultipartFile partImagesAddress,
			@RequestParam(value = "partStatus",required = false)Integer partStatus,
			@RequestParam(value = "rbPartBaseInfoRemark",required = false)String rbPartBaseInfoRemark,
			HttpServletRequest request) {
		Employee employee = (Employee) session.getAttribute(Const.CURRENT_EMPLOYEE);
		if (employee == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
		}
		//校验一下是否是普通员工
		if (Const.EmployeeRole.EMPLOYEEROLE_ORDINARY_CUSTOMER.equals(employee.getEmployeeType())) {
			//是普通员工
			//上传文件
			String rbPartID =  new StringBuilder().append("RB_").append(partModel).append("_").append(producerID).toString();


			String path = request.getSession().getServletContext().getRealPath(
					"upload");
			String targetFileName = fileService.upload(partImagesAddress,path,rbPartID);
			String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;

			Map fileMap = Maps.newHashMap();
			fileMap.put("uri",targetFileName);
			fileMap.put("url",url);




			RBPartBaseInfo rbPartBaseInfo = new RBPartBaseInfo(rbPartID,partModel,partBrand,producerID
												,targetFileName,partStatus.intValue(),rbPartBaseInfoRemark);

			return rbPartBaseInfoService.insertRBPartBaseInfo(rbPartBaseInfo);
		} else {
			return ServerResponse.createByErrorMessage("无权限操作,需要普通员工权限");
		}
	}



	@RequestMapping("upload.do")
	@ResponseBody
	public ServerResponse upload(HttpSession session,
								 String remotePath,
								 @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
		Employee employee = (Employee) session.getAttribute(Const.CURRENT_EMPLOYEE);
		if (employee == null) {
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
		}
		if (Const.EmployeeRole.EMPLOYEEROLE_ORDINARY_CUSTOMER.equals(employee.getEmployeeType())) {
			String path = request.getSession().getServletContext().getRealPath(
					"upload");
			String targetFileName = fileService.upload(file,path,remotePath);
			String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;

			Map fileMap = Maps.newHashMap();
			fileMap.put("uri",targetFileName);
			fileMap.put("url",url);
			return ServerResponse.createBySuccess(fileMap);
		}else {
			return ServerResponse.createByErrorMessage("无权限操作,需要普通员工权限");
		}
	}



}
