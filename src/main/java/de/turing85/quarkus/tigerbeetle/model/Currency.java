package de.turing85.quarkus.tigerbeetle.model;

import java.util.Arrays;
import java.util.Optional;

public enum Currency {
  EUR(1);

  public final int id;

  Currency(int id) {
    this.id = id;
  }

  public static Optional<Currency> of(int id) {
    return Arrays.stream(values()).filter(currency -> currency.id == id).findFirst();
  }

}
