package com.example.world.security.dto;

import com.example.world.user.SiteUser;
import com.example.world.user.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class UserContext extends User implements OAuth2User {

    private final Long id;
    private final String username;
    private final String nickname;

    private Map<String, Object> attributes;
    private String userNameAttributeName;

    public UserContext(SiteUser user, List<GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
    }

    public UserContext(SiteUser user, List<GrantedAuthority> authorities, Map<String, Object> attributes, String userNameAttributeName) {
        this(user, authorities);
        this.attributes = attributes;
        this.userNameAttributeName = userNameAttributeName;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return super.getAuthorities().stream().collect(Collectors.toSet());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        return this.getAttribute(this.userNameAttributeName).toString();
    }
}
