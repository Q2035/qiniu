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
public class UploadUtil extends BaseUtil{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Uptoken uptoken;

    @Autowired
    private FileNameChanger fileNameChanger;

    /**
     * 这里传入的是详细的文件名位置：eg:/home/root/image/a.jpg
     * @param uploadFilePath
     */
    public void upload(String uploadFilePath){
        try {
            logger.info("try to upload {}", uploadFilePath);
            Response response = uploadManager.put(uploadFilePath, fileNameChanger.changeFilePathToFile(uploadFilePath), uptoken.getUptoken());
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
