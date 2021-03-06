package com.dyl.community;

import com.dyl.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author admin
 */
@SpringBootTest
public class MailTests {

    @Autowired
    private MailClient mailClient;

    /**
     * 注入template模板引擎
     */
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    void testSendTextMail() {
        mailClient.sendMail("2334238730@qq.com", "TEXT", "Welcome.");
    }

    @Test
    void testSendHtmlMail() {
        Context context = new Context();
        context.setVariable("username","sunday");

        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);

        mailClient.sendMail("2334238730@qq.com", "HTML", content);
    }
}
