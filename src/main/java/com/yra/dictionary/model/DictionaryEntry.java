package com.yra.dictionary.model;

public class DictionaryEntry {
    String phrase;
    String translation;
    String example;

    DictionaryEntry() {}

    public DictionaryEntry(String phrase, String translation, String example) {
        this.phrase = phrase;
        this.translation = translation;
        this.example = example;
    }

    public String getPhrase() {
        return phrase;
    }

    public String getTranslation() {
        return translation;
    }

    public String getExample() {
        return example;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DictionaryEntry that = (DictionaryEntry) o;

        if (!phrase.equals(that.phrase)) return false;
        return translation.equals(that.translation);

    }

    @Override
    public int hashCode() {
        int result = phrase.hashCode();
        result = 31 * result + translation.hashCode();
        return result;
    }
}
