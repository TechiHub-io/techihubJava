package com.techihub.job.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.techihub.job.enums.Permission.*;

@Getter
@RequiredArgsConstructor
public enum Role {

    SUPERADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE,
                    CANDIDATE_READ,
                    CANDIDATE_UPDATE,
                    CANDIDATE_DELETE,
                    CANDIDATE_CREATE,
                    EMPLOYER_READ,
                    EMPLOYER_UPDATE,
                    EMPLOYER_DELETE,
                    EMPLOYER_CREATE
            )
    ),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE,
                    CANDIDATE_READ,
                    CANDIDATE_UPDATE,
                    CANDIDATE_DELETE,
                    CANDIDATE_CREATE,
                    EMPLOYER_READ,
                    EMPLOYER_UPDATE,
                    EMPLOYER_DELETE,
                    EMPLOYER_CREATE
            )
    ),
    MANAGER(
            Set.of(
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE,
                    CANDIDATE_READ,
                    EMPLOYER_READ,
                    EMPLOYER_CREATE
            )
    ),
    EMPLOYER(
            Set.of(
                    EMPLOYER_READ,
                    EMPLOYER_UPDATE,
                    EMPLOYER_DELETE,
                    EMPLOYER_CREATE,
                    CANDIDATE_READ
            )
    ),
    CANDIDATE(
            Set.of(
                    CANDIDATE_READ,
                    CANDIDATE_UPDATE,
                    CANDIDATE_DELETE,
                    CANDIDATE_CREATE
            )
    );

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
