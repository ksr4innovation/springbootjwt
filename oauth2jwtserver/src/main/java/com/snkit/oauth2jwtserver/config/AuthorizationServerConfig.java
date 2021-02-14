package com.snkit.oauth2jwtserver.config;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPublicKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig  extends AuthorizationServerConfigurerAdapter {

	


	
	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	
	@Autowired
    private ClientDetailsService clientDetailsService;
	
	@Value("${jksfilepath}")
	private String jksfilepath;
	
	static final String CLIEN_ID = "snkitclient";
	static final String CLIENT_SECRET = "snkitclientdemo";
	static final String GRANT_TYPE = "password";
	static final String AUTHORIZATION_CODE = "authorization_code";
	static final String REFRESH_TOKEN = "refresh_token";
	static final String IMPLICIT = "implicit";
	static final String SCOPE_READ = "read";
	static final String SCOPE_WRITE = "write";
	static final String TRUST = "trust";
	static final String profile = "profile";
	static final String openid="openid";
	static final int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 60;
	static final int FREFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 60;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		

		clients.inMemory().withClient(CLIEN_ID).secret("{noop}"+CLIENT_SECRET)
				.authorizedGrantTypes(AUTHORIZATION_CODE, IMPLICIT, REFRESH_TOKEN,"client_credentials", "password")
				.scopes(SCOPE_READ, SCOPE_WRITE, TRUST).accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
				.refreshTokenValiditySeconds(FREFRESH_TOKEN_VALIDITY_SECONDS);
	}
	
	

	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		endpoints
		.authenticationManager(authenticationManager);
		endpoints.tokenStore(tokenStore()).tokenEnhancer(jwtAccessTokenConverter());
		endpoints.requestFactory(requestFactory());
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter ();
		try {
		String fileName = "classpath:oauthdemo.jks";
		
		
		
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream inputStream = cl.getResourceAsStream(fileName);
		 KeyStore store = KeyStore.getInstance("jks");
		 store.load(inputStream, "password".toCharArray());
		 
			RSAPrivateCrtKey key = (RSAPrivateCrtKey) store.getKey("oauthdemo", "password".toCharArray());
			RSAPublicKeySpec spec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
			PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(spec);
			
			
		converter.setKeyPair( new KeyPair(publicKey, key));
		}catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return converter;
	}
	
	
	
	
	@Bean
	public OAuth2RequestFactory requestFactory() {
		CustomOauth2RequestFactory requestFactory = new CustomOauth2RequestFactory(clientDetailsService);
		requestFactory.setCheckUserScopes(false);
		return requestFactory;
	}
	

	@Bean
	public TokenEndpointAuthenticationFilter tokenEndpointAuthenticationFilter() {
		return new TokenEndpointAuthenticationFilter(authenticationManager, requestFactory());
	}
	
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("permitAll()");
	}
	

     
	
}
