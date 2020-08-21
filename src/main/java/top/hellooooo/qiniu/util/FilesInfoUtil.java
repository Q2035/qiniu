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

    public FilesInfoUtil(QiniuConfig qiniuConfig, Uptoken uptoken, FileNameChanger fileNameChanger, PrintUtil printUtil) {
        this.qiniuConfig = qiniuConfig;
        this.uptoken = uptoken;
        this.fileNameChanger = fileNameChanger;
        this.printUtil = printUtil;
    }

    public String getSpecifiedFile(String fileName) {
        String resultName = fileNameChanger.changeFilePathToFile(fileName);
        BucketManager bucketManager = new BucketManager(auth, configuration);
        try {
            FileInfo fileInfo = bucketManager.stat(qiniuConfig.getQiniuBucketName(), resultName);
            logger.info("hash:{} fsize:{} mimeType:{} putTime:{}", fileInfo.hash, fileInfo.fsize, fileInfo.mimeType, fileInfo.putTime);
            printUtil.write(resultName + " " + fileInfo.mimeType + " " + fileInfo.putTime);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return "";
    }
}
