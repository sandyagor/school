package cn.com.ccssoft.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.com.ccssoft.config.datasources.DataSourceNames;
import cn.com.ccssoft.config.datasources.DynamicDataSource;
import cn.com.ccssoft.config.yml.YmlConfigMapping;
import cn.com.ccssoft.service.SysGeneratorService;
import cn.com.ccssoft.utils.PageUtils;
import cn.com.ccssoft.utils.Query;
import cn.com.ccssoft.utils.R;

/**
 * 代码生成器
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月19日 下午9:12:58
 */
@Controller
@RequestMapping("/sys/generator")
public class SysGeneratorController {
	@Autowired
	private SysGeneratorService sysGeneratorService;
	
	@Autowired  
    private YmlConfigMapping ymlConfigMapping;
	
	@ResponseBody
	@RequestMapping("/dselect")
	public R dselect(@RequestParam Map<String, Object> params){
		Object dataSource = params.get("dataSourceName");
		if(dataSource != null) {
			DynamicDataSource.setDataSource(dataSource.toString());
		}
		return R.ok().put("sourceName", DynamicDataSource.getDataSource());
	}
	
	@ResponseBody
	@RequestMapping("/dataSourceInfo")
	public R getDataSourceList(@RequestParam Map<String, Object> params){
		List<String> sourceList = ymlConfigMapping.getDatasource().getDruid().getSourceName();
		return R.ok().put("dsOptions", sourceList);
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		String dataSourceName = String.valueOf(params.get("dataSourceName"));
		setCurentDataSourceName(dataSourceName);
		//查询列表数据
		Query query = new Query(params);
		List<Map<String, Object>> list = sysGeneratorService.queryList(query);
		int total = sysGeneratorService.queryTotal(query);
		PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil).put("ds", DynamicDataSource.getDataSource());
	}
	
	@ResponseBody
	@RequestMapping("/catalogList")
	public R catelogList(@RequestParam Map<String,Object> params) {
		String dataSourceName = String.valueOf(params.get("dataSourceName"));
		setCurentDataSourceName(dataSourceName);
		int tableNum = sysGeneratorService.haveTables();
		if(tableNum!=5) {
			return R.error(410, "未找到元数据管理中的表");
		}
		//查询目录
		Query query = new Query(params);
		List<Map<String,Object>> list = sysGeneratorService.catelogList(query);
		int total = sysGeneratorService.catalogTotal(query);
		PageUtils pageUtil = new PageUtils(list,total,query.getLimit(),query.getPage());
		return R.ok().put("page", pageUtil);
	}
	/**
	 * 生成代码
	 */
	@RequestMapping("/code")
	public void code(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String,Object> params) throws IOException{
		String[] tableNames = new String[]{};
		String tables = request.getParameter("tables");
		tableNames = JSON.parseArray(tables).toArray(tableNames);
		
		String packageName = request.getParameter("packageName");//界面用户输入的包名
		String inOne = request.getParameter("selected");//一个目录一个包？
		String moduleName = request.getParameter("moduleName");
		String fileName = StringUtils.isEmpty(moduleName)?"code":moduleName;
		String dataSourceName = request.getParameter("dataSourceName");
		setCurentDataSourceName(dataSourceName);
		
		byte[] data = sysGeneratorService.generatorCode(tableNames,packageName,moduleName,inOne);
		if(data!=null) {
			response.reset();  
	        response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+".zip\"");  
	        response.addHeader("Content-Length", "" + data.length);  
	        response.setContentType("application/octet-stream; charset=UTF-8");  
	        IOUtils.write(data, response.getOutputStream()); 
		}
		 
	}
	/**
	 *  按照目录编码生成代码
	 */
	@RequestMapping("/catalogCode")
	public void catalogCode(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String,Object> params) throws IOException{
		String[] catalogCodes = new String[]{};
		String tables = request.getParameter("catalogCode");
		catalogCodes = JSON.parseArray(tables).toArray(catalogCodes);
		
		String packageName = request.getParameter("packageName");//界面用户输入的包名
		String inOne = request.getParameter("selected");//一个目录一个包？
		String moduleName = request.getParameter("moduleName");
		String fileName = StringUtils.isEmpty(moduleName)?"code":moduleName;
		String dataSourceName = request.getParameter("dataSourceName");
		setCurentDataSourceName(dataSourceName);
		
		byte[] data = sysGeneratorService.generatorCatalogCode(catalogCodes,packageName,moduleName,inOne);
		if(data!=null) {
			response.reset();  
	        response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+".zip\"");  
	        response.addHeader("Content-Length", "" + data.length);  
	        response.setContentType("application/octet-stream; charset=UTF-8");  
	  
	        IOUtils.write(data, response.getOutputStream());
		}
		  
	}
	
	private void setCurentDataSourceName(String dataSourceName) {
		String sysDataSource = DynamicDataSource.getDataSource();
		if(null != dataSourceName && !dataSourceName.isEmpty()) {
			sysDataSource = dataSourceName;
		}
		else if(null != sysDataSource && !sysDataSource.isEmpty()) {
			;
		}else {
			sysDataSource = DataSourceNames.FIRST;
		}
		DynamicDataSource.setDataSource(sysDataSource);
	}
}
