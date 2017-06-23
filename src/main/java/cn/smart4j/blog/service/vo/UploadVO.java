package cn.smart4j.blog.service.vo;

import cn.smart4j.blog.core.dal.entity.Post;
import cn.smart4j.blog.core.dal.entity.Upload;
import cn.smart4j.blog.core.dal.entity.User;

/**
 * 附件业务对象
 *
 * @author zhang
 */
public class UploadVO extends Upload {
    private Post post;
    private User user;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
