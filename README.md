# maven-war-plugin
	Java WEB JS,CSS,HTML压缩，代码注入，静态资源文件引用文件MD5处理等等

####项目描述
	该Maven插件在apache的maven-war-plugin插件基础之上扩展
	配置文件package.conf拷贝到maven 资源文件路径即可
	
	package.conf
	配置如下：
	#CDN HOST
	cdn.base.url=http://xxx.cdn.com/
	#Jsp中Script.src,Style.href,img.src{增加CDN链接|v=\d+版本控制}
	jsp.filter=/src/main/webapp/WEB-INF/jsp/|/src/main/webapp/WEB-INF/tags/
	#CSS文件压缩
	css.file.compress.filter=/src/main/webapp/resources/
	#JSP中引用JS,CSS 生成MD5引用处理
	jsp.file.md5.filter=/src/main/webapp/WEB-INF/jsp/
	#JS 文件中文生成Unicode
	file.chinese.native2ascii=js
	#匹配删除文件
	matches.delete.files=/src/main/webapp/resources/**/node_modules/*|/src/main/webapp/build
	#head->代码注入
	head.inject.code=<style type="text/css">iframe{display: none !important; width: 0px !important;height: 0px !important;visibility: hidden !important;}</style>
	#匹配注入路径
	head.inject.path=/src/main/webapp/WEB-INF/jsp/
	#压缩HTML代码片段------------------压缩JSP中的HTML
	html.compress.code=/src/main/webapp/WEB-INF/jsp/
	#Js文件压缩
	/src/main/webapp/resources/common/js/common*|\
	
	#压缩JS代码片段------------------JSP文件中的JS块压缩
	jsp.js.chunk.compress.filter=/src/main/webapp/WEB-INF/tags/
	
	
	#压缩CSS代码块------------------JSP文件中的CSS块压缩
	jsp.css.chunk.compress.filter=/src/main/webapp/WEB-INF/jsp/
	
	#跳过所有处理
	#skip_all=true
	#JSP中JS,CSS 过滤压缩处理： @exclude.compress
	#压缩HTML:  <!--COMPRESS.CODE.START--> 待压缩HTML代码段 <!--COMPRESS.CODE.END-->


####项目依赖
	Maven项目POM文件中使用该插件
	<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-war-plugin</artifactId>
				<version>V3.1.4</version>
				<configuration>
					<warName>ProjectName</warName>
					<packageConf>${basedir}/target/classes/package.conf</packageConf>
				</configuration>
			</plugin>
			
			
####mvn package 打包
	该插件运行机制是，在打包时，进行对资源文件进行处理。
	例如:mvn package -Dmaven.test.skip=true
	 			