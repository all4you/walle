package com.ngnis.walle.datasource.mybatis.provider;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.List;
import java.util.Set;

/**
 * 通过 ids 字符串的各种操作
 * <p/>
 * ids 如 "1,2,3"
 *
 * @author limu.zl
 * @see tk.mybatis.mapper.additional.idlist.IdListProvider
 */
public class LogicDeleteIdListProvider extends MapperTemplate {

    public LogicDeleteIdListProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 保证 idList 不能为空
     *
     * @param list
     * @param errorMsg
     */
    public static void notEmpty(List<?> list, String errorMsg) {
        if (list == null || list.size() == 0) {
            throw new MapperException(errorMsg);
        }
    }

    /**
     * 根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段
     *
     * @param ms
     * @return
     */
    public String deleteByIdList(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)))
                .append("<set>is_deleted = 1</set>");
        this.appendWhereIdList(sql, entityClass, getConfig().isSafeDelete());
        return sql.toString();
    }

    /**
     * 拼接条件
     *
     * @param sql
     * @param entityClass
     */
    private void appendWhereIdList(StringBuilder sql, Class<?> entityClass, boolean notEmpty) {
        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        if (columnList.size() == 1) {
            EntityColumn column = columnList.iterator().next();
            if (notEmpty) {
                sql.append("<bind name=\"notEmptyListCheck\" value=\"@com.ngnis.walle.datasource.mybatis.provider.LogicDeleteIdListProvider@notEmpty(");
                sql.append("idList, 'idList 不能为空')\"/>");
            }
            sql.append("<where>");
            sql.append("<foreach collection=\"idList\" item=\"id\" separator=\",\" open=\"");
            sql.append(column.getColumn());
            sql.append(" in ");
            sql.append("(\" close=\")\">");
            sql.append("#{id}");
            sql.append("</foreach>");
            sql.append("</where>");
        } else {
            throw new MapperException("继承 ByIdList 方法的实体类[" + entityClass.getCanonicalName() + "]中必须只有一个带有 @Id 注解的字段");
        }
    }
}
