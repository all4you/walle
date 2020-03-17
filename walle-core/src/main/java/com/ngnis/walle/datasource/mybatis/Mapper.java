package com.ngnis.walle.datasource.mybatis;

import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.base.BaseInsertMapper;
import tk.mybatis.mapper.common.base.BaseSelectMapper;
import tk.mybatis.mapper.common.base.BaseUpdateMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * 通用Mapper接口,其他接口继承该接口即可
 *
 * @param <T> 不能为空
 * @author limu.zl
 * @see tk.mybatis.mapper.common.Mapper
 */
@RegisterMapper
public interface Mapper<T> extends
        BaseSelectMapper<T>,
        BaseInsertMapper<T>,
        BaseUpdateMapper<T>,
        BaseLogicDeleteMapper<T>,
        InsertListMapper<T>,
        ExampleMapper<T>
{
}
