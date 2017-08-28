package com.yra.dictionary.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.yra.dictionary.model.Account;
import com.yra.dictionary.model.Dictionary;
import com.yra.dictionary.model.Tag;
import de.undercouch.bson4jackson.BsonFactory;
import de.undercouch.bson4jackson.BsonParser;
import fr.javatic.mongo.jacksonCodec.JacksonCodecProvider;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoDbConfig {
    @Value("${mongodb.host}")
    String mongoHost;

    @Value("${mongodb.port}")
    int mongoPort;

    @Bean
    MongoClient mongoClient() {
        MongoClientOptions options = MongoClientOptions.builder().build();
        return new MongoClient(new ServerAddress(mongoHost, mongoPort), options);
    }

    @Bean
    public CodecRegistry codecRegistry() {
        final CodecRegistry defaultCodecRegistry = MongoClient.getDefaultCodecRegistry();

        final BsonFactory bsonFactory = new BsonFactory();
        bsonFactory.enable(BsonParser.Feature.HONOR_DOCUMENT_LENGTH);
        ObjectMapper mongoObjectMapper = new ObjectMapper(bsonFactory);

        final CodecProvider jacksonCodecProvider = new JacksonCodecProvider(mongoObjectMapper);
        final CodecRegistry jacksonCodecRegistry = CodecRegistries.fromProviders(jacksonCodecProvider);
        return CodecRegistries.fromRegistries(defaultCodecRegistry, jacksonCodecRegistry);
    }

    @Bean
    public MongoDatabase mongoDatabase(
            final MongoClient mongoClient, final CodecRegistry codecRegistry) {
        return mongoClient.getDatabase("dictionary").withCodecRegistry(codecRegistry);
    }

    @Bean
    public MongoCollection<Dictionary> dictionaryCollection(MongoDatabase mongoDatabase) {
        MongoCollection<Dictionary> dictionaryCollection = mongoDatabase
                .getCollection("dictionary", Dictionary.class);
        dictionaryCollection.createIndex(Indexes.text("name"),
                new IndexOptions().languageOverride("lang_of_document"));
        dictionaryCollection.createIndex(Indexes.ascending("tags"));
        return dictionaryCollection;
    }

    @Bean
    public MongoCollection<Tag> tagCollection(MongoDatabase mongoDatabase) {
        MongoCollection<Tag> tagCollection = mongoDatabase.getCollection("tag", Tag.class);
        tagCollection.createIndex(Indexes.text("name"),
                new IndexOptions().unique(true));
        return tagCollection;
    }

    @Bean
    public MongoCollection<Account> accountCollection(MongoDatabase mongoDatabase) {
        MongoCollection<Account> accountCollection = mongoDatabase.getCollection("account", Account.class);
        accountCollection.createIndex(Indexes.ascending("email"),
                new IndexOptions().unique(true));
        return accountCollection;
    }
}
