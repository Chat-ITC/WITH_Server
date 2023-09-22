package codingFriends_Server.global.config;

import codingFriends_Server.global.auth.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .logout()
                .disable()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                // permitAll -> 인증과 인가를 거치지 않고 모든 사용자에게 접근 권한을 부여한다.
                .antMatchers("/kakao/callback", "/naver/callback", "/member/signup",
                        "/hello","/member/refreshToken", "/language/**", "/question/**","/api/swagger-ui/**","/member/delete/**","/ai/summary/delete/**").permitAll()
                //permitAll로 적용된 URL 이외의 모든 요청은 인증된 사용자만이 접근할 수 있게 설정됩니다.
                .anyRequest().authenticated();
        /*
         perMitAll()은 인증을 거치지 않고, 이미 인증된 사용자로 처리 후, 이미 인증된 사용자로 처리했기 때문에 인증 필터를 거치지 않고 인가 처리를 해준다.
         인가 처리 -> 권한이 있다고 확인된 것.
         */
        return http.build();
    }
}
