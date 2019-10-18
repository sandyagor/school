package cn.com.ccssoft.config.yml;

public class YmlDataSourceEntity {
	private String type;
	private String driverClassName;
	private DruidEntity druid;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public DruidEntity getDruid() {
		return druid;
	}

	public void setDruid(DruidEntity druid) {
		this.druid = druid;
	}


}
