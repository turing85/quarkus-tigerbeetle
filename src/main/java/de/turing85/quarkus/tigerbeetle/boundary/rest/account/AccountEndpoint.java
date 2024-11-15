package de.turing85.quarkus.tigerbeetle.boundary.rest.account;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import de.turing85.quarkus.tigerbeetle.model.Account;
import de.turing85.quarkus.tigerbeetle.service.AccountService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/accounts")
public class AccountEndpoint {
  private final AccountService accountService;

  @POST
  public Uni<Account> createAccount(Account account) {
    // @formatter:off
    return Uni
        .createFrom().item(account)
        .onItem().transform(accountService::persistAccount);
    // @formatter:on
  }

  @GET
  public Multi<Account> getAccounts() {
    return Uni.createFrom().item(accountService::getAllAccounts).onItem()
        .transformToMulti(Multi.createFrom()::iterable);
  }

  @GET
  @Path("/{name}")
  public Multi<Account> getAccountByName(@PathParam("name") String name) {
    // @formatter:off
    return Uni
        .createFrom().item(name)
        .onItem().transform(accountService::getAccounts)
        .onItem().transformToMulti(Multi.createFrom()::iterable);
    // @formatter:on
  }
}
