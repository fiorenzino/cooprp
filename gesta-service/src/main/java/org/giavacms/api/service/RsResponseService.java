package org.giavacms.api.service;

import org.giavacms.api.management.AppConstants;
import org.jboss.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.Serializable;
import java.util.Map;

public abstract class RsResponseService implements Serializable {

    private static final long serialVersionUID = 1L;

    protected final Logger logger = Logger.getLogger(getClass());

    //@OPTIONS
    //public Response options() {
    //    logger.info("@OPTIONS");
    //    return Response.ok().build();
    // }

    //    @OPTIONS
    //    @Path("{val:.}")
    //    public Response allOptions(@PathParam(value = "val") String val) {
    //        logger.info("@OPTIONS ALL " + val);
    //        return Response.ok().build();
    //    }


    public static Response jsonResponse(Map<String, String> toJson, Status status) {
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        for (String key : toJson.keySet()) {
            jsonObjBuilder.add(key, toJson.get(key));
        }

        JsonObject jsonObj = jsonObjBuilder.build();
        return Response.status(status).entity(jsonObj.toString()).build();
    }

    public static Response jsonResponse(Status status, String key, Object value) {
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add(key, value.toString());
        JsonObject jsonObj = jsonObjBuilder.build();
        return Response.status(status).entity(jsonObj.toString()).build();
    }

    public static Response jsonMessageResponse(Status status, Object object) {
        if (object instanceof Throwable) {
            Throwable t = (Throwable) object;
            return jsonResponse(
                     status,
                     AppConstants.JSON_ERROR_KEY,
                     t.getMessage() == null ? t.getClass().getCanonicalName() : t
                              .getMessage());
        } else {
            return jsonResponse(status, AppConstants.JSON_ERROR_KEY, "" + object);

        }
    }

    public static Response jsonErrorMessageResponse(Object error) {
        if (error instanceof Throwable) {
            Throwable t = (Throwable) error;
            return jsonResponse(Status.INTERNAL_SERVER_ERROR,
                     AppConstants.JSON_ERROR_KEY, t.getMessage() == null ? t.getClass()
                              .getCanonicalName() : t.getMessage());
        } else {
            return jsonResponse(Status.INTERNAL_SERVER_ERROR,
                     AppConstants.JSON_ERROR_KEY, "" + error);
        }
    }

}