package shop.mtcoding.securityapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. CSRF 해제
        http.csrf().disable();

        // 2. Form 로그인 설정
        http.formLogin(login -> login
                .loginPage("/loginForm")
                .usernameParameter("username") // 커스텀마이징 가능
                .passwordParameter("password") // 커스텀마이징 가능
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .successHandler((req, resp, authentication) -> {
                    System.out.println("디버그 : 로그인이 완료되었습니다"); // 본 코드에서는 무조건 디버그 붙이자
                })
                .failureHandler((req, resp, ex) -> {
                    System.out.println("디버그 : 로그인 실패" + ex.getMessage()); // 본 코드에서는 무조건 디버그 붙이자
                }));

        // 3. 인증, 권한 필터 설정
        http.authorizeRequests(
                (authorize) -> authorize.antMatchers("/users/**").authenticated()
                        .antMatchers("/manager/**").access("hasRole('ADMIN') or hasRole('MANAGER')")
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll());

        return http.build();
    }
}