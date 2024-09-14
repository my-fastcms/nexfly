/**
 * Copyright (c) 广州小橘灯信息科技有限公司 2016-2017, wjun_java@163.com.
 * <p>
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * http://www.xjd2020.com
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nexfly.common.auth.token;

import com.nexfly.common.auth.model.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenManager {

    /**
     * jwt secretKey
     */
    @Value("${nexfly.auth.token.secret-key:}")
    private String secretKey;

    /**
     * jwt token validity seconds
     */
    @Value("${nexfly.auth.token.expire.seconds:18000}")
    private long tokenValidityInSeconds;

    private byte[] secretKeyBytes;

    protected static final String AUTHORITIES_KEY = "auth";

    public static final String USER_ID = "userId";
    public static final String USER_NAME = "username";

    private static final String TOKEN_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public AccessToken createToken(UserInfo user, Collection<? extends GrantedAuthority> authorities) {
        long now = System.currentTimeMillis();
        Date validity;
        validity = new Date(now + tokenValidityInSeconds * 1000L);
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ID, user.getUserId());
        claims.put(USER_NAME, user.getUsername());
        if (authorities != null) {
            claims.put(AUTHORITIES_KEY, StringUtils.join(authorities.toArray(), ","));
        }
        String token = Jwts.builder().setClaims(claims).setExpiration(validity)
                .signWith(Keys.hmacShaKeyFor(getSecretKeyBytes()), SignatureAlgorithm.HS256).compact();
        return new AccessToken(tokenValidityInSeconds, token);
    }

    public AccessToken createToken(UserInfo user) {
        return createToken(user, null);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSecretKeyBytes()).build().parseClaimsJws(token).getBody();
        Collection<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get(AUTHORITIES_KEY));
        Integer userId = (Integer) claims.get(USER_ID);
        String username = (String) claims.get(USER_NAME);
        UserInfo principal = new UserInfo(Long.valueOf(userId), username, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public void validateToken(String token) {
        Jwts.parserBuilder().setSigningKey(getSecretKeyBytes()).build().parseClaimsJws(token);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isNotBlank(bearerToken)) {
            return bearerToken.startsWith(TOKEN_PREFIX) ? bearerToken.substring(TOKEN_PREFIX.length()) : bearerToken;
        }

        final String token = request.getParameter(ACCESS_TOKEN);
        if (StringUtils.isNotBlank(token)) {
            return token.startsWith(TOKEN_PREFIX) ? token.substring(TOKEN_PREFIX.length()) : token;
        }
        return null;
    }

    private byte[] getSecretKeyBytes() {
        if (secretKeyBytes == null) {
            secretKeyBytes = Decoders.BASE64.decode(secretKey);
        }
        return secretKeyBytes;
    }

}
