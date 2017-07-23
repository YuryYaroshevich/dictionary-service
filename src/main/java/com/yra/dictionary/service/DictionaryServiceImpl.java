package com.yra.dictionary.service;

import com.yra.dictionary.dao.DictionaryDao;
import com.yra.dictionary.model.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    DictionaryDao dictionaryDao;

    @Override
    public List<Dictionary> getDictionaries() {
        return dictionaryDao.getDictionaries();
    }

    @Override
    public List<Dictionary> getDictionaries(List<String> ids) {
        return dictionaryDao.getDictionaries(ids);
    }

    @Override
    public Dictionary getDictionary(String id) {
        return dictionaryDao.getDictionary(id);
    }

    @Override
    public Dictionary saveDictionary(Dictionary dictionary) {
        dictionary.setId(UUID.randomUUID().toString());
        return dictionaryDao.saveDictionary(dictionary);
    }

    @Override
    public Dictionary updateDictionary(Dictionary dictionary) {
        return dictionaryDao.updateDictionary(dictionary);
    }

    @Override
    public void deleteDictionary(String id) {
        dictionaryDao.deleteDictionary(id);
    }
}
