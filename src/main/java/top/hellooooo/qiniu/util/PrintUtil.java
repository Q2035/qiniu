package top.hellooooo.qiniu.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.hellooooo.qiniu.config.QiniuConfig;
import top.hellooooo.qiniu.config.QiniuKeys;

import java.io.*;

/**
 * @Author Q
 * @Date 21/08/2020 22:07
 * @Description
 */
@Component
public class PrintUtil {

    private OutputStreamWriter outputStreamWriter;

    @Autowired
    public PrintUtil(QiniuConfig qiniuConfig) {
        String outputFilePath = qiniuConfig.getProperties().getProperty(QiniuKeys.outputFilePath);
        File destFile = new File(outputFilePath);
        if (!destFile.exists()) {
            try {
                destFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        指定文件输出的方式为尾部追加
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath, true)) {
            outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String res) {
        try {
            outputStreamWriter.write(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
