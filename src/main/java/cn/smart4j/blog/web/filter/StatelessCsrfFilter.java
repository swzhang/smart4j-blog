package cn.smart4j.blog.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.smart4j.blog.core.Constants;
import cn.smart4j.blog.core.util.ServletUtils;
import cn.smart4j.blog.core.util.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import cn.smart4j.blog.core.util.CookieUtil;

/**
 * 处理csrf攻击,Stateless CSRF方案,由客户端产生crsf的cookie和请求参数
 * 
 * @author zhou
 * 
 */
public class StatelessCsrfFilter extends OncePerRequestFilter{
  private List<String> excludes = new ArrayList<>();
  static List<String> METHODS = Arrays.asList("POST", "DELETE", "PUT", "PATCH");
  private PathMatcher matcher = new AntPathMatcher();

  @Override
  protected void initFilterBean() throws ServletException{
    FilterConfig config = getFilterConfig();
    String paths = config.getInitParameter("exclude");
    if(!StringUtils.isBlank(paths)){
      excludes.addAll(Arrays.asList(paths.split(",")));
    }
  }

  private boolean isAjaxVerificationToken(HttpServletRequest request, String csrfToken){
    String headToken = request.getHeader(Constants.CSRF_TOKEN);

    return headToken != null && headToken.equals(csrfToken);
  }

  /**
   * 校验非ajax提交
   * <p>
   * 由于有可能为multipart提交所以须在该filter前做parseMultipart,否则request.getParameter会获取不到值
   * </p>
   * 
   * @param request
   * @param csrfToken
   * @return
   */
  private boolean isVerificationToken(HttpServletRequest request, String csrfToken){
    String paramToken = request.getParameter(Constants.CSRF_TOKEN);

    if(StringUtils.isBlank(paramToken))
      return false;

    return paramToken.equals(csrfToken);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException{
    String url = request.getRequestURI();
    for(String match : excludes){
      if(matcher.match(match, url)){
        filterChain.doFilter(request, response);
        return;
      }
    }

    if(METHODS.contains(request.getMethod())){
      boolean ajax = ServletUtils.isAjax(request);
      CookieUtil cookieUtil = new CookieUtil(request, response);
      String csrfToken = cookieUtil.getCookie(Constants.COOKIE_CSRF_TOKEN, false);
      if(ajax && !isAjaxVerificationToken(request, csrfToken)){
        response.setContentType("application/json");
        response.setCharacterEncoding(Constants.ENCODING_UTF_8.name());
        response.getWriter().write("{'status':'403','success':false,'msg':'非法请求,请刷新重试'}");
        return;
      }else if(!ajax && !isVerificationToken(request, csrfToken)){
        // if(response.isCommitted())
        //这个错误没有给出明确错误信息，导致开发者不便追踪问题，一直以为是spring 参数解析问题，当然tomcat等容器没有设置项目名访问是没有问题的
        response.sendError(HttpServletResponse.SC_BAD_REQUEST,"非法请求，请刷新重试");
        return;
      }
    }

    filterChain.doFilter(request, response);
  }

}
