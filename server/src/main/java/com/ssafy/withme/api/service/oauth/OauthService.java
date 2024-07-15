package com.ssafy.withme.api.service.oauth;


import com.ssafy.withme.domain.authcode.AuthCodeRequestUrlProviderComposite;
import com.ssafy.withme.domain.user.OauthServerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }
}
