package com.nexfly.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({ "com.nexfly.**.mapper" })
public class MybatisConfiguration {
}
