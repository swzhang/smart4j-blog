package cn.smart4j.blog.core.tag;

import javax.servlet.jsp.JspException;

public class FirstTag extends AbstartTagSupport {
    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
        setPageAttribute(1);
        return EVAL_BODY_INCLUDE;
    }

}
