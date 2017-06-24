package com.yra.dictionary.model.codec;

import com.yra.dictionary.model.DictionaryEntry;
import org.bson.AbstractBsonReader;
import org.bson.BsonInvalidOperationException;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class DictionaryEntryCodec implements Codec<DictionaryEntry> {
    @Override
    public DictionaryEntry decode(BsonReader bsonReader, DecoderContext decoderContext) {
        String phrase = "",
            translation = "",
            link = "";
        bsonReader.readStartDocument();
        try {
            phrase = bsonReader.readString("phrase");
            translation = bsonReader.readString("translation");
            link = bsonReader.readString("link");
        } catch (BsonInvalidOperationException e) {
            //System.out.println(e);
        }
        bsonReader.readEndDocument();

        return new DictionaryEntry(phrase, translation, link);
    }

    @Override
    public void encode(BsonWriter bsonWriter, DictionaryEntry dictionaryEntry, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeString("phrase", dictionaryEntry.getPhrase());
        bsonWriter.writeString("translation", dictionaryEntry.getTranslation());
        bsonWriter.writeString("link", dictionaryEntry.getLink());
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<DictionaryEntry> getEncoderClass() {
        return DictionaryEntry.class;
    }
}
