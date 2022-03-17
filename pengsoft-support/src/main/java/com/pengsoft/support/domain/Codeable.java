package com.pengsoft.support.domain;

/**
 * Codable
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
public interface Codeable extends Entity<String> {

    String getCode();

    void setCode(String code);

}
