package com.module1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

// 默认是开启了MongoDB，这里先禁用
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableTransactionManagement
@EnableSwagger2 //启动swagger
@MapperScan("com.module1.dao") // tk.mybatis扫描mapper目录
public class Module1Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Module1Application.class, args);
	}
}
