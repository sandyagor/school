MAVEN仓库推送
1、第一步
以下配置用于识别推送的仓库在需要推送的项目里面添加
<distributionManagement>
	<repository>
		<id>m8cloud</id>
		<name>m8cloud</name>
		<url>http://nexus3.m8.com/repository/m8-hosts/</url>//地址
	</repository>
	<snapshotRepository>
		<id>m8</id>
		<url>http://nexus3.m8.com/repository/m8-hosts/</url>//地址
		</snapshotRepository>
</distributionManagement>
<build>
	<finalName>m8cmp-business-feign-api</finalName>
</build>

2、第二步
需要推送的jar 指定推送JAR名称

<distributionManagement>
<repository>
<id>m8cloud</id>
<name>m8cloud</name>
<url>http://nexus3.m8.com/repository/m8-hosts/</url>
</repository>
</distributionManagement>
<build>
<finalName>m8-common</finalName>
</build>

3、第三步
Maven 多模块时跳过推送，单一无需考虑，最外层需要推送设置为false
<properties>  
 <maven.deploy.skip>true</maven.deploy.skip>  
</properties> 

4、第四步
Maven setting 文件server 节点下配置私服用户信息

<server>
<id>m8cloud</id>
<username>admin</username>
<password>admin123</password>
</server>

5、第五步
 
IDE工具中执行通过maven 命令执行推送

IDE命令：deploy -B -e -U
Maven 命令：mvn  deploy -B -e -U

mvn clean install -Denv=test package -Dmaven.test.skip=true deploy
mvn clean install deploy
