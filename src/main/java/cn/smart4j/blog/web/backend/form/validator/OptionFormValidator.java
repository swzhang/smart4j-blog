package cn.smart4j.blog.web.backend.form.validator;

import cn.smart4j.blog.core.plugin.MapContainer;
import cn.smart4j.blog.core.util.StringUtils;
import cn.smart4j.blog.web.backend.form.GeneralOption;
import cn.smart4j.blog.web.backend.form.PostOption;

public class OptionFormValidator{

  public static MapContainer validateGeneral(GeneralOption option){
    MapContainer form = new MapContainer();
    if(StringUtils.isBlank(option.getTitle())){
      form.put("title", "需填写站点名称名称");
    }
    if(StringUtils.isBlank(option.getSubtitle())){
      form.put("subtitle", "需填写副标题");
    }
    if(StringUtils.isBlank(option.getDescription())){
      form.put("description", "需填写站点描述");
    }
    if(StringUtils.isBlank(option.getKeywords())){
      form.put("keywords", "需填写站点关键字");
    }
    if(StringUtils.isBlank(option.getWebUrl())){
      form.put("webUrl", "需填写网站url");
    }

    return form;
  }

  public static MapContainer validatePost(PostOption option){
    MapContainer form = new MapContainer();
    if(option.getMaxShow() < 1){
      form.put("maxShow", "格式错误");
    }

    if(StringUtils.isBlank(option.getDefaultCategory())){
      form.put("defaultCategory", "默认分类格式错误");
    }

    return form;
  }

}
