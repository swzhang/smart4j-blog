package cn.smart4j.blog.biz.aop;

import java.io.File;

import cn.smart4j.blog.core.dal.constants.OptionConstants;
import cn.smart4j.blog.core.dal.entity.Post;
import cn.smart4j.blog.service.OptionsService;
import cn.smart4j.blog.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.smart4j.blog.biz.CategoryManager;
import cn.smart4j.blog.biz.PostManager;
import cn.smart4j.blog.core.WebConstants;
import cn.smart4j.blog.core.dal.constants.PostConstants;
import cn.smart4j.blog.core.plugin.MapContainer;
import cn.smart4j.blog.core.util.NumberUtils;
import cn.smart4j.blog.service.LinkService;
import cn.smart4j.blog.service.PostService;
import cn.smart4j.blog.service.freemarker.FreeMarkerUtils;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateHashModel;

/**
 * 静态化组件
 *
 * @author zhang
 */
@Component
public class StaticTemplate {
    private static final Logger logger = LoggerFactory.getLogger(StaticTemplate.class);
    @Autowired
    private CategoryManager categoryManager;
    @Autowired
    private PostManager postManager;
    @Autowired
    private LinkService linksService;
    @Autowired
    private PostService postService;
    @Autowired
    private OptionsService optionsService;
    @Autowired
    private TagService tagService;

    /**
     * 静态化导航栏
     */
    public void staticHeader() {
        MapContainer map = new MapContainer();
        map.put("domain", WebConstants.getDomain());
        map.put("title", optionsService.getOptionValue(OptionConstants.TITLE));
        map.put("subtitle", optionsService.getOptionValue(OptionConstants.SUBTITLE));
        map.put("categorys", categoryManager.listAsTree());
        map.put("pages", postManager.listPageAsTree());

        FreeMarkerUtils.doProcessTemplate("/header.html", new File(WebConstants.APPLICATION_PATH, WebConstants.PREFIX
                + "/common/header.html"), map);

        logger.info("staticHeader");
    }

    /**
     * 静态化标签云
     */
    private void staticCloudTags() {
        MapContainer map = new MapContainer();
        map.put("tags", tagService.list());
        map.put("domain", WebConstants.getDomain());
        try {
            BeansWrapper wrapper = new BeansWrapperBuilder(Configuration.VERSION_2_3_22).build();
            TemplateHashModel thm = (TemplateHashModel) wrapper.getStaticModels().get(NumberUtils.class.getName());
            map.put("NumberUtils", thm);
        } catch (Exception e) {
            logger.error("staticCloudTags init NumberUtils error", e);
        }

        FreeMarkerUtils.doProcessTemplate("/tagcloud.html", new File(WebConstants.APPLICATION_PATH, WebConstants.PREFIX
                + "/common/tagcloud.html"), map);
        logger.info("staticCloudTags");
    }

    /**
     * 静态化文章归档
     */
    private void staticArchive() {
        MapContainer map = new MapContainer();
        map.put("archives", postService.listArchive());
        map.put("domain", WebConstants.getDomain());

        FreeMarkerUtils.doProcessTemplate("/archive.html", new File(WebConstants.APPLICATION_PATH, WebConstants.PREFIX
                + "/common/archive.html"), map);

        logger.info("staticArchive");
    }

    /**
     * 静态化友情链接
     */
    public void staticLinks() {
        MapContainer map = new MapContainer();
        map.put("links", linksService.list());

        FreeMarkerUtils.doProcessTemplate("/link.html", new File(WebConstants.APPLICATION_PATH, WebConstants.PREFIX
                + "/common/link.html"), map);
        logger.info("staticLinks");
    }

    /**
     * 静态化文章,同时静态化最近发表or顶部导航页面栏
     *
     * @param post
     */
    public void postInsertOrUpdate(Post post) {
        staticRecentOrHeader(PostConstants.TYPE_POST.equals(post.getType()));
    }

    public void postRemove(String postid, String postType) {
        staticRecentOrHeader(PostConstants.TYPE_POST.equals(postType));
    }

    /**
     * 静态化最近发表或者静态还顶部导航
     *
     * @param ispost
     */
    private void staticRecentOrHeader(boolean ispost) {
        if (ispost) {
            MapContainer param = new MapContainer("domain", WebConstants.getDomain());
            param.put("posts", postManager.listRecent(10, PostConstants.POST_CREATOR_ALL));
            FreeMarkerUtils.doProcessTemplate("/recent.html", new File(WebConstants.APPLICATION_PATH, WebConstants.PREFIX
                    + "/common/recent.html"), param);
            logger.info("staticRecent");

            staticCloudTags();
            staticArchive();
        } else {
            staticHeader();
        }
    }

}
