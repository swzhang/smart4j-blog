package cn.smart4j.blog.service;

import java.util.List;

import cn.smart4j.blog.core.dal.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smart4j.blog.core.dal.mapper.BaseMapper;
import cn.smart4j.blog.core.dal.mapper.TagMapper;

@Service
public class TagService extends BaseService {
    @Autowired
    private TagMapper tagMapper;

    public List<String> listTagsByPost(String postid) {
        return tagMapper.getTagsByPost(postid);
    }

    /**
     * 插入文章标签记录
     *
     * @param tag
     * @return
     */
    public int insertBatch(List<Tag> tags) {
        return tagMapper.insertBatch(tags);
    }

    public int deleteByPostid(String postid) {
        return tagMapper.deleteByPostid(postid);
    }

    @Override
    protected BaseMapper getMapper() {
        return tagMapper;
    }

}
