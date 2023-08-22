package com.example.world.security.Service;

import com.example.world.security.dto.UserContext;
import com.example.world.security.exception.OAuthTypeMatchNotFoundException;
import com.example.world.security.exception.UserNotFoundException;
import com.example.world.user.SiteUser;
import com.example.world.user.UserRepository;
import com.example.world.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String oauthId = oAuth2User.getName();

        SiteUser user = null;
        String oauthType = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        if (!"KAKAO".equals(oauthType)) {
            throw new OAuthTypeMatchNotFoundException();
        }

        if (isNew(oauthType, oauthId)) {
            switch (oauthType) {
                case "KAKAO" -> {
                    Map attributesKakaoAcount = (Map) attributes.get("kakao_account");
                    String email = "%s@kakao.com".formatted(oauthId);
                    String nickname ="KAKAO_%s".formatted(oauthId);



                    if ((boolean) attributesKakaoAcount.get("has_email")) {
                        email = (String) attributesKakaoAcount.get("email");
                    }

                    user = new SiteUser();
                    user.setUsername(email);
                    user.setNickname(nickname);
                    user.setPassword("");
                    user.setRole(UserRole.valueOf("USER"));
                    userRepository.save(user);
                }
            }
        } else {
            user = userRepository.findByNickname("%s_%s".formatted(oauthType, oauthId))
                    .orElseThrow(UserNotFoundException::new);

        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        return new UserContext(user, authorities, attributes, userNameAttributeName);
    }

    private boolean isNew(String oAuthType, String oAuthId) {
        return userRepository.findByNickname("%s_%s".formatted(oAuthType, oAuthId)).isEmpty();
    }
}
