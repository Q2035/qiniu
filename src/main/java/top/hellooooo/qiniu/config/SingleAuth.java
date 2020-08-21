package top.hellooooo.qiniu.config;

import com.qiniu.util.Auth;

/**
 * @Author Q
 * @Date 21/08/2020 21:41
 * @Description
 */
public class SingleAuth {

//    这里写的不优美
    private static QiniuConfig qiniuConfig = new QiniuConfig();

    private static Auth auth;

    public static Auth getInstance() {
        if (auth == null) {
            auth = Auth.create(qiniuConfig.getQiniuAccessKey(), qiniuConfig.getQiniuSecretKey());
        }
        return auth;
    }
}
