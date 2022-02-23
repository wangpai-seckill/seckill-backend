package org.wangpai.seckill.center.session;

public enum CookieType {
    TOKEN("token");

    private String name;

    CookieType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
