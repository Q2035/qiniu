package top.hellooooo.qiniu.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.hellooooo.qiniu.config.QiniuConfig;
import top.hellooooo.qiniu.config.QiniuKeys;

import java.io.*;
import java.text.SimpleDateFormat;

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
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath, true);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将res输入文件中，这里可以采用与slf4j的log差不多的采用大括号作为占位符
     *
     * @param format
     */
    public void write(String format, Object... params) {

        try {
            outputStreamWriter.write(parsePlaceholders(format, params));
            outputStreamWriter.write("\n");
            outputStreamWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析传入的字符串的占位符
     * @param format
     * @param params
     * @return
     */
    public String parsePlaceholders(String format, Object... params) {
        StringBuilder res = new StringBuilder();
        char[] chars = format.toCharArray();
        int tempCount = 0;
        boolean tryToPlace = true;
//        length-1防止出现数组越界
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == '{' && chars[i + 1] == '}' && tryToPlace) {
//                直接调用toString方法
                res.append(params[tempCount++].toString());
//                占位符数目不匹配
                if (tempCount > params.length - 1) {
                    tryToPlace = false;
                }
                i += 2;
            }
            if (i >= chars.length - 1) {
                break;
            }
            res.append(chars[i]);
        }
        if (chars[chars.length - 1] != '}') {
            res.append(chars[chars.length - 1]);
        }
        return res.toString();
    }
}
