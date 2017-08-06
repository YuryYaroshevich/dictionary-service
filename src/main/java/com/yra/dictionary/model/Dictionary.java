package com.yra.dictionary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Dictionary {
    @JsonIgnore
    private String _id;
    private String id;
    private String name;
    private String language;
    private List<DictionaryEntry> entries;
    private List<String> tags;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dictionary)) return false;

        Dictionary that = (Dictionary) o;

        if (!_id.equals(that._id)) return false;
        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        if (!language.equals(that.language)) return false;
        if (entries != null ? !entries.equals(that.entries) : that.entries != null) return false;
        return tags != null ? tags.equals(that.tags) : that.tags == null;
    }

    @Override
    public int hashCode() {
        int result = _id.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + language.hashCode();
        result = 31 * result + (entries != null ? entries.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
