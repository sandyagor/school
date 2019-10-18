package cn.com.ccssoft.config.yml;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="spring")
public class YmlConfigMapping {
	
	private YmlDataSourceEntity datasource;

	public YmlDataSourceEntity getDatasource() {
		return datasource;
	}

	public void setDatasource(YmlDataSourceEntity datasource) {
		this.datasource = datasource;
	}

}
