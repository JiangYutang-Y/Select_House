package com.selecthome.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity
public class Role {
    public static final String USER = "user";
    public static final String ADMIN = "admin";

    public static final String USER_HOUSE_WRITE = "house:write";
    public static final String USER_HOUSE_READ = "house:read";

    public static List<String> getAuthorities(String name) {
        switch (name) {
            case USER:
                return Arrays.asList(USER_HOUSE_READ);
            case ADMIN:
                return Arrays.asList(USER_HOUSE_WRITE, USER_HOUSE_READ);
            default:
                return Collections.emptyList();
        }
    }

    @Id
    private Long id;
    private Long userId;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
