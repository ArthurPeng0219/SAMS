package com.arthur.sams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 学生个人档案管理系统 - 启动类
 *
 * 启动后访问 http://localhost:8080/ 会自动跳转到 /html/index.html
 */
@SpringBootApplication
public class SamsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SamsApplication.class, args);
    }
}
