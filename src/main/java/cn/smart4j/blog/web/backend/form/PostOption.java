package cn.smart4j.blog.web.backend.form;

/**
 * 文章设置表单
 * 
 * @author zhang
 * 
 */
public class PostOption{
  /**
   * 博客页面至多显示数
   */
  private int maxShow;
  /**
   * 允许文章评论
   */
  private boolean allowComment;
  /**
   * 默认文章分类
   */
  private String defaultCategory;

  public int getMaxShow() {
    return maxShow;
  }

  public void setMaxShow(int maxShow) {
    this.maxShow = maxShow;
  }

  public boolean isAllowComment(){
    return allowComment;
  }

  public void setAllowComment(boolean allowComment){
    this.allowComment= allowComment;
  }

  public String getDefaultCategory(){
    return defaultCategory;
  }

  public void setDefaultCategory(String defaultCategory){
    this.defaultCategory = defaultCategory;
  }

}
