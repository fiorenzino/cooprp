package org.giavacms.commons.auth.jwt.util;

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

public class JWTUtils
{

   private static Logger logger = Logger.getLogger(JWTUtils.class);
   private static final String AUTHORIZATION_PROPERTY = "Authorization";
   private static final String AUTHORIZATION_PROPERTY_UPPER = "AUTHORIZATION";
   private static final String TOKEN_SCHEMA = "Bearer";
   private static final String TOKEN_SCHEMA_UPPER = "BEARER";

   public static final String USERNAME = "username";
   public static final String NAME = "name";

   public static final String MAIL = "mail";
   public static final String ROLES = "roles";

   public static String getBearerToken(HttpServletRequest httpServletRequest) throws Exception
   {
      String complete = null;
      if (httpServletRequest.getHeader(AUTHORIZATION_PROPERTY) != null)
      {
         complete = httpServletRequest.getHeader(AUTHORIZATION_PROPERTY);
      }
      else if (httpServletRequest.getHeader(AUTHORIZATION_PROPERTY_UPPER) != null)
      {
         complete = httpServletRequest.getHeader(AUTHORIZATION_PROPERTY_UPPER);
      }
      if (complete != null && complete.contains(TOKEN_SCHEMA))
      {
         return complete.substring(complete.indexOf(TOKEN_SCHEMA) + TOKEN_SCHEMA.length() + 1).trim();
      }
      else if (complete != null && complete.contains(TOKEN_SCHEMA_UPPER))
      {
         return complete.substring(complete.indexOf(TOKEN_SCHEMA_UPPER) + TOKEN_SCHEMA_UPPER.length() + 1).trim();
      }
      throw new Exception("no token in header");

   }

   public static String encode(String secret, int tokenExpireTime, String username, String nome, List<String> ruoli)
   {
      HashMap<String, Object> claims = new HashMap<String, Object>();
      claims.put(USERNAME, username);
      //claims.put(NAME, nome);
      claims.put(ROLES, ruoli.toArray());

      JWTSigner signer = new JWTSigner(secret);
      String token = signer.sign(claims,
               new JWTSigner.Options().setAlgorithm(Algorithm.HS256)
                        .setExpirySeconds(tokenExpireTime).setIssuedAt(true));
      return token;
   }

   public static Map<String, Object> decode(String token, String secret)
            throws SignatureException, NoSuchAlgorithmException, JWTVerifyException, InvalidKeyException, IOException
   {
      JWTVerifier verifier = new JWTVerifier(secret);
      Map<String, Object> decoded = verifier.verify(token);
      return decoded;
   }

   public static String renew(String secret, int tokenExpireTime, String token)
            throws NoSuchAlgorithmException, SignatureException, JWTVerifyException, InvalidKeyException, IOException
   {
      Map<String, Object> claims = JWTUtils.decode(token, secret);
      JWTSigner signer = new JWTSigner(secret);
      String newToken = signer.sign(claims,
               new JWTSigner.Options().setAlgorithm(Algorithm.HS256)
                        .setExpirySeconds(tokenExpireTime).setIssuedAt(true));
      return newToken;
   }

   public static boolean tokenVerificationUsername(String token, String secret, String username) throws Exception
   {
      Map<String, Object> tokenMap = JWTUtils.decode(token, secret);
      return tokenMap.containsKey(USERNAME)
               && tokenMap.get(USERNAME) != null
               && !tokenMap.get(USERNAME).toString().trim().isEmpty()
               && tokenMap.get(USERNAME).toString().trim().equals(username.trim());

   }

   public static String getMail(String token, String secret) throws Exception
   {
      Map<String, Object> tokenMap = JWTUtils.decode(token, secret);
      return (String) tokenMap.get(MAIL);
   }
}
