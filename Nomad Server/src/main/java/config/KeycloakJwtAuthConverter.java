package config;

import Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class KeycloakJwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        return new JwtAuthenticationToken(
                source,
                Stream.concat(
                        new JwtGrantedAuthoritiesConverter().convert(source).stream(),
                        extractResourceRoles(source).stream()
                ).collect(toSet())
        );
    }
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt){
       // var resourceAccess = new HashMap<>(jwt.getClaim("realm_access"));
        //var eternal = (Map<String, List<String>>) jwt.getClaim("realm_access");
        String roles =  jwt.getClaim("role");
        //var roles = eternal.get("roles");

        var col = new ArrayList<SimpleGrantedAuthority>();
        col.add(new SimpleGrantedAuthority(roles));
        return col;
    }
}
