/**
 * @author duzhou
 * @date 2018年4月16日
 * @version v1.0
 */
package cn.com.ccssoft.entity;

import java.util.List;
import java.util.Map;

/**
 * 规格对象
 * 
 * @author duzhou
 * @date 2018年4月16日 上午11:28:35
 */

public class ObjectEntity {
	// 规格对象信息
	private Map<String, String> catalogObject;
	// 对象相关属性
	private List<Map<String, String>> objectColumns;

	public ObjectEntity() {
	};// 空构造函数

	public ObjectEntity(Map<String, String> catalogObject, List<Map<String, String>> objectColumns) {
		this.catalogObject = catalogObject;
		this.objectColumns = objectColumns;
	}

	public Map<String, String> getCatalogObject() {
		return catalogObject;
	}

	public void setCatalogObject(Map<String, String> catalogObject) {
		this.catalogObject = catalogObject;
	}

	public List<Map<String, String>> getObjectColumns() {
		return objectColumns;
	}

	public void setObjectColumns(List<Map<String, String>> objectColumns) {
		this.objectColumns = objectColumns;
	}

}
