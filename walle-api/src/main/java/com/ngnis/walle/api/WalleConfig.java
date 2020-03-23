package com.ngnis.walle.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author houyi
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalleConfig {

    /**
     * http://domain.com
     */
    private String endPoint;

    private String accessKey;

    private String secretKey;

}
