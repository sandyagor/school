package cn.com.ccssoft;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import cn.com.ccssoft.config.datasources.DynamicDataSourceConfig;
import cn.com.ccssoft.config.yml.YmlConfigMapping;

@MapperScan("cn.com.ccssoft.dao")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Import({DynamicDataSourceConfig.class})
@EnableConfigurationProperties({YmlConfigMapping.class})  
public class CcssoftGeneratorApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(CcssoftGeneratorApplication.class, args);
	}
}
