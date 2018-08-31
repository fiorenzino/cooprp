package org.giavacms.commons.jwt.model.pojo;

import java.io.Serializable;

/**
 * Created by fiorenzo on 30/06/15.
 */
public class JWTToken implements Serializable
{

   private static final long serialVersionUID = -7714660421724107420L;
   private String token;

   public JWTToken(String token)
   {
      this.token = token;
   }

   public String getToken()
   {
      return token;
   }

   public void setToken(String token)
   {
      this.token = token;
   }
}
