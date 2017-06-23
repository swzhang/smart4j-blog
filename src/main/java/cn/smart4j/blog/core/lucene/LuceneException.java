package cn.smart4j.blog.core.lucene;

import org.springframework.dao.DataAccessException;

/**
 * Lucene索引操作异常，此处继承spring的dao异常
 *
 * @author zhang
 */
public class LuceneException extends DataAccessException {

    private static final long serialVersionUID = 1L;

    public LuceneException(String msg) {
        super(msg);
    }

    public LuceneException(String msg, Throwable t) {
        super(msg, t);
    }

}