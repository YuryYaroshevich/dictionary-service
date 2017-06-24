package com.yra.dictionary.model.codec;

import com.yra.dictionary.model.Dictionary;
import com.yra.dictionary.model.DictionaryEntry;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class DictionaryCodec implements Codec<Dictionary> {
    private CodecRegistry codecRegistry;

    public DictionaryCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public Dictionary decode(BsonReader bsonReader, DecoderContext decoderContext) {
        bsonReader.readStartDocument();

        Dictionary dictionary = new Dictionary();
        dictionary.set_id(bsonReader.readObjectId().toString());
        dictionary.setId(bsonReader.readString("id"));
        dictionary.setName(bsonReader.readString("name"));
        dictionary.setLanguage(bsonReader.readString("language"));
        dictionary.setLink(bsonReader.readString("link"));

        Codec<DictionaryEntry> dictionaryEntryCodec = codecRegistry.get(DictionaryEntry.class);
        List<DictionaryEntry> dictionaryEntries = new ArrayList<>();
        bsonReader.readStartArray();
        while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            dictionaryEntries.add(dictionaryEntryCodec.decode(bsonReader, decoderContext));
        }
        bsonReader.readEndArray();
        dictionary.setEntries(dictionaryEntries);

        bsonReader.readEndDocument();
        return dictionary;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Dictionary dictionary, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();

        if (dictionary.get_id() != null) {
            bsonWriter.writeObjectId(new ObjectId(dictionary.get_id()));
        }
        bsonWriter.writeString("id", dictionary.getId());
        bsonWriter.writeString("name", dictionary.getName());
        bsonWriter.writeString("language", dictionary.getLanguage());
        bsonWriter.writeString("link", dictionary.getLink());

        bsonWriter.writeStartArray("entries");
        Codec<DictionaryEntry> dictionaryEntryCodec = codecRegistry.get(DictionaryEntry.class);
        dictionary.getEntries().forEach((entry) -> {
            encoderContext.encodeWithChildContext(dictionaryEntryCodec, bsonWriter, entry);
        });
        bsonWriter.writeEndArray();

        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<Dictionary> getEncoderClass() {
        return Dictionary.class;
    }
}
