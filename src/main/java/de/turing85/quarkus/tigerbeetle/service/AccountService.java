package de.turing85.quarkus.tigerbeetle.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;

import com.tigerbeetle.AccountBatch;
import com.tigerbeetle.Client;
import com.tigerbeetle.CreateAccountResultBatch;
import com.tigerbeetle.QueryFilter;
import com.tigerbeetle.UInt128;
import de.turing85.quarkus.tigerbeetle.model.Account;
import de.turing85.quarkus.tigerbeetle.model.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class AccountService {
  private final Client client;

  public Account persistAccount(Account account) {
    String accountName = account.getName();
    validateName(accountName);
    AccountBatch accountBatch = new AccountBatch(1);
    accountBatch.add();
    accountBatch.setId(account.getId().getLeastSignificantBits(),
        account.getId().getMostSignificantBits());
    byte[] accountNameBytes = accountNameTo16BytesArray(accountName);
    accountBatch.setUserData128(accountNameBytes);
    accountBatch.setUserData64(0L);
    accountBatch.setUserData32(0);
    accountBatch.setLedger(Optional.ofNullable(account.getCurrency()).orElse(Currency.EUR).id);
    accountBatch.setCode(1);
    CreateAccountResultBatch errors = client.createAccounts(accountBatch);
    if (errors.getLength() > 0) {
      List<String> errorMessages = new ArrayList<>();
      while (errors.next()) {
        errorMessages.add(errors.getResult().name());
      }
      throw TigerbeetleException.accountCreateFailed(Map.of(accountName, errorMessages));
    }
    return account;
  }

  private static byte[] accountNameTo16BytesArray(String accountName) {
    byte[] accountNameBytes = accountName.getBytes(StandardCharsets.UTF_8);
    byte[] accountName16Bytes = new byte[16];
    System.arraycopy(accountNameBytes, 0, accountName16Bytes, 0, accountNameBytes.length);
    return accountName16Bytes;
  }

  private static void validateName(String name) {
    if (name.getBytes(StandardCharsets.UTF_8).length > 16) {
      throw new IllegalArgumentException("Account name too long, must be at most 128 bytes long");
    }
  }

  public List<Account> getAllAccounts() {
    return getAllAccounts(null);
  }

  public List<Account> getAllAccounts(Integer limit) {
    QueryFilter queryFilter = new QueryFilter();
    queryFilter.setLimit(Optional.ofNullable(limit).orElse(Integer.MAX_VALUE));
    return accountBatchToAccounts(client.queryAccounts(queryFilter));
  }

  public List<Account> getAccounts(String name) {
    validateName(name);
    QueryFilter queryFilter = new QueryFilter();
    queryFilter.setUserData128(accountNameTo16BytesArray(name));
    queryFilter.setLimit(10);
    return accountBatchToAccounts(client.queryAccounts(queryFilter));
  }

  private static List<Account> accountBatchToAccounts(AccountBatch batch) {
    List<Account> accounts = new ArrayList<>();
    while (batch.next()) {
      UUID id = UInt128.asUUID(batch.getId());
      String userName = new String(batch.getUserData128(), StandardCharsets.UTF_8);
      int currencyId = batch.getLedger();
      Currency currency = Currency.of(currencyId).orElseThrow(() -> new IllegalStateException(
          "Currency with id \"%d\" does not exist".formatted(currencyId)));
      accounts.add(Account.builder().id(id).name(userName).currency(currency).build());
    }
    return accounts;
  }
}
