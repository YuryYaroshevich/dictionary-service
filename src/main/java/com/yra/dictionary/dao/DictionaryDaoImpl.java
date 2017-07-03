package com.yra.dictionary.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.yra.dictionary.model.Dictionary;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DictionaryDaoImpl implements DictionaryDao {
    @Value("${dictionary.db.name}")
    String dictionaryDb;

    @Value("${dictionary.collection.name}")
    String dictionaryCollectionName;

    @Autowired
    MongoClient mongoClient;

    @Autowired
    MongoCollection<Dictionary> dictionaryCollection;

    @Override
    public List<Dictionary> getDictionaries() {
        //MongoCollection<Dictionary> dictionaryCollection = getDictionaryCollection();
        return dictionaryCollection.find().into(new ArrayList<>());
    }

    @Override
    public Dictionary getDictionary(String id) {
        //MongoCollection<Dictionary> dictionaryCollection = getDictionaryCollection();
        return dictionaryCollection.find(Filters.eq("id", id)).first();
    }

    @Override
    public Dictionary saveDictionary(Dictionary dictionary) {
        //MongoCollection<Dictionary> dictionaryCollection = getDictionaryCollection();
        dictionaryCollection.insertOne(dictionary);
        return dictionary;
    }

    @Override
    public Dictionary updateDictionary(Dictionary dictionary) {
        //MongoCollection<Dictionary> dictionaryCollection = getDictionaryCollection();
        dictionaryCollection.deleteOne(Filters.eq("id", dictionary.getId()));
        dictionaryCollection.insertOne(dictionary);
        return dictionary;
    }

    @Override
    public void deleteDictionary(String id) {
        //MongoCollection<Dictionary> dictionaryCollection = getDictionaryCollection();
        dictionaryCollection.deleteOne(Filters.eq("id", id));
    }
/*
    private MongoCollection getDictionaryCollection() {
        MongoDatabase db = mongoClient.getDatabase(dictionaryDb);
        return db.getCollection(dictionaryCollectionName, Dictionary.class);
    }*/
}
