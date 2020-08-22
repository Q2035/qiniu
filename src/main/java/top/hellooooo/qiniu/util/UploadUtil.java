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
import top.hellooooo.qiniu.config.QiniuConfig;
import top.hellooooo.qiniu.config.QiniuKeys;
import top.hellooooo.qiniu.token.Uptoken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Q
 * @Date 21/08/2020 09:04
 * @Description
 */
@Component
public class UploadUtil extends BaseUtil{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Uptoken uptoken;

    private final QiniuConfig qiniuConfig;

    private final FileNameChanger fileNameChanger;

    public UploadUtil(Uptoken uptoken, FileNameChanger fileNameChanger, QiniuConfig qiniuConfig) {
        this.uptoken = uptoken;
        this.fileNameChanger = fileNameChanger;
        this.qiniuConfig = qiniuConfig;
    }

    /**
     * 这里传入的是详细的文件名位置：eg:/home/root/image/a.jpg
     * @param uploadFilePath
     */
    public void upload(String uploadFilePath){
        try {
            logger.info("try to upload {}", uploadFilePath);
            String cdnURL = (String) qiniuConfig.getProperties().get(QiniuKeys.qiniuCdnURL);
            String resultFileName = fileNameChanger.changeFilePathToFile(uploadFilePath);
            Response response = zone0UploadManager.put(uploadFilePath, resultFileName, uptoken.getUptoken());
            DefaultPutRet defaultPutRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            logger.info("key {} hash {}", defaultPutRet.key, defaultPutRet.hash);
            logger.info("the url is {}",cdnURL + resultFileName);
        } catch (QiniuException e) {
            Response r = e.response;
            logger.error(r.toString());
            try {
                logger.error(r.bodyString());
            } catch (QiniuException qiniuException) {
                qiniuException.printStackTrace();
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传所有指定文件夹下的所有文件
     */
    public void batchUpload() {
        String filePath = qiniuConfig.getProperties().getProperty(QiniuKeys.baseUploadFilePath);
        File baseFilePath = new File(filePath);
        if (!baseFilePath.isDirectory()) {
            logger.warn("Sorry, '{}' is not directory.", baseFilePath);
            return;
        }
        List<File> list = new ArrayList<>(16);
//        列出filePath目录及其子目录下的所有文件
        getFoldersAndSubFolderFiles(baseFilePath, list);
//        遍历所有文件
        list.forEach(file -> upload(file.getAbsolutePath()));
    }

    /**
     * 递归获取指定目录及其子目录下所有文件
     * @param baseFile
     * @param list
     */
    private void getFoldersAndSubFolderFiles(File baseFile, List list){
        File[] files = baseFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                getFoldersAndSubFolderFiles(file, list);
            }
            if (file.isFile()) {
                list.add(file);
            }
        }
    }
}
