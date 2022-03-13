package com.dyl.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author admin
 */
@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {
}
