package com.dyl.community;

import java.io.IOException;

public class WkTests {

    public static void main(String[] args) {
        String cmd = "d:/app/wkhtmltopdf/bin/wkhtmltoimage --quality 75 https://blog.dyl.fit D:/Learn/data/community/wk-images/2.png";
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
