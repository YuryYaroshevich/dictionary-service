package com.yra.dictionary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Dictionary {
    @JsonIgnore
    private String _id;
    private String id;
    private String name;
    private String language;
    private String link;
    private List<DictionaryEntry> entries;

    public Dictionary() {}

    public Dictionary(String id, String name, String language, String link, List<DictionaryEntry> entries) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.link = link;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dictionary that = (Dictionary) o;

        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        if (!language.equals(that.language)) return false;
        return entries.equals(that.entries);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + language.hashCode();
        result = 31 * result + entries.hashCode();
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
}
