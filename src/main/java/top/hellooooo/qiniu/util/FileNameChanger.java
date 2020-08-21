package top.hellooooo.qiniu.util;

import org.springframework.stereotype.Component;

/**
 * @Author Q
 * @Date 21/08/2020 21:54
 * @Description
 */
@Component
public class FileNameChanger {

    /**
     * 将带路径的本地文件名转换为HTTP文件名
     * D:\blog\image\a.jpg -> image/a.jpg
     * @param filePath
     * @return
     */
    public String changeFilePathToFile(String filePath) {
        String basePath = System.getProperty("user.dir");
//            取出文件后半部分
        String fileName = filePath
                .replace(basePath, "")
                .replaceAll("\\\\", "/")
//                取出头部的'/'
                .substring(1);
        return fileName;
    }
}
