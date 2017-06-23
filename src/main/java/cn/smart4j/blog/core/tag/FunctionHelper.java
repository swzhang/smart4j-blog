package cn.smart4j.blog.core.tag;

import java.net.URI;
import java.util.Collection;

import cn.smart4j.blog.core.util.CollectionUtils;
import cn.smart4j.blog.core.util.StringUtils;
import cn.smart4j.blog.web.support.WebContext;
import cn.smart4j.blog.web.support.WebContextFactory;
import cn.smart4j.blog.core.util.CookieUtil;
import cn.smart4j.blog.core.util.FileUtils;

public class FunctionHelper {

    private FunctionHelper() {
    }

    /**
     * 获取base64解码后的cookie值
     *
     * @param name
     * @return
     */
    public static String cookieValue(String name) {
        WebContext context = WebContextFactory.get();
        CookieUtil cookieUtil = new CookieUtil(context.getRequest(), context.getResponse());
        return cookieUtil.getCookie(name);
    }

    /**
     * 获取制定url的域名链接，包含path路径
     *
     * @param url
     * @return
     */
    public static String getDomainLink(String url) {
        if (StringUtils.isBlank(url))
            return "";

        URI uri = URI.create(url);
        String result = uri.getHost();
        if (uri.getPort() != -1 && uri.getPort() != 80) {
            result += ":" + uri.getPort();
        }

        if (!"/".equals(uri.getPath())) {
            result += uri.getPath();
        }

        if (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    public static String fileExt(String filename) {
        return FileUtils.getFileExt(filename).toUpperCase();
    }

    public static String join(Collection<String> collect, String delimiter) {
        return CollectionUtils.isEmpty(collect) ? null : StringUtils.join(collect, delimiter);
    }

}
