package cn.smart4j.blog.biz;

import cn.smart4j.blog.core.dal.constants.OptionConstants;
import cn.smart4j.blog.core.dal.constants.PostConstants;
import cn.smart4j.blog.core.util.StringUtils;
import cn.smart4j.blog.service.OptionsService;
import cn.smart4j.blog.web.backend.form.MailOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.smart4j.blog.core.util.NumberUtils;
import cn.smart4j.blog.web.backend.form.GeneralOption;
import cn.smart4j.blog.web.backend.form.PostOption;

@Component
public class OptionManager {
    @Autowired
    private OptionsService optionsService;

    /**
     * 更新网站基础设置，同时更新WebConstants中变量
     *
     * @param form
     */
    @Transactional
    public void updateGeneralOption(GeneralOption form) {
        optionsService.updateOptionValue(OptionConstants.TITLE, form.getTitle());
        optionsService.updateOptionValue(OptionConstants.SUBTITLE, form.getSubtitle());
        optionsService.updateOptionValue(OptionConstants.DESCRIPTION, form.getDescription());
        optionsService.updateOptionValue(OptionConstants.KEYWORDS, form.getKeywords());
    }

    @Transactional
    public void updatePostOption(PostOption form) {
        optionsService.updateOptionValue(OptionConstants.MAXSHOW, form.getMaxShow() + "");
        optionsService.updateOptionValue(OptionConstants.ALLOW_COMMENT, form.isAllowComment() + "");
        optionsService.updateOptionValue(OptionConstants.DEFAULT_CATEGORY_ID, form.getDefaultCategory());
    }

    @Transactional
    public void updateMailOption(MailOption form) {
        optionsService.updateOptionValue("mail_host", form.getHost());
        optionsService.updateOptionValue("mail_port", form.getPort() + "");
        optionsService.updateOptionValue("mail_username", form.getUsername());
        optionsService.updateOptionValue("mail_password", form.getPassword());
    }

    /**
     * 获取下一篇文章ID,使用select for update 保证id自增
     *
     * @return
     */
    @Transactional
    public String getNextPostid() {
        String currentid = optionsService.getOptionValueForUpdate(OptionConstants.POSTID);
        int id = NumberUtils.toInteger(currentid, PostConstants.INIT_POST_ID);
        id++;
        optionsService.updateOptionValue(OptionConstants.POSTID, id + "");

        return id + "";
    }

    /**
     * 从数据库中获取站点通用设置,不存在时返回null
     *
     * @return
     */
    public GeneralOption getGeneralOption() {
        GeneralOption form = new GeneralOption();
        form.setTitle(optionsService.getOptionValue(OptionConstants.TITLE));
        if (!StringUtils.isBlank(form.getTitle())) {
            form.setSubtitle(optionsService.getOptionValue(OptionConstants.SUBTITLE));
            form.setDescription(optionsService.getOptionValue(OptionConstants.DESCRIPTION));
            form.setKeywords(optionsService.getOptionValue(OptionConstants.KEYWORDS));
            form.setWebUrl(optionsService.getOptionValue("webUrl"));
        } else {
            form = null;
        }

        return form;
    }

    public PostOption getPostOption() {
        PostOption option = new PostOption();
        option.setMaxShow(NumberUtils.toInteger(optionsService.getOptionValue(OptionConstants.MAXSHOW), 10));
        option.setAllowComment(Boolean.parseBoolean(optionsService.getOptionValue(OptionConstants.ALLOW_COMMENT)));
        option.setDefaultCategory(optionsService.getOptionValue(OptionConstants.DEFAULT_CATEGORY_ID));

        return option;
    }

    public MailOption getMailOption() {
        MailOption option = new MailOption();
        option.setHost(optionsService.getOptionValue("mail_host"));
        if (!StringUtils.isBlank(option.getHost())) {
            option.setPort(Integer.parseInt(optionsService.getOptionValue("mail_port")));
            option.setUsername(optionsService.getOptionValue("mail_username"));
            option.setPassword(optionsService.getOptionValue("mail_password"));
        } else {
            option = null;
        }

        return option;
    }

}
