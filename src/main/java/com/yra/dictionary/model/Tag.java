package com.yra.dictionary.model;


public class Tag {
  String name;
  String user;

  public Tag(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getUser() {
    return user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Tag)) return false;

    Tag tag = (Tag) o;

    if (!name.equals(tag.name)) return false;
    return user.equals(tag.user);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + user.hashCode();
    return result;
  }
}
