package com.yra.dictionary.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.yra.dictionary.model.Dictionary;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DictionaryDao {
    @Value("${dictionary.db.name}")
    String dictionaryDb;

    @Value("${dictionary.collection.name}")
    String dictionaryCollectionName;

    @Autowired
    MongoCollection<Dictionary> dictionaryCollection;

    public List<Dictionary> getDictionaries() {
        return dictionaryCollection.find().into(new ArrayList<>());
    }

    public List<Dictionary> getDictionaries(List<String> ids) {
        return dictionaryCollection
                .find(Filters.in("id", ids)).into(new ArrayList<>());
    }

    public Dictionary getDictionary(String id) {
        return dictionaryCollection.find(Filters.eq("id", id)).first();
    }

    public Dictionary saveDictionary(Dictionary dictionary) {
        dictionaryCollection.insertOne(dictionary);
        return dictionary;
    }

    public Dictionary updateDictionary(Dictionary dictionary) {
        dictionaryCollection.deleteOne(Filters.eq("id", dictionary.getId()));
        dictionaryCollection.insertOne(dictionary);
        return dictionary;
    }

    public void deleteDictionary(String id) {
        dictionaryCollection.deleteOne(Filters.eq("id", id));
    }

    public void deleteAll(List<String> ids) {
        dictionaryCollection.deleteMany(Filters.in("id", ids));
    }
}
