package com.ngnis.walle.datasource.mybatis;

import com.ngnis.walle.datasource.mybatis.provider.LogicDeleteProvider;
import com.ngnis.walle.datasource.mybatis.provider.LogicDeleteProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * @author limu.zl
 * @see tk.mybatis.mapper.common.base.delete.DeleteMapper
 */
@RegisterMapper
public interface LogicDeleteMapper<T> {

    /**
     * 根据实体属性作为条件进行删除，查询条件使用等号
     */
    @DeleteProvider(type = LogicDeleteProvider.class, method = "dynamicSQL")
    int delete(T record);

}