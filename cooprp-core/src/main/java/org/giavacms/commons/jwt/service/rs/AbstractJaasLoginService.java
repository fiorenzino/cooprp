package org.giavacms.commons.jwt.service.rs;

import org.giavacms.commons.jwt.model.pojo.JWTLogin;
import org.giavacms.commons.jwt.model.pojo.JWTToken;
import org.giavacms.commons.jwt.util.JWTUtils;
import org.giavacms.commons.model.pojo.Utente;
import org.jboss.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractJaasLoginService extends AbstractJWTLoginService implements Serializable
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

}