package com.meicloud.ship.preview.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

@Slf4j
public class HtmlParseUtil {

    public static String editHtml(File targetFile, String storePath) {
        String html = "";
        try {
            Document document = Jsoup.parse(targetFile, "GB2312");
            Elements imgList = document.body().getElementsByTag("img");
            if (CollectionUtils.isEmpty(imgList)) {
                return html;
            }
            for (Element img : imgList) {
                String src = img.attr("src");
                src = URLDecoder.decode(src, "utf-8");
                String imgPath = storePath + "/" + src;
                String base64Code = Base64Util.encodeBase64File(imgPath);
                img.attr("src", "data:image/png;base64," + base64Code);
            }
            html = document.html();
        } catch (IOException e) {
            log.error("jsoup parse html file error: {} ", e.getStackTrace());
        }
        return html;
    }
}