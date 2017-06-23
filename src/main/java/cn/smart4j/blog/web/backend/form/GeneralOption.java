package cn.smart4j.blog.web.backend.form;

/**
 * 默认设置表单
 * 
 * @author zhang
 * 
 */
public class GeneralOption{
  private String title;
  private String subtitle;
  private String description;
  private String keywords;
  private String webUrl;

  public String getTitle(){
    return title;
  }

  public void setTitle(String title){
    this.title = title;
  }

  public String getSubtitle(){
    return subtitle;
  }

  public void setSubtitle(String subtitle){
    this.subtitle = subtitle;
  }

  public String getDescription(){
    return description;
  }

  public void setDescription(String description){
    this.description = description;
  }

  public String getKeywords(){
    return keywords;
  }

  public void setKeywords(String keywords){
    this.keywords = keywords;
  }

  public String getWebUrl() {
    return webUrl;
  }

  public void setWebUrl(String webUrl) {
    this.webUrl = webUrl;
  }
}
