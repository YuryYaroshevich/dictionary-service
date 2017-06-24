package com.yra.dictionary.dao;

import com.yra.dictionary.model.Dictionary;

import java.util.List;

public interface DictionaryDao {
    List<Dictionary> getDictionaries();

    Dictionary getDictionary(String id);

    Dictionary saveDictionary(Dictionary dictionary);

    Dictionary updateDictionary(Dictionary dictionary);

    void deleteDictionary(String id);
}
