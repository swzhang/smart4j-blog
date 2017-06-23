package cn.smart4j.blog.service;

import java.util.List;

import cn.smart4j.blog.core.dal.constants.CategoryConstants;
import cn.smart4j.blog.core.dal.mapper.BaseMapper;
import cn.smart4j.blog.core.dal.mapper.PostMapper;
import cn.smart4j.blog.core.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.smart4j.blog.core.dal.entity.Category;
import cn.smart4j.blog.core.dal.mapper.CategoryMapper;
import cn.smart4j.blog.core.util.IdGenerator;

@Service
public class CategoryService extends BaseService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private PostMapper postMapper;

    @Transactional
    public boolean insertChildren(Category category, String parentName) {
        Category parent = loadByName(StringUtils.isBlank(parentName) ? CategoryConstants.ROOT : parentName);
        category.setLeftv(parent.getRightv());
        category.setRightv(parent.getRightv() + 1);

        categoryMapper.updateInsertLeftv(parent.getRightv());
        categoryMapper.updateInsertRightv(parent.getRightv());
        insert(category);

        return true;
    }

    @Transactional
    public boolean insertAfter(Category category, Category sbling) {
        category.setLeftv(sbling.getRightv() + 1);
        category.setRightv(sbling.getRightv() + 2);

        categoryMapper.updateInsertLeftv(sbling.getRightv());
        categoryMapper.updateInsertRightv(sbling.getRightv());
        insert(category);

        return true;
    }

    /**
     * 此方法只被CategoryManager调用
     *
     * @param category
     * @return
     */
    @Transactional
    public void remove(Category category) {
        int length = category.getRightv() - category.getLeftv() + 1;
    /* 注意:delete须第一个执行,因为updateDeleteLeftv会有影响 */
        categoryMapper.delete(category.getLeftv(), category.getRightv());
        categoryMapper.updateDeleteLeftv(category.getLeftv(), length);
        categoryMapper.updateDeleteRightv(category.getRightv(), length);
    }

    /**
     * 获取指定分类的子分类
     *
     * @param category
     * @return
     */
    public List<Category> loadChildren(Category category) {
        return categoryMapper.loadChildren(category);
    }

    public void init() {
        Category root = new Category();
        root.setId(IdGenerator.uuid19());
        root.setLeftv(1);
        root.setName(CategoryConstants.ROOT);
        root.setRightv(2);
        insert(root);
    }

    public Category loadByName(String name) {
        return categoryMapper.loadByName(name);
    }

    @Override
    protected BaseMapper getMapper() {
        return categoryMapper;
    }

}
