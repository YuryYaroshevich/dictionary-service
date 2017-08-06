package com.yra.dictionary.model;


public class Tag {
  String name;


  public Tag(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Tag)) return false;

    Tag tag = (Tag) o;

    return name.equals(tag.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
