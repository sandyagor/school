package cn.com.ccssoft.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import cn.com.ccssoft.entity.ColumnEntity;
import cn.com.ccssoft.entity.TableEntity;

/**
 * 代码生成器   工具类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月19日 下午11:40:24
 */
public class GenUtils {

    public static List<String> getTemplates(){
        List<String> templates = new ArrayList<String>();
        templates.add("template/Entity.java.vm");
        templates.add("template/PagedEntity.java.vm");
        templates.add("template/Dao.java.vm");
        templates.add("template/Dao.xml.vm");
        templates.add("template/Service.java.vm");
        templates.add("template/ServiceImpl.java.vm");
        templates.add("template/Controller.java.vm");

        templates.add("template/adminlte/list.html.vm");
        templates.add("template/adminlte/list.js.vm");

        templates.add("template/elementui/index.vue.vm");
        templates.add("template/elementui/add-or-update.vue.vm");
        templates.add("template/elementui/index.js.vm");

        return templates;
    }

    /**
     * 生成代码
     */
    public static void generatorCode(Map<String, String> table,
                                     List<Map<String, String>> columns, ZipOutputStream zip,String packageName,String moduleName,String inOne) {
        //配置信息
        Configuration config = getConfig();
        boolean hasBigDecimal = false;
        //表信息
        TableEntity tableEntity = new TableEntity();
        String tableName = table.get("tableName" );
        tableEntity.setTableName(tableName);
        tableEntity.setComments(table.get("tableComment" ));
        //表名转换成Java类名
        String className = getClassName(tableName,config.getString("separator" ));
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        boolean haveID = false;
        for(Map<String, String> column : columns){
            ColumnEntity columnEntity = new ColumnEntity();
            String columnName = column.get("columnName" );
            if("id".equalsIgnoreCase(columnName)) {
            	haveID = true;
            }
            columnEntity.setColumnName(columnName);
            columnEntity.setDataType(column.get("dataType" ));
            columnEntity.setComments(column.get("columnComment" ));
            columnEntity.setExtra(column.get("extra" ));

            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType().toLowerCase(), "String" );//未知类型转换为String
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && attrType.equals("BigDecimal" )) {
                hasBigDecimal = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey" )) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
        Velocity.init(prop);
        String mainPath = config.getString("mainPath" );
        mainPath = StringUtils.isBlank(mainPath) ? "cn.com.ccssoft" : mainPath;
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("mainPath", mainPath);
        map.put("package", config.getString("package" ));
        map.put("moduleName", config.getString("moduleName" ));
        map.put("author", config.getString("author" ));
        map.put("email", config.getString("email" ));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        map.put("haveID", haveID);
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8" );
            tpl.merge(context, sw);

            try {
                //添加到zip
            	String fileName = getFileName(template, tableEntity.getClassName(), config.getString("package" ), config.getString("moduleName" ));
                zip.putNextEntry(new ZipEntry(fileName));
                IOUtils.write(sw.toString(), zip, "UTF-8" );
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw new RRException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            }
        }
    }
    /**
     * 生成代码
     */
    public static void generatorCatalogCode(Map<String, String> table,
                                     List<Map<String, String>> columns, ZipOutputStream zip,String packageName,String moduleName,String inOne) {
        //配置信息
        Configuration config = getConfig();
        boolean hasBigDecimal = false;
        //表信息
        TableEntity tableEntity = new TableEntity();
        String tableName = table.get("tableName" );
        String tablePackage = StringUtils.substringAfterLast(tableName, "_").toLowerCase();
        tableEntity.setTableName(tableName);
        tableEntity.setComments(table.get("tableComment" ));
        //表名转换成Java类名
        String className = getClassName(tableName,config.getString("separator" ));
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        boolean haveID = false;
        for(Map<String, String> column : columns){
            ColumnEntity columnEntity = new ColumnEntity();
            String columnName = column.get("columnName" );
            if("id".equalsIgnoreCase(columnName)) {
            	haveID = true;
            }
            columnEntity.setColumnName(columnName);
            columnEntity.setDataType(column.get("dataType" ));
            columnEntity.setComments(column.get("columnComment" ));
            columnEntity.setExtra(column.get("extra" ));
            
            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType().toLowerCase(), "String" );//未知类型为String
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && attrType.equals("BigDecimal" )) {
                hasBigDecimal = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey" )) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
        Velocity.init(prop);
        String mainPath = config.getString("mainPath" );
        mainPath = StringUtils.isBlank(mainPath) ? "cn.com.ccssoft" : mainPath;//主包路径
        mainPath = StringUtils.isBlank(packageName)?mainPath:packageName;//自定义输入的包名
        //截取包名的最后一个点前的为主包路径如果小于一个点的直接就是主包
        mainPath = getMainPath(mainPath);
        if("1".equals(inOne)) {//同一个文件夹
        	moduleName = StringUtils.isBlank(moduleName)?config.getString("moduleName" ):moduleName;
        }else {
        	moduleName = StringUtils.isEmpty(table.get("packageName"))?tablePackage:table.get("packageName");
        }
        
        packageName = StringUtils.isBlank(packageName)?mainPath:packageName;//包名
        //访问路径重新定位
        String pathName = StringUtils.substringAfter(tableName, "_").replaceAll("_", "/").toLowerCase()+"s";//前端地址
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", pathName);
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("mainPath", mainPath);
        map.put("package", packageName);
        map.put("moduleName", moduleName);
        map.put("author", config.getString("author" ));
        map.put("email", config.getString("email" ));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        map.put("haveID", haveID);
        map.put("baseObjectCode", table.get("baseObjectCode"));
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8" );
            tpl.merge(context, sw);

            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template, tableEntity.getClassName(), packageName, moduleName)));
                IOUtils.write(sw.toString(), zip, "UTF-8" );
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw new RRException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            }
        }
    }

    /**
     * 截取package的最后一个点前的字符串为主包，如果少于或者等于一个直接使用
	 * @param mainPath
	 * @return
	 */
	private static String getMainPath(String mainPath) {
		if(pattern(mainPath,".")>2) {
			mainPath = StringUtils.substringBeforeLast(mainPath, ".");
		}
		return mainPath;
	}
	private static int pattern(String text,String compile) {
		String[] atext = StringUtils.split(text, compile);
		return atext.length;
	}
	/**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "" );
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "" );
        }
        return columnToJava(tableName);
    }
    /**
     * 默认类名 为表名的第二个 _ 后面的内容
     * @param tableName
     * @return
     */
    private static String getClassName(String tableName,String separator) {
    	if(null != tableName && StringUtils.isNotBlank(tableName)) {
    		if(tableName.indexOf(separator)>-1) {
    			tableName = StringUtils.substringAfter(tableName, separator);
    		}
    	}
    	return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties" );
        } catch (ConfigurationException e) {
            throw new RRException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String className, String packageName, String moduleName) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
        }

        if (template.contains("template/Entity.java.vm" )) {
            return packagePath + "entity" + File.separator + className + "Entity.java";
        }
        if (template.equalsIgnoreCase("template/PagedEntity.java.vm" )) {
            return packagePath + "entity" + File.separator + "queryEntity" + File.separator + "PagedQuery" + className + "Entity.java";
        }

        if (template.equalsIgnoreCase("template/Dao.java.vm" )) {
            return packagePath + "dao" + File.separator + className + "Dao.java";
        }

        if (template.equalsIgnoreCase("template/Service.java.vm" )) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if (template.equalsIgnoreCase("template/ServiceImpl.java.vm" )) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.equalsIgnoreCase("template/Controller.java.vm" )) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if (template.equalsIgnoreCase("template/Dao.xml.vm" )) {
            return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName + File.separator + className + "Mapper.xml";
        }

        if (template.equalsIgnoreCase("template/menu.sql.vm" )) {
            return className.toLowerCase() + "_menu.sql";
        }

        if (template.equalsIgnoreCase("template/adminlte/list.html.vm" )) {
            return "main" + File.separator + "resources" + File.separator + "adminlte" + File.separator
                    + "modules" + File.separator + moduleName + File.separator + className.toLowerCase() + ".html";
        }

        if (template.equalsIgnoreCase("template/adminlte/list.js.vm" )) {
            return "main" + File.separator + "resources" + File.separator + "adminlte" + File.separator + "js" + File.separator
                    + "modules" + File.separator + moduleName + File.separator + className.toLowerCase() + ".js";
        }

        if (template.equalsIgnoreCase("template/elementui/index.vue.vm" )) {
            return "main" + File.separator + "resources" + File.separator + "elementui" + File.separator + "src" + File.separator + "views" +
                    File.separator + className.toLowerCase() + File.separator + "index.vue";
        }

        if (template.equalsIgnoreCase("template/elementui/add-or-update.vue.vm" )) {
            return "main" + File.separator + "resources" + File.separator + "elementui" + File.separator + "src" + File.separator + "views" +
                    File.separator + className.toLowerCase() + File.separator +"add-or-update.vue";
        }

        if (template.equalsIgnoreCase("template/elementui/index.js.vm" )) {
            return "main" + File.separator + "resources" + File.separator + "elementui" + File.separator + "src" + File.separator + "api" +
                    File.separator+ "modules" + File.separator + className.toLowerCase() + ".js";
        }

        return null;
    }
}
