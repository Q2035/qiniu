package top.hellooooo.qiniu.util;

import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;

/**
 * @Author Q
 * @Date 21/08/2020 21:32
 * @Description
 */
public abstract class BaseUtil {

//    这里似乎和文档提供的不一样
//    在查看文件信息时，这里不能使用autoZone，得使用详细的Zone，不然会报UnsupportedOperationException
//    发现了，上传的时候得用autoZone
//    拉取文件信息的使用又得用zone0等具体的Zone
    protected Configuration autoZoneConfiguration = new Configuration(Zone.autoZone());

    protected Configuration zone0Configuration = new Configuration(Zone.zone0());

    protected UploadManager zone0UploadManager = new UploadManager(autoZoneConfiguration);
}
