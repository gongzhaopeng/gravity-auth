package cn.benbenedu.gravity.auth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class WebSecurityConfigurer
        extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;

    public WebSecurityConfigurer(
            UserDetailsService userDetailsService) {

        this.userDetailsService = userDetailsService;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean()
            throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {

        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage("http://sundial.benbenedu.cn:9001/xauth/login");

        http.formLogin()
                .loginProcessingUrl("/login");

        final var successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy(new DefaultRedirectStrategy() {

            @Override
            public void sendRedirect(
                    HttpServletRequest request, HttpServletResponse response, String url)
                    throws IOException {

                final var convertedUrl = convertRedirectUrlForGateway(request, url);

                super.sendRedirect(request, response, convertedUrl);
            }

            private String convertRedirectUrlForGateway(
                    HttpServletRequest request, String url) {

                if (UrlUtils.isAbsoluteUrl(url)) {

                    final var xForwardedProto = request.getHeader("x-forwarded-proto");
                    final var xForwardedHost = request.getHeader("x-forwarded-host");
                    final var xForwardedPrefix = request.getHeader("x-forwarded-prefix");

                    if (StringUtils.hasLength(xForwardedProto) &&
                            StringUtils.hasLength(xForwardedHost) &&
                            StringUtils.hasLength(xForwardedPrefix)) {

                        return xForwardedProto + "://" + xForwardedHost + xForwardedPrefix +
                                url.substring(url.indexOf("/", url.indexOf("://") + 3));
                    }
                }

                return url;
            }
        });
        http.formLogin().successHandler(successHandler);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
