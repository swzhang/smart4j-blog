package cn.smart4j.blog.biz.editor;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;

import cn.smart4j.blog.core.dal.entity.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import cn.smart4j.blog.biz.UploadManager;
import cn.smart4j.blog.core.WebConstants;
import cn.smart4j.blog.core.plugin.MapContainer;
import cn.smart4j.blog.web.support.ServletRequestReader;
import cn.smart4j.blog.web.support.WebContextFactory;

/**
 * ueditor上传参数见:http://fex-team.github.io/ueditor/#dev-request_specification
 *
 * @author zhang
 */
@Component
public class Ueditor {
    @Autowired
    private UploadManager uploadManager;

    public MapContainer server(ServletRequestReader reader) {
        String action = reader.getAsString("action");

        MapContainer result = null;
        if ("config".equals(action)) {
            result = config();
        } else if ("uploadimage".equals(action)) {
            result = uploadImage(reader);
        } else if ("listimage".equals(action)) {

        } else if ("uploadfile".equals(action)) {

        } else {
            result = new MapContainer("state", "SUCCESS");
        }

        return result;
    }

    private MapContainer config() {
        MapContainer config = new MapContainer();
        /* 上传图片配置项 */
        config.put("imageActionName", "uploadimage");
        config.put("imageFieldName", "upfile");
        config.put("imageMaxSize", 2048000);
        config.put("imageUrlPrefix", "");
        config.put("imageAllowFiles", Arrays.asList(".png", ".jpg", ".jpeg", ".gif", ".bmp"));

        /* 上传文件配置 */
        config.put("fileActionName", "uploadfile");
        config.put("fileFieldName", "upfile");
        config.put("fileMaxSize", 51200000);
        config.put("fileAllowFiles", Arrays.asList(".png", ".jpg", ".jpeg", ".gif", ".bmp", ".zip", ".tar", ".gz", ".7z"));

        /* 上传视频配置 */
        config.put("videoActionName", "uploadvideo");
        config.put("videoFieldName", "upfile");
        config.put("videoMaxSize", 102400000);
        config.put("videoAllowFiles",
                Arrays.asList(".flv", ".swf", ".mkv", ".avi", ".rmvb", ".mpeg", ".mpg", ".mov", ".wmv", ".mp4", ".webm"));

        /* 列出指定目录下的文件 */
        config.put("fileManagerActionName", "listfile");

        /* 列出指定目录下的图片 */
        config.put("imageManagerActionName", "listimage");

        return config;
    }

    public MapContainer uploadImage(ServletRequestReader reader) {
        MultipartFile file = reader.getFile("upfile");
        Upload upload = null;
        try (InputStream in = file.getInputStream()) {
            upload = uploadManager.insertUpload(new InputStreamResource(in), new Date(), file.getOriginalFilename(),
                    WebContextFactory.get().getUser().getId());
        } catch (Exception e) {
            e.printStackTrace();
            upload = null;
        }

        if (upload == null) {
            return new MapContainer("state", "文件上传失败");
        }

        MapContainer mc = new MapContainer("state", "SUCCESS");
        mc.put("original", upload.getName());
        mc.put("title", upload.getName());
        mc.put("url", WebConstants.getDomain() + upload.getPath());

        return mc;
    }

}
