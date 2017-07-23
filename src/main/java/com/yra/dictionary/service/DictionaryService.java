package com.yra.dictionary.service;

import com.yra.dictionary.model.Dictionary;

import java.util.List;

public interface DictionaryService {
    List<Dictionary> getDictionaries();

    List<Dictionary> getDictionaries(List<String> ids);

    Dictionary getDictionary(String id);

    Dictionary saveDictionary(Dictionary dictionary);

    Dictionary updateDictionary(Dictionary dictionary);

    void deleteDictionary(String id);
}
