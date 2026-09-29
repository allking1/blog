package spingboot.beginner.blog.config;

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
                // 1. TẮT CSRF: Cần thiết khi dùng API RESTful
                .csrf(csrf -> csrf.disable())

                // 2. PHÂN QUYỀN ĐƯỜNG DẪN (THỨ TỰ CỰC KỲ QUAN TRỌNG)
                .authorizeHttpRequests(auth -> auth
                        // [BƯỚC A] KHÓA KHU VỰC ADMIN TRƯỚC:
                        // Dòng này chặn ngay lập tức mọi file HTML trong folder /admin và mọi API /api/admin
                        .requestMatchers("/admin/**", "/api/admin/**").authenticated()

                        // [BƯỚC B] MỞ KHU VỰC PUBLIC SAU:
                        // Vì thư mục /admin đã bị dòng trên chặn, "/**/*.html" ở đây chỉ còn tác dụng
                        // với các file bên ngoài (như /login.html, /index.html)
                        .requestMatchers("/", "/**/*.html", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/api/articles/**").permitAll()

                        // Đảm bảo API login/logout luôn mở để không bị kẹt vòng lặp
                        .requestMatchers("/api/login", "/api/logout").permitAll()

                        // Các request khác bắt buộc đăng nhập
                        .anyRequest().authenticated()
                )

                // 3. XỬ LÝ LỖI 401 (CHƯA ĐĂNG NHẬP)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (request.getRequestURI().startsWith("/api/")) {
                                // Nếu là API -> Trả về JSON để Javascript (Fetch) tự xử lý
                                response.setContentType("application/json;charset=UTF-8");
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.getWriter().write("{\"error\": \"Bạn chưa đăng nhập!\"}");
                            } else {
                                // Nếu là đường dẫn web (VD: gõ /admin/dashboard.html lên thanh địa chỉ)
                                // -> Backend ép trình duyệt bẻ lái 302 về trang login NGAY LẬP TỨC
                                response.sendRedirect("/login.html");
                            }
                        })
                )

                // 4. CẤU HÌNH API ĐĂNG NHẬP (/api/login)
                .formLogin(form -> form
                        .loginProcessingUrl("/api/login")
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
                        .deleteCookies("JSESSIONID") // Xóa phiên làm việc
                        .permitAll()
                );

        return http.build();
    }

    // 6. TÀI KHOẢN ADMIN TEST TRONG RAM
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}123456")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}