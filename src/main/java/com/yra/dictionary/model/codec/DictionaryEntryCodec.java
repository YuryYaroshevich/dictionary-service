package com.yra.dictionary.model.codec;

import com.yra.dictionary.model.DictionaryEntry;
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
            example = "";
        bsonReader.readStartDocument();
        try {
            phrase = bsonReader.readString("phrase");
            translation = bsonReader.readString("translation");
            example = bsonReader.readString("example");
        } catch (BsonInvalidOperationException e) {
            //System.out.println(e);
        }
        bsonReader.readEndDocument();

        return new DictionaryEntry(phrase, translation, example);
    }

    @Override
    public void encode(BsonWriter bsonWriter, DictionaryEntry dictionaryEntry, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeString("phrase", dictionaryEntry.getPhrase());
        bsonWriter.writeString("translation", dictionaryEntry.getTranslation());
        bsonWriter.writeString("example", dictionaryEntry.getExample());
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<DictionaryEntry> getEncoderClass() {
        return DictionaryEntry.class;
    }
}
