package cn.smart4j.blog.service.vo;

import java.util.ArrayList;
import java.util.List;

import cn.smart4j.blog.core.dal.entity.Post;
import cn.smart4j.blog.core.plugin.TreeItem;

/**
 * 页面业务对象
 *
 * @author zhang
 */
public class PageVO extends Post implements TreeItem<PageVO> {
    private List<PageVO> children;

    public void setChildren(List<PageVO> children) {
        this.children = children;
    }

    @Override
    public List<PageVO> getChildren() {
        return children;
    }

    @Override
    public void addChild(PageVO comment) {
        if (children == null)
            setChildren(new ArrayList<PageVO>());

        getChildren().add(comment);
    }
}
