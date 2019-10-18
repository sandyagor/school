package cn.com.ccssoft.dao;

import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月19日 下午3:32:04
 */
public interface SysGeneratorDao {
	
	List<Map<String, Object>> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	Map<String, String> queryTable(String tableName);
	
	List<Map<String, String>> queryColumns(String tableName);
	/**
	 * 查询目录表的数据
	 * @param map
	 * @return List
	 */
	List<Map<String,Object>> catelogList(Map<String,Object> map);
	/**
	 * 查询目录总数
	 * @param map
	 * @return
	 */
	int catalogTotal(Map<String,Object> map);
	/**
	 * 按照目录编码查询目录所有对象属性
	 * @param calogCode 目录编码
	 * @return List
	 */
	List<Map<String,String>> queryCatalog(String calogCode);
	int haveTables();
}
