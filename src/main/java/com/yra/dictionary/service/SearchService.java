package com.yra.dictionary.service;


import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Indexes.text;
import static com.yra.dictionary.controller.SearchType.name;
import static com.yra.dictionary.controller.SearchType.tag;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.yra.dictionary.controller.SearchType;
import com.yra.dictionary.model.Dictionary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
  @Autowired
  private MongoCollection<Dictionary> dictionaryCollection;

  private final Map<SearchType, Function<String, Bson>> filterCreatorMapping;

  public SearchService() {
    filterCreatorMapping = new HashMap<>();
    filterCreatorMapping.put(tag,
            (searchText) -> eq("tags", searchText));
    filterCreatorMapping.put(name,
            (searchText) -> eq("name", searchText));
  }

  public List<Dictionary> search(String searchText,
                                 SearchType searchType, String user) {
    Bson searchFilter = filterCreatorMapping.get(searchType).apply(searchText);
    return dictionaryCollection.find(and(searchFilter, eq("user", user)))
            .into(new ArrayList<>());
  }
}
