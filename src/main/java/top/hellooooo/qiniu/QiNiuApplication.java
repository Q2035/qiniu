package top.hellooooo.qiniu;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.hellooooo.qiniu.config.BaseConfig;
import top.hellooooo.qiniu.util.FilesInfoUtil;
import top.hellooooo.qiniu.util.UploadUtil;

/**
 * @Author Q
 * @Date 2020/8/19 8:54 PM
 * @Description
 */
public class QiNiuApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BaseConfig.class);
//        当前工作路径
        UploadUtil uploadUtil = applicationContext.getBean(UploadUtil.class);
//        uploadUtil.upload("D:\\Private\\github\\qiniu\\image\\yunduo.png");
        FilesInfoUtil bean = applicationContext.getBean(FilesInfoUtil.class);
//        bean.writeSpecifiedFileInfo("D:\\Private\\github\\qiniu\\image\\yunduo.png");
        bean.getAllFileInfo();
    }
}
