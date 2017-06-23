package cn.smart4j.blog.web.front.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;

import cn.smart4j.blog.biz.PostManager;
import cn.smart4j.blog.biz.aop.StaticTemplate;
import cn.smart4j.blog.core.WebConstants;
import cn.smart4j.blog.core.dal.constants.OptionConstants;
import cn.smart4j.blog.core.dal.constants.PostConstants;
import cn.smart4j.blog.core.feed.ArticleAdapter;
import cn.smart4j.blog.core.feed.Channel;
import cn.smart4j.blog.core.feed.RssFeedWriter;
import cn.smart4j.blog.core.util.ServletUtils;
import cn.smart4j.blog.core.util.StringUtils;
import cn.smart4j.blog.service.OptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.smart4j.blog.service.vo.PostVO;

@Controller
public class IndexController{
  @Autowired
  private PostManager postManager;
  @Autowired
  private StaticTemplate staticTemplate;
  @Autowired
  private OptionsService optionsService;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String index(@RequestParam(value = "page", defaultValue = "1") int page, String word, Model model){
    if(!StringUtils.isBlank(word)){
      word = word.trim();
      model.addAttribute("page", postManager.search(word, page));
      model.addAttribute("search", word);
      model.addAttribute(WebConstants.PRE_TITLE_KEY, word);
    }else{
      model.addAttribute("page", postManager.listPost(page, 10));
    }

    return "index";
  }

  @RequestMapping(value = "/feed")
  public void rss(HttpServletRequest request, HttpServletResponse response) throws IOException{
    Channel channel = new Channel();
    channel.setDomain(ServletUtils.getDomain(request));
    channel.setLogoUrl(channel.getDomain() + "/resource/img/logo.png");
    channel.setTitle(optionsService.getOptionValue(OptionConstants.TITLE));
    channel.setDescription(optionsService.getOptionValue(OptionConstants.DESCRIPTION));

    List<Channel.Article> items = new ArrayList<>();
    for(PostVO pvo : postManager.listRecent(10, PostConstants.POST_CREATOR_ALL)){
      items.add(new ArticleAdapter(pvo));
    }
    channel.setItems(items);
    response.setContentType("text/xml");
    try{
      RssFeedWriter.write(channel, response.getOutputStream());
    }catch(XMLStreamException | IOException e){
      throw new IOException(e);
    }
  }

  @RequestMapping(value = "/sitemap", method = RequestMethod.GET)
  public String sitemap(Model model){
    List<PostVO> posts = postManager.listBySitemap();
    model.addAttribute("posts", posts);

    return "sitemap";
  }

  @RequestMapping("/restatic.json")
  public void restatic(HttpServletRequest request){
  }

}
