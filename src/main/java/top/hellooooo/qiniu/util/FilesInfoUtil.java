package top.hellooooo.qiniu.util;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.hellooooo.qiniu.config.QiniuConfig;
import top.hellooooo.qiniu.config.SingleAuth;
import top.hellooooo.qiniu.token.Uptoken;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @Author Q
 * @Date 21/08/2020 21:27
 * @Description
 */
@Component
public class FilesInfoUtil extends BaseUtil{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Auth auth = SingleAuth.getInstance();

    private final Uptoken uptoken;

    private final QiniuConfig qiniuConfig;

    private final FileNameChanger fileNameChanger;

    private final PrintUtil printUtil;

    private final SimpleDateFormat simpleDateFormat;

    public FilesInfoUtil(QiniuConfig qiniuConfig, Uptoken uptoken, FileNameChanger fileNameChanger, PrintUtil printUtil, SimpleDateFormat simpleDateFormat) {
        this.qiniuConfig = qiniuConfig;
        this.uptoken = uptoken;
        this.fileNameChanger = fileNameChanger;
        this.printUtil = printUtil;
        this.simpleDateFormat = simpleDateFormat;
    }

    /**
     * 将指定的文件信息写入文本
     * @param fileName
     */
    public void writeSpecifiedFileInfo(String fileName) {
        String resultName = fileNameChanger.changeFilePathToFile(fileName);
        BucketManager bucketManager = new BucketManager(auth, configuration);
        try {
            FileInfo fileInfo = bucketManager.stat(qiniuConfig.getQiniuBucketName(), resultName);
            logger.info("hash:{} fsize:{} mimeType:{} putTime:{}", fileInfo.hash, fileInfo.fsize, fileInfo.mimeType, fileInfo.putTime);
//            我擦，FileInfo默认单位100ns 需要除以1000变为毫秒 我不知道为啥，反正结果是正确的
            printUtil.write("name:{} type:{} size:{} putTime:{}", resultName, fileInfo.mimeType, sizeConversion(fileInfo.fsize), simpleDateFormat.format(fileInfo.putTime / 10e3));
        } catch (QiniuException e) {
            logger.warn("File {} doesn't exist", resultName);
            e.printStackTrace();
        }
    }

    public void getAllFileInfo() {
        BucketManager bucketManager = new BucketManager(auth, configuration);
//        官方文档抄的
//      文件名前缀
        String prefix = "";
//      每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
//      指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
//      列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(qiniuConfig.getQiniuBucketName(), prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            printUtil.write("----------------{}---------------",simpleDateFormat.format(new Date()));
            //处理获取的file list结果
            FileInfo[] items = fileListIterator.next();
            long totalSize = Arrays.stream(items).mapToLong(f -> f.fsize).sum();
            printUtil.write("totalCount:{} totalSize:{}", items.length, sizeConversion(totalSize));
            for (FileInfo item : items) {
//                将文件信息输出到文本文件中
                printUtil.write("key:{} hash:{} fsize:{} mimeType:{} putTime:{} endUser:{}",
                        item.key,
                        item.hash,
                        sizeConversion(item.fsize),
                        item.mimeType,
                        simpleDateFormat.format(item.putTime / 10e3),
                        item.endUser);
            }
        }
    }

    /**
     * 将字节转换为更容易看的MB KB
     * @param fsize
     * @return
     */
    private String sizeConversion(long fsize) {
        if (fsize < 1024) {
            return fsize + "B";
        }
//        KB
        if (fsize < 1024 * 1024) {
            String res = (fsize / 1024 + (float)(fsize - fsize / 1024 * 1024) / 1024 + "");
            int i = res.indexOf(".");
//            保留两位小数
            return res.substring(0, i + 3) + "K";
        }
//        MB
        if (fsize < 1024 * 1024 * 1024) {
            String res = (fsize / 1024 / 1024 + (float) (fsize - fsize / 1024 / 1024 * 1024 * 1024) / 1024 / 1024 + "");
            int i = res.indexOf(".");
//            保留两位小数
            return res.substring(0, i + 3) + "M";
        }
//        GB
        if (fsize < 1024 * 1024 * 1024 * 1024) {
            String res = (fsize / 1024 / 1024 / 1024 + (float) (fsize - fsize / 1024 / 1024 / 1024 * 1024 * 1024 * 1024) / 1024 / 1024 / 1024 + "");
            int i = res.indexOf(".");
//            保留两位小数
            return res.substring(0, i + 3) + "G";
        }
        return fsize+"B";
    }
}
