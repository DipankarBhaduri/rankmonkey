package com.rankmonkeysvc.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    ADMIN("ADMIN"),
    SUPER_ADMIN("SUPER_ADMIN"),
    USER("USER");

    @Getter
    private final String roleName;

    public List<SimpleGrantedAuthority> getAuthorities() {
        return Set.of("ROLE_" + this.roleName)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}