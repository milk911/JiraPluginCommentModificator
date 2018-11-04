package com.sgc.services;

import com.sgc.logic.CommentAlias;
import com.sgc.logic.PluginConfiguration;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})

public class RestService {
    @GET
    @Path("aliases")
    public Response getAliases() {
        return Response.ok(PluginConfiguration.getAliases()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @DELETE
    @Path("aliase/{id}")
    public Response delAliase(@PathParam("id") String id) {
        return Response.ok(PluginConfiguration.delAlias(id)).type(MediaType.TEXT_HTML).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("aliases")
    public Response addAlias(final CommentAlias newAlias) {
        return Response.ok(PluginConfiguration.addAlias(newAlias)).type(MediaType.TEXT_HTML).build();
    }
}


