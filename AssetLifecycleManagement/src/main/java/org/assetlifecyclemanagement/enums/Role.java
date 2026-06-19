package org.assetlifecyclemanagement.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum Role {
    ADMIN(Set.of(
            Permissions.EMPLOYEE_READ,
            Permissions.EMPLOYEE_WRITE,
            Permissions.EMPLOYEE_DELETE,
            Permissions.CACHE_READ,
            Permissions.CACHE_DELETE,
            Permissions.HEALTH_READ)),
    USER(Set.of(
            Permissions.EMPLOYEE_READ,
            Permissions.EMPLOYEE_WRITE,
            Permissions.HEALTH_READ));

    private final Set<Permissions> permissions;
}
