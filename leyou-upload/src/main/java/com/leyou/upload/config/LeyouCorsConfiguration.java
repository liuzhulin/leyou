package com.leyou.upload.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class LeyouCorsConfiguration {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //允许跨域的域名，如果要携带cookie，不能写*。*：代表所有域名都可以跨域访问
        corsConfiguration.addAllowedOrigin("http://manage.leyou.com");
        //允许携带cookie
        corsConfiguration.setAllowCredentials(true);
        //允许所有请求方法
        corsConfiguration.addAllowedMethod("*");
        //允许携带任何头信息
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}
