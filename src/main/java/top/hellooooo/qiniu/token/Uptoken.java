package top.hellooooo.qiniu.token;

import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.hellooooo.qiniu.config.QiniuConfig;
import top.hellooooo.qiniu.config.SingleAuth;

/**
 * @Author Q
 * @Date 21/08/2020 08:50
 * @Description
 */
@Component
public class Uptoken {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String uptoken;

    private Auth auth = SingleAuth.getInstance();

    @Autowired
    public Uptoken(QiniuConfig qiniuConfig){
//        获取upToken
        uptoken = auth.uploadToken(qiniuConfig.getQiniuBucketName());
        logger.info("Get uptoken successfully {}", uptoken);
    }

    public String getUptoken() {
        return uptoken;
    }
}
