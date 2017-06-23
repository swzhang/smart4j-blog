package cn.smart4j.blog.web.backend.form.validator;

import cn.smart4j.blog.core.util.StringUtils;
import cn.smart4j.blog.web.backend.form.MailOption;
import cn.smart4j.blog.core.plugin.MapContainer;

public class MailFormValidator{

  public static MapContainer validate(MailOption form){
    MapContainer result = new MapContainer();
    if(StringUtils.isBlank(form.getHost())){
      result.put("host", "请输入host");
    }

    if(form.getPort() < 10){
      result.put("port", "请输入合法端口号");
    }

    if(StringUtils.isBlank(form.getUsername())){
      result.put("username", "请输入用户名");
    }

    if(StringUtils.isBlank(form.getPassword())){
      result.put("password", "请输入密码");
    }

    return result;
  }
}
