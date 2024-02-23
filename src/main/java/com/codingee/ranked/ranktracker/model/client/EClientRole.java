package com.codingee.ranked.ranktracker.model.client;


import com.google.common.collect.Sets;

import java.util.Set;

public enum EClientRole {
    PAID(Sets.newHashSet(EClientAuthority.AUTHORITY1, EClientAuthority.AUTHORITY2)),
    TRIAL(Sets.newHashSet(EClientAuthority.AUTHORITY1, EClientAuthority.AUTHORITY2)),
    FREE(Sets.newHashSet(EClientAuthority.AUTHORITY1, EClientAuthority.AUTHORITY2)),
    SUPER(Sets.newHashSet(EClientAuthority.AUTHORITY1, EClientAuthority.AUTHORITY2));

    private final Set<EClientAuthority> authorities;

    EClientRole(Set<EClientAuthority> authorities) {
        this.authorities = authorities;
    }

    public Set<EClientAuthority> getAuthorities() {
        return this.authorities;
    }

}
