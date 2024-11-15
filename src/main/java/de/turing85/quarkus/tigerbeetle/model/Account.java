package de.turing85.quarkus.tigerbeetle.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@AllArgsConstructor
@Value
@Jacksonized
@Builder
public class Account {
  String name;

  @lombok.Builder.Default
  UUID id = UUID.randomUUID();

  @lombok.Builder.Default
  Currency currency = Currency.EUR;
}
