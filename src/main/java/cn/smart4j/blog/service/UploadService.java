package cn.smart4j.blog.service;

import java.util.List;

import cn.smart4j.blog.core.dal.entity.Upload;
import cn.smart4j.blog.core.dal.mapper.BaseMapper;
import cn.smart4j.blog.core.plugin.PageModel;
import cn.smart4j.blog.service.vo.UploadVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.smart4j.blog.core.dal.mapper.UploadMapper;

@Service
public class UploadService extends BaseService {
    @Autowired
    private UploadMapper uploadMapper;

    public PageModel<UploadVO> list(int pageIndex, int pageSize) {
        PageModel<UploadVO> pageModel = new PageModel<>(pageIndex, pageSize);
        list(pageModel);

        return pageModel;
    }

    public List<Upload> listByPostid(String postid) {
        return uploadMapper.listByPostid(postid);
    }

    public void updatePostid(String postid, List<String> imgpaths) {
        uploadMapper.updatePostid(postid, imgpaths);
    }

    /**
     * 将所有postid的记录置空,非删除记录
     *
     * @param postid
     */
    public void setNullPostid(String postid) {
        uploadMapper.setNullPostid(postid);
    }

    public void deleteByPostid(String postid) {
        uploadMapper.deleteByPostid(postid);
    }

    @Override
    protected BaseMapper getMapper() {
        return uploadMapper;
    }

}
