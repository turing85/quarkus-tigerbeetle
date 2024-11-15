package de.turing85.quarkus.tigerbeetle.boundary.rest.exception.mapper;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import io.quarkus.logging.Log;

@Provider
@SuppressWarnings("unused")
public class CatchAllExceptionMapper implements ExceptionMapper<RuntimeException> {
  @Override
  public final Response toResponse(RuntimeException runtimeException) {
    Log.error("Error occurred", runtimeException);
    // @formatter:off
    return Response
        .serverError()
        .type(MediaType.APPLICATION_JSON_TYPE)
        .entity("Internal Server Error")
        .build();
    // @formatter:on
  }
}
