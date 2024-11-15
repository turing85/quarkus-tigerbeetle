package de.turing85.quarkus.tigerbeetle.bean;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

import com.tigerbeetle.Client;
import de.turing85.quarkus.tigerbeetle.config.TigerbeetleConfig;

@Dependent
public class TigerBeetleClient {
  @Produces
  @Singleton
  public Client client(TigerbeetleConfig config) {
    return new Client(config.clusterIdAsBytes(), config.repliaAcressesAsArray());
  }
}
