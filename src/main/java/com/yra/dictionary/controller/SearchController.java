package com.yra.dictionary.controller;

import com.yra.dictionary.model.Dictionary;
import com.yra.dictionary.service.SearchService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dictionary/search")
public class SearchController {
  @Autowired
  private SearchService searchService;

  @GetMapping
  public List<Dictionary> search(@RequestParam String searchText,
                                 @RequestParam SearchType searchType) {
    return searchService.search(searchText, searchType);
  }
}
