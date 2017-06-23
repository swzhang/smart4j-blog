package cn.smart4j.blog.core.dal.mapper;

public interface OptionMapper extends BaseMapper {

    String getOptionValue(String name);

    /**
     * 以select .. for update,注意此方法须在事务中执行
     *
     * @param name
     * @return
     */
    String getOptionValueForUpdate(String name);

}
