package de.turing85.quarkus.tigerbeetle.boundary.rest.exception.mapper;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

import de.turing85.quarkus.tigerbeetle.service.TigerbeetleException;

@Produces
@SuppressWarnings("unused")
public class TigerbeetleExceptionMapper implements ExceptionMapper<TigerbeetleException> {
  @Override
  public Response toResponse(TigerbeetleException exception) {
    // @formatter:off
    return Response
        .status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE)
        .entity(exception.getMessage())
        .build();
    // @formatter:on
  }
}
