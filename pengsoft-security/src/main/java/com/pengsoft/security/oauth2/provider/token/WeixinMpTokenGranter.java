package com.pengsoft.security.oauth2.provider.token;

import java.util.LinkedHashMap;

import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

public class WeixinMpTokenGranter extends AbstractTokenGranter {

    public static final String GRANT_TYPE = "weixin_mp";

    private static final String PARAM_NAME = "weixin_mp_open_id";

    private UserDetailsService userDetailsService;

    public WeixinMpTokenGranter(final UserDetailsService userDetailsService,
            final AuthorizationServerTokenServices tokenServices, final ClientDetailsService clientDetailsService,
            final OAuth2RequestFactory requestFactory) {
        this(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.userDetailsService = userDetailsService;
    }

    protected WeixinMpTokenGranter(final AuthorizationServerTokenServices tokenServices,
            final ClientDetailsService clientDetailsService, final OAuth2RequestFactory requestFactory,
            final String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(final ClientDetails client,
            final TokenRequest tokenRequest) {
        final var storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        final var parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        final var weixinMpOpenId = parameters.get(PARAM_NAME);
        final var userDetails = userDetailsService.loadUserByUsername(weixinMpOpenId);
        final var authentication = new RememberMeAuthenticationToken(userDetails.getUsername(), userDetails,
                userDetails.getAuthorities());
        return new OAuth2Authentication(storedOAuth2Request, authentication);
    }

}
