package spingboot.beginner.blog.config;


import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Tắt CSRF để tránh lỗi form đăng nhập mặc định bị chặn
                .csrf(csrf -> csrf.disable())

                // 2. Phân luồng đường dẫn (Routing)
                .authorizeHttpRequests(auth -> auth
                        // KHÓA chặt đúng 2 trường hợp: gõ "/admin" hoặc gõ "/admin/bất_cứ_gì"
                        .requestMatchers("/admin", "/admin/**").authenticated()

                        // MỞ CỬA toàn bộ các đường dẫn còn lại (bao gồm /home, /article, và cả trang /error)
                        .anyRequest().permitAll()
                )

                // 3. Cấu hình Form Login
                .formLogin(form -> form
                        .defaultSuccessUrl("/admin", true) // Thành công thì đẩy thẳng vào Admin
                        .permitAll()
                )

                // 4. Cấu hình Đăng xuất
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/") // Đăng xuất xong về trang chủ
                        .permitAll()
                );

        return http.build();
    }

    // Thiết lập bộ mã hóa mật khẩu thô (Plain Text) cho dễ thực hành
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // Khởi tạo tài khoản
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password("123456") // Gõ thẳng mật khẩu, không cần {noop} nữa
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}