package com.example.dorundorun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //패스워드 암호화 처리용(해싱)
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){

        return new BCryptPasswordEncoder();
    }

    //계층 설정
    @Bean
    public RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();

        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        return hierarchy;
    }

    //시큐리티 설정가능
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        //경로별 접근 권한설정(인가기능)
        http
                .authorizeHttpRequests((auth)->auth
                                .antMatchers("/", "/member/findPW", "/member/findId", "/js/**" , "/css/**", "/img/**", "/member/login", "/member/join", "/member/idCheck", "/member/nicknameCheck", "/sms/**").permitAll()
                                .antMatchers("/admin").hasRole("ADMIN")
                                .antMatchers("/member/**", "/place/**", "/board/**", "/crew/**", "/crewBoard/**", "/marathon/**", "/board_comment/**", "/chat/**", "/crewBoard_comment").hasAnyRole("USER","ADMIN")
                                .anyRequest().authenticated()
//                        .antMatchers("/", "/member/login", "/member/join", "/member/idCheck", "/member/nicknameCheck","/css/**", "/img/**").permitAll()
//                        .antMatchers("/admin").hasRole("ADMIN")
//                        .antMatchers("/member/**", "/board/**", "/crew/**", "/crewBoard/**", "/marathon/**").hasAnyRole("USER","ADMIN")
//                        .antMatchers("/css/**", "/js/**", "/images/**").permitAll()
//                        .anyRequest().authenticated()
//                        .and()
                );

        //로그인 폼 작업
        http
                //권한없이 get요청으로 접근시에 로그인 페이지로 보내줌
                .formLogin((auth)->auth.loginPage("/member/login")
                        //폼 태그에서 login경로로 가는것을 시큐리티가 받아서 로그인 처리 진행
                        .loginProcessingUrl("/member/loginProc")
                        .permitAll()
                        .defaultSuccessUrl("/member/main", true)
                );

        //csrf 작업 개발중에는 disable
        http
                .csrf((csrf)->csrf.disable());

        //다중로그인 작업(브라우저별 1개로 설정할거
        http
                .sessionManagement((auth)->auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true));

        //세션 고정보호(해커가 계정탈취하려고 할때 세션 id 바꿔버리는거)
        http
                .sessionManagement((auth)->auth
                        .sessionFixation().changeSessionId());

        //로그아웃(스프링 시큐리티)
        http
                .logout()
                .logoutUrl("/logout") // 로그아웃 URL 설정
                .logoutSuccessUrl("/?logout") // 로그아웃 성공 시 이동할 URL 설정
                .invalidateHttpSession(true) // 세션 무효화 설정
                .deleteCookies("JSESSIONID") // 쿠키 삭제 설정
                .permitAll();


        return http.build();
    }
}
