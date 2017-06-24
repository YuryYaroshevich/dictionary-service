package com.yra.dictionary.model.codec;

import com.yra.dictionary.model.Dictionary;
import com.yra.dictionary.model.DictionaryEntry;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class DictionaryCodecProvider implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry codecRegistry) {
        if (clazz == Dictionary.class) {
            return (Codec<T>) new DictionaryCodec(codecRegistry);
        } else if (clazz == DictionaryEntry.class) {
            return (Codec<T>) new DictionaryEntryCodec();
        }
        return null;
    }
}
