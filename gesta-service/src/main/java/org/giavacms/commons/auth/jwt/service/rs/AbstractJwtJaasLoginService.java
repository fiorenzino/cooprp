package org.giavacms.commons.auth.jwt.service.rs;

import org.giavacms.commons.auth.jwt.model.pojo.JWTLogin;
import org.giavacms.commons.model.pojo.Utente;

import java.io.Serializable;

public abstract class AbstractJwtJaasLoginService extends AbstractJwtLoginService implements Serializable
{

   private static final long serialVersionUID = 1L;

   protected Utente getUtente(JWTLogin jwtLogin) throws Exception
   {
      httpServletRequest.login(jwtLogin.getUsername(), jwtLogin.getPassword());
      String principal = httpServletRequest.getUserPrincipal().getName();
      if (principal == null)
      {
         return null;
      }
      Utente utente = new Utente();
      utente.setUsername(principal);
      utente.setNome(getNome(principal));
      return utente;
   }

   protected void checkRole(Utente utente, String jwtRole)
   {
      if (httpServletRequest.isUserInRole(jwtRole))
      {
         utente.getRuoli().add(jwtRole);
      }
   }

   abstract protected String getNome(String principal);

}