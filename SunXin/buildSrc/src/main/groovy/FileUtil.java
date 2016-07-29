// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import java.io.File;

/**
 * Created by 钟光燕 on 2016/7/28.
 * e-mail guangyanzhong@163.com
 */
public class FileUtil {

    public void createDir(String path){
        File file = new File(path) ;
        if (!file.exists()){
            file.mkdirs() ;
        }
        System.out.print("createDir");
        int width = 10 ;
        width = width>>1 ;
        System.out.print("createDir=-==="+width);
    }



}
