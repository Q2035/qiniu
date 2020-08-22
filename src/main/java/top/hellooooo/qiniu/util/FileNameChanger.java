package top.hellooooo.qiniu.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.hellooooo.qiniu.config.QiniuConfig;
import top.hellooooo.qiniu.config.QiniuKeys;

import java.util.IllegalFormatException;
import java.util.Properties;

/**
 * @Author Q
 * @Date 21/08/2020 21:54
 * @Description
 */
@Component
public class FileNameChanger {

    private final QiniuConfig qiniuConfig;

    private final Properties properties;

    public FileNameChanger(QiniuConfig qiniuConfig) {
        this.qiniuConfig = qiniuConfig;
        this.properties = qiniuConfig.getProperties();
    }

    /**
     * 将带路径的本地文件名转换为HTTP文件名
     * D:\blog\image\a.jpg -> image/a.jpg
     * @param filePath
     * @return
     */
    public String changeFilePathToFile(String filePath) throws Exception {
        String basePath = (String) properties.get(QiniuKeys.baseUploadFilePath);
        String specifiedHeader = (String) properties.get(QiniuKeys.specifiedFileHeader);
//        也就是说filePath文件并不在basePath下
        if (!filePath.contains(basePath)) {
            throw new Exception("非法文件");
        }
//            取出文件后半部分
        String fileName = filePath
                .replace(basePath, "")
                .replaceAll("\\\\", "/")
//                取出头部的'/'
                .substring(1);
        return specifiedHeader + "/" + fileName;
    }
}
