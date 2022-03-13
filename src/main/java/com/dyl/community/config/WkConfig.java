package com.dyl.community.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @author admin
 */
@Configuration
public class WkConfig {

    public static final Logger logger = LoggerFactory.getLogger(WkConfig.class);

    @Value("${wk.image.storage}")
    private String wkImageStorage;

    @Value("${wk.pdf.storage}")
    private String wkPdfStorage;

    @PostConstruct
    public void init() {
        // 创建wk图片目录
        File file = new File(wkImageStorage);
        if (!file.exists()) {
            file.mkdir();
            logger.info("创建wk图片目录：" + wkImageStorage);
        }

        File file1 = new File(wkPdfStorage);
        if (!file1.exists()) {
            file1.mkdir();
            logger.info("创建wkPDF目录：" + wkPdfStorage);
        }
    }
}
