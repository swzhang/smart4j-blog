package cn.smart4j.blog.web.backend.form.validator;

import cn.smart4j.blog.core.util.StringUtils;
import cn.smart4j.blog.core.dal.entity.Category;
import cn.smart4j.blog.core.plugin.MapContainer;

public class CategoryFormValidator{

  public static MapContainer validateInsert(Category category){
    MapContainer form = new MapContainer();
    if(StringUtils.isBlank(category.getName())){
      form.put("msg", "分类名称不能为空");
    }

    return form;
  }

}
