package cn.com.ccssoft.entity;

/**
 * 
 * @author duzhou
 * @email duzhou@ccssoft.com.cn
 */
public class CatalogEntity {
	//目录编码
	private String catalogCode;
	//目录名称
	private String catalogName;
	//目录类型
	private String catalogTypeName;
	//备注
	private String catalogNote;
	public String getCatalogCode() {
		return catalogCode;
	}
	public void setCatalogCode(String catalogCode) {
		this.catalogCode = catalogCode;
	}
	public String getCatalogName() {
		return catalogName;
	}
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	public String getCatalogTypeName() {
		return catalogTypeName;
	}
	public void setCatalogTypeName(String catalogTypeName) {
		this.catalogTypeName = catalogTypeName;
	}
	public String getCatalogNote() {
		return catalogNote;
	}
	public void setCatalogNote(String catalogNote) {
		this.catalogNote = catalogNote;
	}
	
}
