package com.ngnis.walle.datasource.mybatis.provider;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * @author limu.zl
 */
public class LogicDeleteProvider extends MapperTemplate {
    public LogicDeleteProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 通过条件删除
     *
     * @param ms
     * @return
     */
    public String delete(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        //如果设置了安全删除，就不允许执行不带查询条件的 delete 方法
        if (getConfig().isSafeDelete()) {
            sql.append(SqlHelper.notAllNullParameterCheck("_parameter", EntityHelper.getColumns(entityClass)));
        }
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)))
                .append("<set>is_deleted = 1</set>")
                .append(SqlHelper.whereAllIfColumns(entityClass, isNotEmpty(), true));
        return sql.toString();
    }

    /**
     * 通过主键删除
     *
     * @param ms
     */
    public String deleteByPrimaryKey(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        // 逻辑删除
        return SqlHelper.updateTable(entityClass, tableName(entityClass)) +
                "<set>is_deleted = 1</set>" +
                //增加 @Version 乐观锁支持
                SqlHelper.wherePKColumns(entityClass, true);
    }


}
