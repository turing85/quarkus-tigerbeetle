package de.turing85.quarkus.tigerbeetle.config;

import java.util.Optional;

import com.tigerbeetle.UInt128;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "quarkus.tigerbeetle")
public interface TigerbeetleConfig {
  String DEFAULT_REPLICA_ADDRESS = "3000";

  @WithName("cluster-id")
  int clusterIdAsInt();

  Optional<String> replicaAddresses();

  default String[] repliaAcressesAsArray() {
    return replicaAddresses().orElse(DEFAULT_REPLICA_ADDRESS).split(",");
  }

  default byte[] clusterIdAsBytes() {
    return UInt128.asBytes(clusterIdAsInt());
  }
}
