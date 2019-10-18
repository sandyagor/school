package cn.com.ccssoft.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.com.ccssoft.dao.SysGeneratorDao;
import cn.com.ccssoft.entity.ObjectEntity;
import cn.com.ccssoft.utils.GenUtils;

/**
 * 代码生成器
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月19日 下午3:33:38
 */
@Service
public class SysGeneratorService {
	@Autowired
	private SysGeneratorDao sysGeneratorDao;

	public List<Map<String, Object>> catelogList(Map<String, Object> map) {
		return sysGeneratorDao.catelogList(map);
	}

	public int catalogTotal(Map<String, Object> map) {
		return sysGeneratorDao.catalogTotal(map);
	}

	public List<Map<String, Object>> queryList(Map<String, Object> map) {
		return sysGeneratorDao.queryList(map);
	}

	public int queryTotal(Map<String, Object> map) {
		return sysGeneratorDao.queryTotal(map);
	}

	public Map<String, String> queryTable(String tableName) {
		return sysGeneratorDao.queryTable(tableName);
	}

	public List<Map<String, String>> queryColumns(String tableName) {
		return sysGeneratorDao.queryColumns(tableName);
	}

	public int haveTables() {
		return sysGeneratorDao.haveTables();
	}

	public byte[] generatorCode(String[] tableNames, String packageName, String moduleName, String inOne) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		for (String tableName : tableNames) {
			// 查询表信息
			Map<String, String> table = queryTable(tableName);
			// 查询列信息
			List<Map<String, String>> columns = queryColumns(tableName);
			// 生成代码
			GenUtils.generatorCatalogCode(table, columns, zip, packageName, moduleName, inOne);
		}
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}

	public byte[] generatorCatalogCode(String[] catalogCodes, String packageName, String moduleName, String inOne) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		for (String catalogCode : catalogCodes) {
			List<ObjectEntity> listObject = getListObject(catalogCode);
			if (listObject != null && !listObject.isEmpty()) {
				for (ObjectEntity obj : listObject) {
					// 获取对象信息
					Map<String, String> catalogObject = obj.getCatalogObject();
					// 获取属性信息
					List<Map<String, String>> objectColumns = obj.getObjectColumns();
					// 生成代码
					GenUtils.generatorCatalogCode(catalogObject, objectColumns, zip, packageName, moduleName, inOne);
				}
			}
		}
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}

	/**
	 * 按照目录编码获取 本级目录下所有对象及属性
	 * 
	 * @param catalogCode
	 *            目录Code
	 * @return
	 */
	private List<ObjectEntity> getListObject(String catalogCode) {
		List<ObjectEntity> listObject = new ArrayList<ObjectEntity>();
		List<Map<String, String>> objectList = sysGeneratorDao.queryCatalog(catalogCode);
		// 遍历获取所有对象和其属性
		Set<String> tableSet = new HashSet<String>();
		for (Map<String, String> objectMapOne : objectList) {
			Map<String, String> catalogObject = new HashMap<String, String>();
			List<Map<String, String>> objectColumns = new ArrayList<Map<String, String>>();
			String objectCode = objectMapOne.get("objectCode");// 对象编码
			String tableName = objectCode;
			String objectName = objectMapOne.get("objectName");// 对象注释
			String packageName = getCatalogCode(objectMapOne.get("catalogCode"));
			String baseObjectCode = objectMapOne.get("baseObjectCode");
			if (!tableSet.contains(tableName)) {// 作为对象名称（class）和注释
				catalogObject.put("tableName", tableName);
				catalogObject.put("tableComment", objectName);
				catalogObject.put("packageName", packageName);
				catalogObject.put("baseObjectCode", baseObjectCode);
				tableSet.add(tableName);
			}
			for (Map<String, String> objectMapTwo : objectList) {// 找到该类下的所有属性
				if (objectCode.equals(objectMapTwo.get("objectCode"))) {
					Map<String, String> columnsMap = new HashMap<String, String>();
					String columnName = objectMapTwo.get("objectAttrCode");
					columnsMap.put("columnName", columnName);
					columnsMap.put("columnComment", objectMapTwo.get("objectAttrName"));
					columnsMap.put("dataType", objectMapTwo.get("datatypeName"));
					String ispk = String.valueOf(objectMapTwo.get("pkey"));
					if ("1".equals(ispk)) {
						columnsMap.put("columnKey", "PRI");
					}
					objectColumns.add(columnsMap);
				}
			}
			if (!catalogObject.isEmpty() && !objectColumns.isEmpty()) {// 避免空值情况
				listObject.add(new ObjectEntity(catalogObject, objectColumns));
			}
		}
		return listObject;
	}

	/**
	 * @param string
	 * @return
	 */
	private String getCatalogCode(String string) {
		return StringUtils.substringAfterLast(string, "_").toLowerCase();
	}
}
