package top.hellooooo.qiniu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Q
 * @Date 2020/8/19 9:06 PM
 * @Description
 */
@ComponentScan("top.hellooooo.qiniu")
@Configuration
public class BaseConfig {

    @Bean
    public QiniuConfig qiniuConfig(){
        return new QiniuConfig();
    }
}
