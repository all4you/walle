package com.ngnis.walle.datasource.mybatis;

import com.ngnis.walle.datasource.mybatis.provider.LogicDeleteIdListProvider;
import com.ngnis.walle.datasource.mybatis.provider.LogicDeleteIdListProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通用Mapper接口,根据idList逻辑删除
 *
 * @param <T> 不能为空
 * @author limu.zl
 * @see tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface LogicDeleteByIdListMapper<T, PK> {

    /**
     * 根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段
     */
    @DeleteProvider(type = LogicDeleteIdListProvider.class, method = "dynamicSQL")
    int deleteByIdList(@Param("idList") List<PK> idList);

}