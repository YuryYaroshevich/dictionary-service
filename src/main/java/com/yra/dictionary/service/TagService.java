package com.yra.dictionary.service;

import static com.mongodb.client.model.Updates.inc;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import com.yra.dictionary.model.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {
  @Autowired
  private MongoCollection<Tag> tagCollection;

  public UpdateResult save(List<String> tags) {
    return tagCollection.updateMany(Filters.in("name", tags),
            inc("count", 1),
            new UpdateOptions().upsert(true));
  }
}
