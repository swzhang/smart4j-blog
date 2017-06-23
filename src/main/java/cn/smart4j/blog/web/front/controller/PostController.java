package cn.smart4j.blog.web.front.controller;

import javax.servlet.http.HttpServletRequest;

import cn.smart4j.blog.biz.CommentManager;
import cn.smart4j.blog.biz.PostManager;
import cn.smart4j.blog.biz.VisitStatManager;
import cn.smart4j.blog.core.WebConstants;
import cn.smart4j.blog.service.vo.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.smart4j.blog.core.util.CookieUtil;
import cn.smart4j.blog.service.PostService;

@Controller
@RequestMapping("/posts")
public class PostController{
  @Autowired
  private PostManager postManager;
  @Autowired
  private PostService postService;
  @Autowired
  private CommentManager commentManager;
  @Autowired
  private VisitStatManager visitStatManager;

  @RequestMapping(value = "/{postid}", method = RequestMethod.GET)
  public String post(@PathVariable("postid") String id, HttpServletRequest request, Model model){
    PostVO post = postManager.loadReadById(id);
    if(post != null){
      visitStatManager.record(id);
      model.addAttribute(WebConstants.PRE_TITLE_KEY, post.getTitle());
      model.addAttribute("post", post);
      model.addAttribute("comments",
          commentManager.getAsTree(id, new CookieUtil(request, null).getCookie("comment_author")));
      /* 上一篇，下一篇 */
      model.addAttribute("next", postService.getNextPost(id));
      model.addAttribute("prev", postService.getPrevPost(id));
    }

    return post != null ? "post" : "404";
  }

}
