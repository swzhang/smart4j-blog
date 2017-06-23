package cn.smart4j.blog.core.dal.mapper;

import java.util.List;

import cn.smart4j.blog.core.dal.entity.Tag;

public interface TagMapper extends BaseMapper {

    public int insertBatch(List<Tag> list);

    public int deleteByPostid(String postid);

    public List<String> getTagsByPost(String postid);

}
