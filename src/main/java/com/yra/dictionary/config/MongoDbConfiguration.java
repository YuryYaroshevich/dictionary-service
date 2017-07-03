package com.yra.dictionary.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.yra.dictionary.model.Dictionary;
import de.undercouch.bson4jackson.BsonFactory;
import de.undercouch.bson4jackson.BsonParser;
import fr.javatic.mongo.jacksonCodec.JacksonCodecProvider;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MongoDbConfiguration {
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
        return mongoDatabase.getCollection("dictionary", Dictionary.class);
    }
}
