package cn.com.ccssoft.config.datasources;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 增加多数据源，在此配置
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017/8/18 23:46
 */
@Component
public interface DataSourceNames {
    @Value("${spring.datasource.druid.first.sourceName}")
    String FIRST = "m8cmp_alpha_microservice";
    @Value("${spring.datasource.druid.second.sourceName}")
    String SECOND = "m8cmp_alpha_business";
    @Value("${spring.datasource.druid.third.sourceName}")
    String THIRD = "3";
    @Value("${spring.datasource.druid.fourth.sourceName}")
    String FOURTH = "4";
    @Value("${spring.datasource.druid.fifth.sourceName}")
    String FIFTH = "5";
    @Value("${spring.datasource.druid.sixth.sourceName}")
    String SIXTH = "6";
    @Value("${spring.datasource.druid.seventh.sourceName}")
    String SEVENTH = "7";
    @Value("${spring.datasource.druid.eighth.sourceName}")
    String EIGHTH = "8";
}
