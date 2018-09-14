package org.giavacms.api.service;

import org.giavacms.api.repository.Repository;
import org.jboss.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.io.Serializable;

public abstract class BadRequestRsRepositoryService<T> extends RsRepositoryService<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected final Logger logger = Logger.getLogger(getClass());

    protected Repository<T> repository;

    public BadRequestRsRepositoryService() {

    }

    public BadRequestRsRepositoryService(Repository<T> repository) {
        this.repository = repository;
    }

    @POST
    public Response persist(T object) throws Exception {
        return Response.status(Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/{id}")
    public Response fetch(@PathParam("id") String id) {
        return Response.status(Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, T object) throws Exception {
        return Response.status(Status.BAD_REQUEST).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) throws Exception {
        return Response.status(Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/{id}/exist")
    public Response exist(@PathParam("id") Long id) {
        return Response.status(Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/listSize")
    public Response getListSize(@Context UriInfo ui) {
        return Response.status(Status.BAD_REQUEST).build();
    }

    @GET
    public Response getList(
            @DefaultValue("0") @QueryParam("startRow") Integer startRow,
            @DefaultValue("10") @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy, @Context UriInfo ui) {
        return Response.status(Status.BAD_REQUEST).build();
    }

}
