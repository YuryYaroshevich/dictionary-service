package com.yra.dictionary.controller;

import com.yra.dictionary.model.Dictionary;
import com.yra.dictionary.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/dictionary")
public class DictionaryBuilderController {
  @Autowired
  DictionaryService dictionaryService;

  @RequestMapping(path = "/extract", method = RequestMethod.POST, consumes="application/json")
  public Dictionary extractDictionary(@RequestBody ExtractDictionaryRequest extractRequest) {
    dictionaryService.updateDictionary(extractRequest.oldDictionary);
    return dictionaryService.saveDictionary(extractRequest.extractedDictionary);
  }
}

class ExtractDictionaryRequest {
  Dictionary extractedDictionary;
  Dictionary oldDictionary;

  public void setExtractedDictionary(Dictionary extractedDictionary) {
    this.extractedDictionary = extractedDictionary;
  }

  public void setOldDictionary(Dictionary oldDictionary) {
    this.oldDictionary = oldDictionary;
  }
}
