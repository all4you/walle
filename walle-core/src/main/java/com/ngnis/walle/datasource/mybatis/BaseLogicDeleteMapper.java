package com.ngnis.walle.datasource.mybatis;

import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 参考BaseDeleteMapper的写法
 *
 * @author limu.zl
 * @see tk.mybatis.mapper.common.base.BaseDeleteMapper
 */
@RegisterMapper
public interface BaseLogicDeleteMapper<T> extends
        LogicDeleteMapper<T>,
        LogicDeleteByPrimaryKeyMapper<T> {
}
