package cn.smart4j.blog.service.vo;

import java.util.ArrayList;
import java.util.List;

import cn.smart4j.blog.core.dal.entity.Post;
import cn.smart4j.blog.core.plugin.TreeItem;
import cn.smart4j.blog.core.dal.entity.Comment;

/**
 * 评论业务对象
 *
 * @author zhang
 */
public class CommentVO extends Comment implements TreeItem<CommentVO> {
    private Post post;
    private List<CommentVO> children;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setChildren(List<CommentVO> children) {
        this.children = children;
    }

    @Override
    public List<CommentVO> getChildren() {
        return children;
    }

    @Override
    public void addChild(CommentVO comment) {
        if (children == null)
            setChildren(new ArrayList<CommentVO>());

        getChildren().add(comment);
    }

}
