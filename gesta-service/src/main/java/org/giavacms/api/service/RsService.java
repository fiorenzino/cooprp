package org.giavacms.api.service;

import org.giavacms.api.model.HeapStatus;
import org.giavacms.api.model.HostStatus;
import org.giavacms.api.model.NonHeapStatus;
import org.giavacms.api.model.QueueStatus;
import org.giavacms.api.util.CliUtils;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

public abstract class RsService implements Serializable {

    protected final Logger logger = Logger.getLogger(getClass().getName());

    private static final long serialVersionUID = 1L;



    @GET
    @Path("/up")
    public Response up() {
        return Response.status(Response.Status.OK).entity(true).build();
    }

    @GET
    @Path("/status")
    public Response status() {
        ModelControllerClient client = null;
        try {
            client = CliUtils.connect(9990);
            HeapStatus hs = CliUtils.readHeap(client);
            NonHeapStatus nhs = CliUtils.readNonHeap(client);
            List<QueueStatus> qss = readQueueStatuses(client);
            HostStatus hostStatus = new HostStatus(hs, nhs, qss);
            return Response.status(Response.Status.OK).entity(hostStatus)
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getClass().getCanonicalName()).build();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (Exception e) {

                }
            }
        }
    }

    // override status
    protected List<QueueStatus> readQueueStatuses(ModelControllerClient client) {
        return null;
    }

}

