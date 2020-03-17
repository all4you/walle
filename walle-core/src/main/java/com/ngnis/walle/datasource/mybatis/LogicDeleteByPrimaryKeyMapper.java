package com.ngnis.walle.datasource.mybatis;

import com.ngnis.walle.datasource.mybatis.provider.LogicDeleteProvider;
import com.ngnis.walle.datasource.mybatis.provider.LogicDeleteProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 通用Mapper接口,删除
 *
 * @param <T> 不能为空
 * @author limu.zl
 * @see tk.mybatis.mapper.common.base.delete.DeleteByPrimaryKeyMapper
 */
@RegisterMapper
public interface LogicDeleteByPrimaryKeyMapper<T> {

    /**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     */
    @DeleteProvider(type = LogicDeleteProvider.class, method = "dynamicSQL")
    int deleteByPrimaryKey(Object key);

}
