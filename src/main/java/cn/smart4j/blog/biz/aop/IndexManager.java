package cn.smart4j.blog.biz.aop;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.smart4j.blog.core.dal.constants.PostConstants;
import cn.smart4j.blog.core.dal.entity.Post;
import cn.smart4j.blog.core.lucene.LuceneUtils;
import cn.smart4j.blog.core.lucene.QueryBuilder;
import cn.smart4j.blog.core.lucene.SearchEnginer;
import cn.smart4j.blog.core.plugin.MapContainer;
import cn.smart4j.blog.core.plugin.PageModel;
import cn.smart4j.blog.core.util.JsoupUtils;

/**
 * 文章Lucene索引管理器
 *
 * @author zhang
 */
@Component
public class IndexManager {
    private static final Logger logger = LoggerFactory.getLogger(IndexManager.class);

    /**
     * 只有添加文章才插入Lucene索引
     *
     * @param post
     */
    public void insert(Post post) {
        if (PostConstants.TYPE_POST.equals(post.getType())) {
            logger.debug("add post index -->" + post.getTitle());
            SearchEnginer.postEnginer().insert(convert(post));
        }
    }

    /**
     * 只有更新文章才更新Lucene索引
     *
     * @param post
     * @param affect
     */
    public void update(Post post, boolean affect) {
        if (PostConstants.TYPE_POST.equals(post.getType()) && affect) {
            SearchEnginer.postEnginer().update(new Term("id", post.getId()), convert(post));
        }
    }

    public void remove(String postid, String postType) {
        if (PostConstants.TYPE_POST.equals(postType))
            SearchEnginer.postEnginer().delete(new Term("id", postid));
    }

    public PageModel<MapContainer> search(String word, int pageIndex) {
        PageModel<MapContainer> result = new PageModel<>(pageIndex, 15);
        QueryBuilder builder = new QueryBuilder(SearchEnginer.postEnginer().getAnalyzer());
        builder.addShould("title", word).addShould("excerpt", word);
        builder.addLighter("title", "excerpt");
        SearchEnginer.postEnginer().searchHighlight(builder, result);

        return result;
    }

    private Document convert(Post post) {
        Document doc = new Document();
        doc.add(new Field("id", post.getId() + "", LuceneUtils.directType()));
        doc.add(new Field("title", post.getTitle(), LuceneUtils.searchType()));
    /* 用jsoup剔除html标签 */
        doc.add(new Field("excerpt", JsoupUtils.plainText(post.getContent()), LuceneUtils.searchType()));
        // doc.add(new LongField("createTime", post.getCreateTime().getTime(),
        // LuceneUtils.storeType()));
        return doc;
    }

}
