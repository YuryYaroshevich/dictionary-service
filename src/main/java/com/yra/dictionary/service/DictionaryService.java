package com.yra.dictionary.service;

import com.yra.dictionary.dao.DictionaryDao;
import com.yra.dictionary.model.Dictionary;
import com.yra.dictionary.model.DictionaryEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictionaryService {
    @Autowired
    DictionaryDao dictionaryDao;

    @Autowired
    TagService tagService;

    public List<Dictionary> getDictionaries(String user) {
        return dictionaryDao.getDictionaries(user);
    }

    public List<Dictionary> getDictionaries(List<String> ids, String user) {
        return dictionaryDao.getDictionaries(ids, user);
    }

    public Dictionary getDictionary(String id, String user) {
        return dictionaryDao.getDictionary(id, user);
    }

    public Dictionary saveDictionary(Dictionary dictionary) {
        dictionary.setId(UUID.randomUUID().toString());
        return dictionaryDao.saveDictionary(dictionary);
    }

    public Dictionary updateDictionary(Dictionary dictionary) {
        return dictionaryDao.updateDictionary(dictionary);
    }

    public void deleteDictionary(String id, String user) {
        Dictionary dictionary = dictionaryDao.deleteDictionary(id, user);
        tagService.save(Collections.emptySet(), dictionary.getTags(), user);
    }

    public void mergeDictionaries(List<String> ids, String name, String user) {
        List<Dictionary> dictionaries = dictionaryDao.getDictionaries(ids, user);
        List<DictionaryEntry> mergedEntries = dictionaries.stream()
                .map(dict -> dict.getEntries())
                .reduce(new ArrayList<>(), (allEntries, entries) -> {
                    allEntries.addAll(entries);
                    return allEntries;
                });
        Set<String> tags = dictionaries.stream()
                .map(Dictionary::getTags)
                .filter(dictTags -> dictTags != null)
                .reduce(new HashSet<>(), (allTags, dictTags) -> {
                    allTags.addAll(dictTags);
                    return allTags;
                });
        Dictionary newDictionary = new Dictionary(
                UUID.randomUUID().toString(),
                name,
                dictionaries.get(0).getLanguage(),
                mergedEntries);
        newDictionary.setTags(tags);
        newDictionary.setUser(user);
        dictionaryDao.saveDictionary(newDictionary);
        dictionaryDao.deleteAll(ids, user);
    }
}
