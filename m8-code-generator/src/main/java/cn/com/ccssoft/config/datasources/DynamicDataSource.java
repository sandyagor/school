package cn.com.ccssoft.config.datasources;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;


public class DynamicDataSource extends AbstractRoutingDataSource {
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	  public static String getDataSourceType() {
		  return contextHolder.get();
	  }
	  
	public DynamicDataSource(DataSource defaultTargetDataSource, Map<String, DataSource> targetDataSources) {
		super.setDefaultTargetDataSource(defaultTargetDataSource);
		super.setTargetDataSources(new HashMap<>(targetDataSources));
		super.afterPropertiesSet();
	}

	@Override
	protected Object determineCurrentLookupKey() {
		return getDataSourceType();
	}

	@Override
	protected DataSource determineTargetDataSource() {
		return super.determineTargetDataSource();
	}

	@Override
	public Connection getConnection() throws SQLException {
		return super.getConnection();
	}

	@Override
	public void setTargetDataSources(Map<Object, Object> targetDataSources) {
		super.setTargetDataSources(targetDataSources);
	}

	public static void setDataSource(String dataSource) {
		contextHolder.set(dataSource);
	}

	public static String getDataSource() {
		return contextHolder.get();
	}

	public static void clearDataSource() {
		contextHolder.remove();
	}

}
