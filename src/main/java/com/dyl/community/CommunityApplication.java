package com.dyl.community;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author admin
 */
@SpringBootApplication
@Slf4j
public class CommunityApplication {

    @PostConstruct
    public void init() {
        // 解决netty启动冲突问题
        // see Netty4Utils.setAvailableProcessors()
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(CommunityApplication.class, args);
        log.info("..######..##.....##..######...######..########..######...######.\n" +
                ".##....##.##.....##.##....##.##....##.##.......##....##.##....##\n" +
                ".##.......##.....##.##.......##.......##.......##.......##......\n" +
                "..######..##.....##.##.......##.......######....######...######.\n" +
                ".......##.##.....##.##.......##.......##.............##.......##\n" +
                ".##....##.##.....##.##....##.##....##.##.......##....##.##....##\n" +
                "..######...#######...######...######..########..######...######.");
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        if (!StringUtils.hasLength(path)) {
            path = "";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application  is running! Access URLs:\n\t" +
                "Local访问网址: \t\thttp://localhost:" + port + path + "/index" + "\n\t" +
                "External访问网址: \thttp://" + ip + ":" + port + path + "\n\t" +
                "----------------------------------------------------------");
    }

}
