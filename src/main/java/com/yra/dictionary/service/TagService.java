package com.yra.dictionary.service;

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
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {
  @Autowired
  private MongoCollection tagCollection;

  public Optional<BulkWriteResult> save(List<String> newTags, List<String> removedTags) {
    List<WriteModel> updates = newTags.stream().map(tag -> new UpdateOneModel(
            Filters.eq("name", tag),
            combine(set("name", tag), inc("count", 1)),
            new UpdateOptions().upsert(true))).collect(toList());
    List<WriteModel> decrementUpdates = removedTags.stream().map(tag -> new UpdateOneModel(
            Filters.eq("name", tag),
            inc("count", -1),
            new UpdateOptions().upsert(true))).collect(toList());
    updates.addAll(decrementUpdates);

    if (updates.isEmpty()) {
      return Optional.empty();
    }

    updates.add(new DeleteManyModel(Filters.eq("count", 0)));
    return Optional.of(tagCollection.bulkWrite(updates));
  }

  public List<String> find(String query) {
    return (List<String>) tagCollection
            .find(Filters.text(query)).into(new ArrayList<>());
  }
}
