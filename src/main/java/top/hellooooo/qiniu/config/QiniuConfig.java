package top.hellooooo.qiniu.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author Q
 * @Date 21/08/2020 08:35
 * @Description
 */
public class QiniuConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String QiniuAccessKey;

    private String QiniuSecretKey;

    private String QiniuBucketName;

    private Properties properties = new Properties();

    public QiniuConfig(){
        InputStream inputStream = getClass().getResourceAsStream("/application.properties");
        try {
            properties.load(inputStream);
            QiniuAccessKey = properties.getProperty(QiniuKeys.accesskey);
            QiniuSecretKey = properties.getProperty(QiniuKeys.secretkey);
            QiniuBucketName = properties.getProperty(QiniuKeys.bucketName);
        } catch (IOException e) {
            logger.error("The properties info is illegal");
            e.printStackTrace();
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public String getQiniuBucketName() {
        return QiniuBucketName;
    }

    public void setQiniuBucketName(String qiniuBucketName) {
        QiniuBucketName = qiniuBucketName;
    }

    public String getQiniuAccessKey() {
        return QiniuAccessKey;
    }

    public void setQiniuAccessKey(String qiniuAccessKey) {
        QiniuAccessKey = qiniuAccessKey;
    }

    public String getQiniuSecretKey() {
        return QiniuSecretKey;
    }

    public void setQiniuSecretKey(String qiniuSecretKey) {
        QiniuSecretKey = qiniuSecretKey;
    }
}
