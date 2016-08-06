// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by 钟光燕 on 2016/7/28.
 * e-mail guangyanzhong@163.com
 */
public class FileUtil {

    public void createDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        System.out.print("createDir");
        int width = 10;
        width = width >> 1;
        System.out.print("createDir=-===" + width);
    }

    public String getPropertyEnv(String filePath) {
        String env = "";
        Properties p = new Properties();

        InputStream is;
        try {
            is = new FileInputStream(filePath);
            try {
                p.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            env = p.getProperty("sdk.dir");
            System.out.print("createDir=-===" + env);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return env;
    }


}
