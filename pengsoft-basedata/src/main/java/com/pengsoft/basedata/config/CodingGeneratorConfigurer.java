package com.pengsoft.basedata.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Optical character recognition service auto configure.
 *
 * @author peng.dang@pengsoft.com
 * @since 1.0.0
 */
@Configuration
@ComponentScan("com.pengsoft.*.generator")
public class CodingGeneratorConfigurer {

}
