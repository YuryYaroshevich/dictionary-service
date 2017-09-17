package com.yra.dictionary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Dictionary {
    @JsonIgnore
    private String _id;
    private String id;
    private String name;
    private String language;
    private String user;
    private List<DictionaryEntry> entries;
    private Set<String> tags = new HashSet<>();

    public Dictionary() {}

    public Dictionary(String id, String name, String language, List<DictionaryEntry> entries) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.entries = entries;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setEntries(List<DictionaryEntry> entries) {
        this.entries = entries;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public List<DictionaryEntry> getEntries() {
        return entries;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dictionary)) return false;

        Dictionary that = (Dictionary) o;

        if (_id != null ? !_id.equals(that._id) : that._id != null) return false;
        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        if (!language.equals(that.language)) return false;
        if (!user.equals(that.user)) return false;
        if (entries != null ? !entries.equals(that.entries) : that.entries != null) return false;
        return tags != null ? tags.equals(that.tags) : that.tags == null;
    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + language.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + (entries != null ? entries.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }
}
