package cn.smart4j.blog.web.backend.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.smart4j.blog.biz.CommentManager;
import cn.smart4j.blog.biz.PostManager;
import cn.smart4j.blog.core.Constants;
import cn.smart4j.blog.core.dal.constants.PostConstants;
import cn.smart4j.blog.core.dal.entity.User;
import cn.smart4j.blog.core.plugin.MapContainer;
import cn.smart4j.blog.core.util.CookieUtil;
import cn.smart4j.blog.core.util.ServletUtils;
import cn.smart4j.blog.core.util.StringUtils;
import cn.smart4j.blog.service.CommentService;
import cn.smart4j.blog.service.PostService;
import cn.smart4j.blog.service.UploadService;
import cn.smart4j.blog.service.UserService;
import cn.smart4j.blog.service.shiro.StatelessToken;
import cn.smart4j.blog.service.vo.OSInfo;
import cn.smart4j.blog.web.backend.form.LoginForm;
import cn.smart4j.blog.web.backend.form.validator.LoginFormValidator;
import cn.smart4j.blog.web.support.CookieRememberManager;
import cn.smart4j.blog.web.support.WebContextFactory;

@Controller
@RequestMapping("/backend")
public class BackendController {
    @Autowired
    private UserService userService;
    @Autowired
    private PostManager postManager;
    @Autowired
    private CommentManager commentManager;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UploadService uploadService;

    @RequiresRoles(value = {"admin", "editor"}, logical = Logical.OR)
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("osInfo", OSInfo.getCurrentOSInfo());

        /* 基本站点统计信息 */
        model.addAttribute("userCount", userService.count());
        model.addAttribute("postCount", postService.count());
        model.addAttribute("commentCount", commentService.count());
        model.addAttribute("uploadCount", uploadService.count());

        model.addAttribute("posts", postManager.listRecent(10, PostConstants.POST_CREATOR_ALL));
        model.addAttribute("comments", commentManager.listRecent());
        return "backend/index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(String msg, Model model) {
        if (WebContextFactory.get().isLogon())
            return "redirect:/backend/index";

        if ("logout".equals(msg)) {
            model.addAttribute("msg", "您已登出。");
        } else if ("unauthenticated".equals(msg)) {
            model.addAttribute("msg", "你没有当前操作权限");
        }
        return "backend/login";
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        CookieRememberManager.logout(request, response);
        SecurityUtils.getSubject().logout();
        CookieUtil cookieUtil = new CookieUtil(request, response);
        cookieUtil.removeCokie(Constants.COOKIE_CSRF_TOKEN);
        cookieUtil.removeCokie("comment_author");
        cookieUtil.removeCokie("comment_author_email");
        cookieUtil.removeCokie("comment_author_url");

        return "redirect:/backend/login?msg=logout";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String dashboard(LoginForm form, HttpServletRequest request, HttpServletResponse response) {
        MapContainer result = LoginFormValidator.validateLogin(form);
        if (!result.isEmpty()) {
            request.setAttribute("msg", result.get("msg"));
            return "backend/login";
        }

        User user = userService.login(form.getUsername(), form.getPassword());
        if (user == null) {
            request.setAttribute("msg", "用户名密码错误");
            return "backend/login";
        }

        SecurityUtils.getSubject().login(new StatelessToken(user.getId(), user.getPassword()));
        CookieUtil cookieUtil = new CookieUtil(request, response);
        /* 根据RFC-2109中的规定，在Cookie中只能包含ASCII的编码 */
        cookieUtil.setCookie(Constants.COOKIE_USER_NAME, form.getUsername(), false, 7 * 24 * 3600);
        cookieUtil.setCookie("comment_author", user.getNickName(), "/", false, 365 * 24 * 3600);
        cookieUtil.setCookie("comment_author_email", user.getEmail(), "/", false, 365 * 24 * 3600, false);
        cookieUtil.setCookie("comment_author_url", ServletUtils.getDomain(request), "/", false, 365 * 24 * 3600, false);

        CookieRememberManager.loginSuccess(request, response, user.getId(), user.getPassword(), form.isRemember());

        return "redirect:" + StringUtils.emptyDefault(form.getRedirectURL(), "/backend/index");
    }

}
