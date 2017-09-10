package com.yra.dictionary.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class Account {
  @JsonIgnore
  private String _id;

  private String email;
  private String password;

  public Account() {
  }

  public String get_id() {
    return _id;
  }

  public void set_id(String _id) {
    this._id = _id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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
