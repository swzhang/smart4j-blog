package cn.smart4j.blog.service;

import cn.smart4j.blog.core.dal.entity.Link;
import cn.smart4j.blog.core.dal.mapper.BaseMapper;
import cn.smart4j.blog.core.dal.mapper.LinkMapper;
import cn.smart4j.blog.core.plugin.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkService extends BaseService {
    @Autowired
    private LinkMapper linkMapper;

    public PageModel<Link> list(int pageIndex, int pageSize) {
        PageModel<Link> pageModel = new PageModel<>(pageIndex, pageSize);
        list(pageModel);

        return pageModel;
    }

    @Override
    protected BaseMapper getMapper() {
        return linkMapper;
    }

}
