package com.codingee.ranked.ranktracker.model.client;

public enum EClientAuthority {
    AUTHORITY1("authority:1"),
    AUTHORITY2("authority:2"),
    AUTHORITY3("authority:3"),
    AUTHORITY4("authority:4");

    private final String authority;
    EClientAuthority(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

}
