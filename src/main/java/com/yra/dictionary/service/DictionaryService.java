package com.yra.dictionary.service;

import com.yra.dictionary.dao.DictionaryDao;
import com.yra.dictionary.model.Dictionary;
import com.yra.dictionary.model.DictionaryEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictionaryService {
    @Autowired
    DictionaryDao dictionaryDao;

    public List<Dictionary> getDictionaries() {
        return dictionaryDao.getDictionaries();
    }

    public List<Dictionary> getDictionaries(List<String> ids) {
        return dictionaryDao.getDictionaries(ids);
    }

    public Dictionary getDictionary(String id) {
        return dictionaryDao.getDictionary(id);
    }

    public Dictionary saveDictionary(Dictionary dictionary) {
        dictionary.setId(UUID.randomUUID().toString());
        return dictionaryDao.saveDictionary(dictionary);
    }

    public Dictionary updateDictionary(Dictionary dictionary) {
        return dictionaryDao.updateDictionary(dictionary);
    }

    public void deleteDictionary(String id) {
        dictionaryDao.deleteDictionary(id);
    }

    public void mergeDictionaries(List<String> ids, String name) {
        List<Dictionary> dictionaries = dictionaryDao.getDictionaries(ids);
        List<DictionaryEntry> mergedEntries = dictionaries.stream()
                .map(dict -> dict.getEntries())
                .reduce(new ArrayList<>(), (allEntries, entries) -> {
                    allEntries.addAll(entries);
                    return allEntries;
                });
        Dictionary newDictionary = new Dictionary(
                UUID.randomUUID().toString(),
                dictionaries.get(0).getName(),
                dictionaries.get(0).getLanguage(),
                mergedEntries);
        dictionaryDao.saveDictionary(newDictionary);
        dictionaryDao.deleteAll(ids);
    }
}
