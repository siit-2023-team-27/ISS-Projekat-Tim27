package config;

import Services.IService;
import Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import security.auth.RestAuthEntryPoint;
import security.auth.TokenAuthFilter;
import util.TokenUtils;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = {RestAuthEntryPoint.class, TokenUtils.class})
// Ukljucivanje podrske za anotacije "@Pre*" i "@Post*" koje ce aktivirati autorizacione provere za svaki pristup metodi
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

//    @Autowired
//    private UserService userService;
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //10 RUNDI HASH
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    @Autowired
    private RestAuthEntryPoint restAuthenticationEntryPoint;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Autowired
    private TokenUtils tokenUtils;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // sve neautentifikovane zahteve obradi uniformno i posalji 401 gresku
        http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
        http.authorizeRequests()
                //OVDE DOZVOLJAVA RUTE AUTENTIFIKOVANIM KORISNICIMA KO GDE MOZE
                // DODATI @PreAuthorize("hasAuthority('ADMIN')") IZNAD APIJA za autorizaciju
                // ILI OVDE STAVITI .requestMatchers("/api/accommodations").hasAuthority("ADMIN")
                //.requestMatchers("/api/**").permitAll()
                // za svaki drugi zahtev korisnik mora biti autentifikovan
                .anyRequest().authenticated().and()
                .cors().and()
                .addFilterBefore(new TokenAuthFilter(tokenUtils,  userDetailsService()), BasicAuthenticationFilter.class);
//        http.authorizeRequests().anyRequest().permitAll();
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        //OVIM RUTAMA MOGU NEREGISTROVANI DA PRISTUPE
        return (web) -> web.ignoring().requestMatchers(HttpMethod.POST, "/auth/login")
                .requestMatchers(HttpMethod.POST, "/auth/signup")
                .requestMatchers(HttpMethod.GET, "/auth/confirm-account")
                .requestMatchers("/api/**");

                .requestMatchers(HttpMethod.GET, "/**")
    }

}
