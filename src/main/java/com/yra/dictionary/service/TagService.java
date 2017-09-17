package com.yra.dictionary.service;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Indexes.text;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.inc;
import static com.mongodb.client.model.Updates.set;
import static java.util.stream.Collectors.toList;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.DeleteManyModel;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.TextSearchOptions;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {
  @Autowired
  private MongoCollection tagCollection;

  public Optional<BulkWriteResult> save(Set<String> newTags, Set<String> removedTags, String user) {
    List<WriteModel> updates = newTags.stream().map(tag -> new UpdateOneModel(
            nameAndUser(tag, user),
            combine(set("name", tag), inc("count", 1)),
            new UpdateOptions().upsert(true))).collect(toList());
    List<WriteModel> decrementUpdates = removedTags.stream().map(tag -> new UpdateOneModel(
            nameAndUser(tag, user),
            inc("count", -1),
            new UpdateOptions().upsert(true))).collect(toList());
    updates.addAll(decrementUpdates);
    if (updates.isEmpty()) {
      return Optional.empty();
    }

    updates.add(new DeleteManyModel(eq("count", 0)));
    return Optional.of(tagCollection.bulkWrite(updates));
  }

  public List<String> find(String query) {
    return (List<String>) tagCollection
            .find(text(query)).into(new ArrayList<>());
  }

  private static Bson nameAndUser(String name, String user) {
    return and(eq("name", name), eq("user", user));
  }
}
