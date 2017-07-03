package com.yra.dictionary.controller;

import com.yra.dictionary.model.Dictionary;
import com.yra.dictionary.service.DictionaryService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping(path = "/dictionary", method = RequestMethod.GET)
    List<Dictionary> getDictionaries() {
        List<Dictionary> dictionaries = dictionaryService.getDictionaries();
        return dictionaries;
    }

    @RequestMapping(path = "/dictionary/{id}", method = RequestMethod.GET)
    Dictionary getDictionary(@PathVariable String id) {
        return dictionaryService.getDictionary(id);
    }

    @RequestMapping(path = "/dictionary", method = RequestMethod.POST)
    Dictionary createDictionary(@RequestBody Dictionary dictionary) {
        return dictionaryService.saveDictionary(dictionary);
    }

    @RequestMapping(path = "/dictionary", method = RequestMethod.PUT)
    Dictionary updateDictionary(@RequestBody Dictionary dictionary) {
        return dictionaryService.updateDictionary(dictionary);
    }

    @RequestMapping(path = "/dictionary/{id}", method = RequestMethod.DELETE)
    ResponseEntity deleteDictionary(@PathVariable String id) {
        dictionaryService.deleteDictionary(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
