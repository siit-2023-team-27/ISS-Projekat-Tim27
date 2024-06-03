package config;

import Services.IService;
import org.springframework.security.core.session.SessionRegistryImpl;
import Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import security.auth.RestAuthEntryPoint;
import security.auth.TokenAuthFilter;
import util.TokenUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = {RestAuthEntryPoint.class, TokenUtils.class})
// Ukljucivanje podrske za anotacije "@Pre*" i "@Post*" koje ce aktivirati autorizacione provere za svaki pristup metodi
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

//    @Autowired
//    private UserService userService;
    private static final String GROUPS = "groups";
    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";

    private final KeycloakLogoutHandler keycloakLogoutHandler;

    WebSecurityConfig(KeycloakLogoutHandler keycloakLogoutHandler) {
        this.keycloakLogoutHandler = keycloakLogoutHandler;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(sessionRegistry());
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(new AntPathRequestMatcher("/api/users"))
                .hasRole("user")
                .requestMatchers(new AntPathRequestMatcher("/"))
                .permitAll()
                .anyRequest()
                .authenticated());
        http.oauth2ResourceServer((oauth2) -> oauth2
                .jwt(Customizer.withDefaults()));
        http.oauth2Login(Customizer.withDefaults())
                .logout(logout -> logout.addLogoutHandler(keycloakLogoutHandler).logoutSuccessUrl("/"));
        return http.build();
    }


    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            var authority = authorities.iterator().next();
            boolean isOidc = authority instanceof OidcUserAuthority;

            if (isOidc) {
                var oidcUserAuthority = (OidcUserAuthority) authority;
                var userInfo = oidcUserAuthority.getUserInfo();

                // Tokens can be configured to return roles under
                // Groups or REALM ACCESS hence have to check both
                if (userInfo.hasClaim(REALM_ACCESS_CLAIM)) {
                    var realmAccess = userInfo.getClaimAsMap(REALM_ACCESS_CLAIM);
                    var roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                    System.out.println("USAO VAMO");

                    System.out.println(roles);
                } else if (userInfo.hasClaim(GROUPS)) {
                    Collection<String> roles = (Collection<String>) userInfo.getClaim(
                            GROUPS);
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                }
            } else {
                System.out.println("USAO U ELSE");

                var oauth2UserAuthority = (OAuth2UserAuthority) authority;
                Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

                if (userAttributes.containsKey(REALM_ACCESS_CLAIM)) {
                    Map<String, Object> realmAccess = (Map<String, Object>) userAttributes.get(
                            REALM_ACCESS_CLAIM);
                    Collection<String> roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                }
            }
            return mappedAuthorities;
        };
    }

    Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
        System.out.println("xxxxxx"+roles.stream());
        return roles.stream().map(role -> new SimpleGrantedAuthority( role)
        ).collect(
                Collectors.toList());
    }
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
        System.out.println("1. FILTER CHAIN");
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // sve neautentifikovane zahteve obradi uniformno i posalji 401 gresku
        http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
        http.authorizeRequests()
                //OVDE DOZVOLJAVA RUTE AUTENTIFIKOVANIM KORISNICIMA KO GDE MOZE
                //.requestMatchers("/api/accommodations").hasAuthority("GUEST")
                .requestMatchers("/socket/**").permitAll()
                .requestMatchers("/api/users/{id}").permitAll()
                .requestMatchers(HttpMethod.POST ,"/api/users").permitAll()
//                .requestMatchers(HttpMethod.GET ,"/api/users").permitAll()
                .requestMatchers(HttpMethod.DELETE ,"/api/users/{id}").permitAll()
                .requestMatchers(HttpMethod.GET ,"/api/accommodations").permitAll()
                .requestMatchers(HttpMethod.GET ,"/api/accommodations/{id}").permitAll()
                .requestMatchers(HttpMethod.GET ,"/api/search-filter").permitAll()
                .requestMatchers(HttpMethod.GET ,"/api/filter").permitAll()
                .requestMatchers(HttpMethod.GET ,"/api/prices").permitAll()
                .requestMatchers(HttpMethod.GET ,"/{accommodationId}/amenities").permitAll()
                .requestMatchers(HttpMethod.POST,"/auth/reauthenticate").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/notifications").permitAll()
                .requestMatchers(HttpMethod.POST,"/api/amenities").permitAll()
                .requestMatchers("/api/amenities").permitAll()
                .requestMatchers(HttpMethod.GET ,"/api/accommodations/price/{accommodationId}/{date}").permitAll()
                .requestMatchers(HttpMethod.GET ,"/api/accommodations/unverified").permitAll()
                .requestMatchers(HttpMethod.GET ,"/api/accommodations/taken-dates/{accommodationId}").permitAll()
                .requestMatchers(HttpMethod.GET ,"/api/accommodations/isAvailable/{accommodationId}/{date}").permitAll()
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
                .requestMatchers(HttpMethod.GET, "/api/users")
                .requestMatchers(HttpMethod.POST, "/api/users")
                .requestMatchers(HttpMethod.GET, "/auth/confirm-account")
                .requestMatchers(HttpMethod.GET, "/api/accommodations/verified")
                .requestMatchers( HttpMethod.POST,"/api/accommodations")
                .requestMatchers( HttpMethod.GET,"/api/accommodation-ratings/for-accommodation")
                .requestMatchers(HttpMethod.GET,"/api/accommodation-ratings/for-accommodation/{id}")
                .requestMatchers(HttpMethod.GET,"/api/reports/generate-pdf/accommodation/{hostId}/{accommodationId}/{year}")
                .requestMatchers(HttpMethod.GET,"/api/reports/generate-pdf/date-range/{hostId}")
                .requestMatchers("/socket/**")
                .requestMatchers( "/images/**");
    }
}
