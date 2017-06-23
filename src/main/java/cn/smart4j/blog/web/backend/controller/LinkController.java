package cn.smart4j.blog.web.backend.controller;

import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.smart4j.blog.core.dal.entity.Link;
import cn.smart4j.blog.core.plugin.MapContainer;
import cn.smart4j.blog.core.util.IdGenerator;
import cn.smart4j.blog.core.util.StringUtils;
import cn.smart4j.blog.service.LinkService;
import cn.smart4j.blog.web.backend.form.validator.LinkFormValidator;
import cn.smart4j.blog.web.support.WebContextFactory;

@Controller
@RequestMapping("/backend/links")
@RequiresRoles("admin")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        model.addAttribute("page", linkService.list(page, 15));
        return "backend/link/list";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String insert(Link link, Model model) {
        MapContainer form = LinkFormValidator.validateInsert(link);
        if (!form.isEmpty()) {
            model.addAllAttributes(form);
            return "backend/link/edit";
        }

        link.setCreateTime(new Date());
        link.setLastUpdate(link.getCreateTime());
        link.setCreator(WebContextFactory.get().getUser().getNickName());
        link.setId(IdGenerator.uuid19());
        link.setVisible(true);
        linkService.insert(link);
        return "redirect:/backend/links";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(Link link, Model model) {
        MapContainer form = LinkFormValidator.validateUpdate(link);
        if (!form.isEmpty()) {
            model.addAttribute("link", link);
            model.addAttribute("msg", form.get("msg"));
            return "backend/link/edit";
        }

        link.setLastUpdate(new Date());
        linkService.update(link);
        return "redirect:/backend/links";
    }

    @ResponseBody
    @RequestMapping(value = "/{linkid}", method = RequestMethod.DELETE)
    public Object remove(@PathVariable("linkid") String linkid) {
        linkService.deleteById(linkid);
        return new MapContainer("success", true);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(String lid, Model model) {
        if (!StringUtils.isBlank(lid))
            model.addAttribute("link", linkService.loadById(lid));

        return "backend/link/edit";
    }

}
