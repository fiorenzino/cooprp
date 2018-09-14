package org.giavacms.commons.auth.basic.util;

import com.auth0.jwt.Algorithm;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import org.jboss.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicUtils {

    private static Logger logger = Logger.getLogger(BasicUtils.class);
    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHORIZATION_PROPERTY_UPPER = "AUTHORIZATION";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    private static final String AUTHENTICATION_SCHEME_UPPER = "BASIC";

    public static String getBasic(HttpServletRequest httpServletRequest) throws Exception {
        String complete = null;
        if (httpServletRequest.getHeader(AUTHORIZATION_PROPERTY) != null) {
            complete = httpServletRequest.getHeader(AUTHORIZATION_PROPERTY);
        } else if (httpServletRequest.getHeader(AUTHORIZATION_PROPERTY_UPPER) != null) {
            complete = httpServletRequest.getHeader(AUTHORIZATION_PROPERTY_UPPER);
        } if (complete.contains(AUTHENTICATION_SCHEME)) {
            return complete.substring(complete.indexOf(AUTHENTICATION_SCHEME) + AUTHENTICATION_SCHEME.length() + 1).trim();
        } else if (complete.contains(AUTHENTICATION_SCHEME_UPPER)) {
            return complete.substring(complete.indexOf(AUTHENTICATION_SCHEME_UPPER) + AUTHENTICATION_SCHEME_UPPER.length() + 1).trim();
        }
        throw new Exception("no basic in header");

    }

    public static String encode(String secret, int tokenExpireTime, String username, String nome, List<String> ruoli) {
        HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("username", username);
        claims.put("name", nome);
        claims.put("roles", ruoli.toArray());

        JWTSigner signer = new JWTSigner(secret);
        String token = signer.sign(claims,
                new JWTSigner.Options().setAlgorithm(Algorithm.HS256)
                        .setExpirySeconds(tokenExpireTime).setIssuedAt(true));
        return token;
    }

    public static Map<String, Object> decode(String token, String secret)
            throws SignatureException, NoSuchAlgorithmException, JWTVerifyException, InvalidKeyException, IOException {
        JWTVerifier verifier = new JWTVerifier(secret);
        Map<String, Object> decoded = verifier.verify(token);
        return decoded;
    }

}
