package com.ngnis.walle.common.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *     分页查询对象
 * </p>
 * @author houyi.wh
 * @since 2018-09-16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO implements Serializable {
    public static final int MAX_ROWS_PER_PAGE = 100;
    public static final int DEFAULT_ROWS_PER_PAGE = 10;
    /**
     * 页码
     */
    private Integer pageNo = 1;

    /**
     * 单页显示多少条
     */
    private Integer pageSize = DEFAULT_ROWS_PER_PAGE;

    /**
     * 最多显示100条
     *
     * @return 单页显示多少条
     */
    public Integer getPageSize() {
        if (this.pageSize > MAX_ROWS_PER_PAGE) {
            this.pageSize = MAX_ROWS_PER_PAGE;
        } else if (this.pageSize <= 0) {
            this.pageSize = DEFAULT_ROWS_PER_PAGE;
        }
        return this.pageSize;
    }
}