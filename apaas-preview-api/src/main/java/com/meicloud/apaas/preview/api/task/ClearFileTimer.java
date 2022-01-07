package com.meicloud.apaas.preview.api.task;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/***
 * 
 * @author chenlei140*
 * @className ClearFileTimer*
 * @description 临时文件清除定时器*
 * @date 2022/1/7 13:57
 */
@Component
public class ClearFileTimer {
    private static final Logger logger = LoggerFactory.getLogger(ClearFileTimer.class);

    @Value("${meicloud.apaas.preview.store-path}")
    private String storePath;

    /**
     * @Description: 每小时执行一次文件清理(清理临时存储文件)
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void clear() {
        logger.info(" \n 临时文件清理 start...");

        File dir = new File(storePath);
        // 此处连续尝试删除3次,有可能文件正在被其他线程打开,而删除失败
        deleteDir(dir);
        for (int i = 0; i < 3; i++) {
            deleteDir(dir);
            boolean flag = dir.delete();
            if (flag) {
                // 删除成功,结束循环
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        logger.info(" \n 临时文件清理 end...");

    }

    /**
     * @param dir
     * @Description: 删除文件夹下所有内容, 不会删除文件夹本身
     */
    private void deleteDir(File dir) {
        if (!dir.isDirectory()) {
            return;
        }
        File[] children = dir.listFiles();
        logger.info(" \n 当前临时文件夹下有 【{}】个文件", children.length);
        for (File child : children) {
            if (child.isDirectory()) {
                deleteDir(child);
            }
            child.delete();
        }
    }
}
