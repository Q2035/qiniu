package top.hellooooo.qiniu.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.hellooooo.qiniu.token.Uptoken;

/**
 * @Author Q
 * @Date 21/08/2020 09:04
 * @Description
 */
@Component
public class UploadUtil {

//    这里似乎和文档提供的不一样
    private Configuration configuration = new Configuration(Zone.autoZone());

    private UploadManager uploadManager = new UploadManager(configuration);

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Uptoken uptoken;

    /**
     * 这里传入的是详细的文件名位置：eg:/home/root/image/a.jpg
     * @param uploadFilePath
     */
    public void upload(String uploadFilePath){
        try {
            logger.info("try to upload {}", uploadFilePath);
            String basePath = System.getProperty("user.dir");
            String fileName;
//            取出文件后半部分
            fileName = uploadFilePath.replace(basePath, "").replaceAll("\\\\", "/").substring(1);
            Response response = uploadManager.put(uploadFilePath, fileName, uptoken.getUptoken());
            DefaultPutRet defaultPutRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            logger.info("key {} hash {}", defaultPutRet.key, defaultPutRet.hash);
        } catch (QiniuException e) {
            Response r = e.response;
            logger.error(r.toString());
            try {
                logger.error(r.bodyString());
            } catch (QiniuException qiniuException) {
                qiniuException.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
