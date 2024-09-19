package com.sparta.orderking.store.entity;

public enum StoreServiceEnum {
    OPEN(Authority.OPEN),  // 사용자 권한
    CLOSED(Authority.CLOSED);  // 관리자 권한

    private final String authority;

    StoreServiceEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String OPEN = "SERVICE_OPEN";
        public static final String CLOSED = "SERVICE_CLOSED";
    }
}
