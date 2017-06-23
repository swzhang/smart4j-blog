package cn.smart4j.blog.service.freemarker;

/**
 * <p>
 * freemarker静态化异常
 * </p>
 * 
 * @author zhang
 * 
 */
public class FreeMarkerException extends Exception{
  private static final long serialVersionUID = 1L;

  public FreeMarkerException(String msg){
    super(msg);
  }

  public FreeMarkerException(String msg, Throwable t){
    super(msg, t);
  }

}
