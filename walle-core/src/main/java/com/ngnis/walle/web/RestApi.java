package com.ngnis.walle.web;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author houyi.wh
 * @since 2018-11-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestApi {
    private String name;
    private String url;
    private String method;
}
