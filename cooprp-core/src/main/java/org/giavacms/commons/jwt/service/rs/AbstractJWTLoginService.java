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
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// @Path("/login")
//@Consumes(MediaType.APPLICATION_JSON)
//@Produces(MediaType.APPLICATION_JSON)
public abstract class AbstractJWTLoginService implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Logger logger = Logger.getLogger(getClass());

    @Context
    protected HttpServletRequest httpServletRequest;

    protected abstract List<String> getAvailableJwtRoles() throws Exception;

    protected abstract String getJwtSecret() throws Exception;

    protected abstract int getJwtExpireTime() throws Exception;

    @GET
    public Response get(@QueryParam("username") String username, @QueryParam("password") String password) {
        return post(new JWTLogin(username, password));
    }

    @POST
    public Response post(JWTLogin jwtLogin) {
        try {
            if (httpServletRequest.getUserPrincipal() != null) {
                httpServletRequest.logout();
            }
        } catch (ServletException e) {
            e.printStackTrace();
        }

        Utente utente = null;
        try {
            utente = getUtente(jwtLogin);
            if (utente == null) {
                throw new Exception("failed to get utente");
            }
        } catch (Throwable e) {
            System.out.println("forbidden if login fails: ");
            // forbidden if login fails
            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("Login failed for user: " + jwtLogin
                    ).build();
        }

        Response response = verify(utente);
        if (response != null) {
            return response;
        }

        try {

            List<String> userRoles = new ArrayList<String>();
            for (String jwtRole : getAvailableJwtRoles()) {
                checkRole(utente, jwtRole);
            }

            String token = JWTUtils.encode(getJwtSecret(), getJwtExpireTime(),
                    jwtLogin.getUsername(), utente.getNome(), utente.getRuoli());

            // return Response.status(Response.Status.OK).entity("{\"token\":\"" + token + "\"}").build();
            return Response.status(Response.Status.OK).entity(new JWTToken(token)).build();
        } catch (Throwable e) {
            logger.error(e.getMessage());
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error attempting login: " + e.getMessage())
                    .build();
        }
    }

    protected Response verify(Utente utente) {
        return null;
    }

    protected abstract Utente getUtente(JWTLogin jwtLogin) throws Exception;

    protected abstract void checkRole(Utente utente, String jwtRole) throws Exception;

    protected String getNome(String loginName) {
        return loginName;
    }

    @OPTIONS
    public Response options() {
        logger.info("@OPTIONS");
        return Response.ok().build();
    }

    @OPTIONS
    @Path("{path:.*}")
    public Response allOptions() {
        logger.info("@OPTIONS ALL");
        return Response.ok().build();
    }

}
