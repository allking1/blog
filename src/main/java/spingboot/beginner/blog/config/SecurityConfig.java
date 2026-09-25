package spingboot.beginner.blog.config; // Đảm bảo đúng tên package của bạn

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. TẮT CSRF: Bắt buộc tắt khi dùng API thuần để test trên localhost
                // Nếu không tắt, các API POST/PUT/DELETE sẽ bị chặn (báo lỗi 403 Forbidden)
                .csrf(csrf -> csrf.disable())

                // 2. PHÂN QUYỀN ĐƯỜNG DẪN
                .authorizeHttpRequests(auth -> auth
                        // Cho phép tải tự do các file giao diện tĩnh (HTML, CSS, JS)
                        .requestMatchers("/", "/*.html", "/css/**", "/js/**").permitAll()
                        // Cho phép khách vãng lai gọi các API công khai
                        .requestMatchers("/api/articles/**").permitAll()
                        // BẮT BUỘC ĐĂNG NHẬP khi gọi các API quản trị
                        .requestMatchers("/api/admin/**").authenticated()
                        .anyRequest().authenticated()
                )

                // 3. XỬ LÝ LỖI 401: Khi chưa đăng nhập mà cố gọi API Admin
                // (Thay vì bẻ lái về trang HTML login, nó sẽ trả về JSON)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Trả mã 401
                            response.getWriter().write("{\"error\": \"Bạn chưa đăng nhập!\"}");
                        })
                )

                // 4. CẤU HÌNH API ĐĂNG NHẬP (/api/login)
                .formLogin(form -> form
                        .loginProcessingUrl("/api/login") // Frontend sẽ POST username/password vào đây
                        .successHandler((request, response, authentication) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("{\"message\": \"Đăng nhập thành công!\"}");
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\": \"Sai tài khoản hoặc mật khẩu!\"}");
                        })
                        .permitAll()
                )

                // 5. CẤU HÌNH API ĐĂNG XUẤT (/api/logout)
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("{\"message\": \"Đăng xuất thành công!\"}");
                        })
                        .deleteCookies("JSESSIONID") // Xóa thẻ phiên làm việc (Cookie)
                        .permitAll()
                );

        return http.build();
    }

    // Tạo sẵn 1 tài khoản Admin trong RAM để test (Giống với code SSR cũ của bạn)
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}123456") // {noop} báo cho Spring biết mật khẩu này không mã hóa
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}