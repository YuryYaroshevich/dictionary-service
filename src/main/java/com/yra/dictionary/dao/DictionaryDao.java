package com.yra.dictionary.dao;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.yra.dictionary.model.Dictionary;
import java.util.ArrayList;
import java.util.List;
import org.bson.conversions.Bson;
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

    public List<Dictionary> getDictionaries(String user) {
        return dictionaryCollection.find(eq("user", user))
                .into(new ArrayList<>());
    }

    public List<Dictionary> getDictionaries(List<String> ids, String user) {
        return dictionaryCollection
                .find(dictionaryIdsAndUser(ids, user))
                .into(new ArrayList<>());
    }

    public Dictionary getDictionary(String id, String user) {
        return dictionaryCollection.find(
                dictionaryIdAndUser(id, user)).first();
    }

    public Dictionary saveDictionary(Dictionary dictionary) {
        dictionaryCollection.insertOne(dictionary);
        return dictionary;
    }

    public Dictionary updateDictionary(Dictionary dictionary) {
        String dictionaryId = dictionary.getId();
        String user = dictionary.getUser();
        dictionaryCollection.deleteOne(
                dictionaryIdAndUser(dictionaryId, user));
        dictionaryCollection.insertOne(dictionary);
        return dictionary;
    }

    public Dictionary deleteDictionary(String id, String user) {
        return dictionaryCollection.findOneAndDelete(dictionaryIdAndUser(id, user));
    }

    public void deleteAll(List<String> ids, String user) {
        dictionaryCollection.deleteMany(dictionaryIdsAndUser(ids, user));
    }

    private static Bson dictionaryIdAndUser(String id, String user) {
        return and(eq("id", id),eq("user", user));
    }

    private static Bson dictionaryIdsAndUser(List<String> ids, String user) {
        return and(in("id", ids),eq("user", user));
    }
}