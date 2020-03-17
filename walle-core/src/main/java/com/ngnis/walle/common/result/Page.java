package com.ngnis.walle.common.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 *     分页结果
 * </p>
 * @author houyi.wh
 * @since 2018-09-16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> implements Serializable {
    private Collection<T> items;
    private Long totalCount;
    private Integer currentPage;
    private Integer pageSize;
}
