package com.yra.dictionary.model;


public class Account {

  private String email;
  private String password;

  public Account() {
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Account)) return false;

    Account account = (Account) o;

    if (!email.equals(account.email)) return false;
    return password.equals(account.password);
  }

  @Override
  public int hashCode() {
    int result = email.hashCode();
    result = 31 * result + password.hashCode();
    return result;
  }
}
