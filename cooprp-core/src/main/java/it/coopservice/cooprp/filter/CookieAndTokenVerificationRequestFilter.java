package it.coopservice.cooprp.filter;



import it.coopservice.cooprp.management.AppProperties;
import org.giavacms.commons.auth.jwtcookie.annotation.AccountCookieAndTokenVerification;
import org.giavacms.commons.auth.jwtcookie.filter.AbstractCookieAndTokenVerificationRequestFilter;

import javax.ws.rs.ext.Provider;

@Provider
@AccountCookieAndTokenVerification
public class CookieAndTokenVerificationRequestFilter extends AbstractCookieAndTokenVerificationRequestFilter
{

   @Override protected String getJwtSecret()
   {
      return AppProperties.jwtSecret.value();
   }
}
