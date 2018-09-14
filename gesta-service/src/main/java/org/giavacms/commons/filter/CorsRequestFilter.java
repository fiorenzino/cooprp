package org.giavacms.commons.filter;

import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
public class CorsRequestFilter implements ContainerRequestFilter {

    Logger logger = Logger.getLogger(getClass());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        CorsFilter filter = new CorsFilter();
        filter.getAllowedOrigins().add("http://localhost");
    }
}
