package com.yra.dictionary.controller;

import com.yra.dictionary.service.TagService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tag")
public class TagController {
  @Autowired
  private TagService tagService;

  @GetMapping
  public List<String> findTags(@RequestParam String query) {
    return tagService.find(query);
  }
}
