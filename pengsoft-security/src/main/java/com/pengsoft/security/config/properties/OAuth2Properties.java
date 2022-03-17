package com.pengsoft.security.config.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * OAuth2 configuration properties
 */
@Getter
@Setter
@ConfigurationProperties("pengsoft.security.oauth2")
public class OAuth2Properties {

    private OAuth2AuthorizationServer authorizationServer;

    private OAuth2ResourceServer resourceServer;

    private List<OAuth2Client> clients = new ArrayList<>();

    @Getter
    @Setter
    public static class OAuth2AuthorizationServer {

        private boolean enabled;

        private String endpoint;

    }

    @Getter
    @Setter
    public static class OAuth2ResourceServer {

        private boolean enabled;

        private String endpoint;

    }

    @Getter
    @Setter
    public static class OAuth2Client {

        private String id;

        private String secret;

        private String scope = "all";

        private int accessTokenValiditySeconds = 60 * 60 * 8;

        private List<String> grantTypes = List.of();

    }

}
