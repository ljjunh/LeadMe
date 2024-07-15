package com.ssafy.withme.domain.authcode;

import com.ssafy.withme.domain.user.OauthServerType;

public interface AuthCodeRequestUrlProvider {

    OauthServerType supportServer();

    String provide();
}
