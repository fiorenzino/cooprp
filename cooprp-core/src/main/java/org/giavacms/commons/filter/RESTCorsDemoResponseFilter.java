package org.giavacms.commons.filter;

import org.jboss.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
public class RESTCorsDemoResponseFilter implements ContainerResponseFilter {

    Logger logger = Logger.getLogger(getClass());

    @Override
    public void filter(ContainerRequestContext requestCtx, ContainerResponseContext responseCtx) throws IOException {
        logger.info(
                "Executing REST response filter getMethod: " + requestCtx.getMethod() + " - " + requestCtx.getUriInfo()
                        .getPath());
        responseCtx.getHeaders().add("Access-Control-Allow-Credentials", "true");
        responseCtx.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");

        responseCtx.getHeaders().add("Access-Control-Max-Age", "1209600");

        // When HttpMethod comes as OPTIONS, just acknowledge that it accepts...
        //      if (requestCtx.getRequest().getMethod().equals("OPTIONS"))
        //      {
        //         log.info("HTTP Method (OPTIONS) - Detected!");
        //
        //         // Just send a OK signal back to the browser
        //         responseCtx.setEntity(Response.Status.OK).build());
        //         return;
        //      }

        try {
            MultivaluedMap<String, String> multiValuedMap = requestCtx.getHeaders();
            if (multiValuedMap.containsKey("Origin")) {
                responseCtx.getHeaders().add("Access-Control-Allow-Origin", requestCtx.getHeaderString("Origin"));
            }
        } catch (Exception e) {
            responseCtx.getHeaders().add("Access-Control-Allow-Origin", "*");
        }
    }
}
