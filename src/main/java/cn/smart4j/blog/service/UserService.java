package cn.smart4j.blog.service;


import cn.smart4j.blog.core.dal.entity.User;
import cn.smart4j.blog.core.dal.mapper.BaseMapper;
import cn.smart4j.blog.core.dal.mapper.UserMapper;
import cn.smart4j.blog.core.plugin.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService {
    @Autowired
    private UserMapper userMapper;

    public PageModel<User> list(int pageIndex, int pageSize) {
        PageModel<User> page = new PageModel<>(pageIndex, pageSize);
        super.list(page);
        return page;
    }

    public User login(String username, String password) {
        return userMapper.loadByNameAndPass(username, password);
    }

    @Override
    protected BaseMapper getMapper() {
        return userMapper;
    }

}
