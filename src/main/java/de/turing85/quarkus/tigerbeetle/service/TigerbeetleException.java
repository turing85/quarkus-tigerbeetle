package de.turing85.quarkus.tigerbeetle.service;

import java.util.List;
import java.util.Map;

public class TigerbeetleException extends RuntimeException {
  private TigerbeetleException(String message, Object reason) {
    super(message + ": " + reason.toString());
  }

  public static TigerbeetleException accountCreateFailed(Map<String, List<String>> reason) {
    return new TigerbeetleException("AccountCreationBatch failed", reason);
  }
}
