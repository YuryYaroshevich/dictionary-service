package com.yra.dictionary.controller;

import com.yra.dictionary.model.Dictionary;
import com.yra.dictionary.service.DictionaryService;
import com.yra.dictionary.service.TagService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/dictionary")
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private TagService tagService;

    @GetMapping
    List<Dictionary> getDictionaries(@RequestParam(required = false) List<String> ids) {
        if (ids == null) {
            return dictionaryService.getDictionaries();
        } else {
            return dictionaryService.getDictionaries(ids);
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    Dictionary getDictionary(@PathVariable String id) {
        return dictionaryService.getDictionary(id);
    }

    @PostMapping
    Dictionary createDictionary(@RequestBody Dictionary dictionary) {
        dictionary = dictionaryService.saveDictionary(dictionary);
        //tagService.save(dictionary.getTags());
        return dictionary;
    }

    @PutMapping
    Dictionary updateDictionary(@RequestBody Dictionary dictionary,
                                @RequestParam List<String> newTags,
                                @RequestParam List<String> removedTags) {
        dictionary = dictionaryService.updateDictionary(dictionary);
        tagService.save(newTags, removedTags);
        return dictionary;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    ResponseEntity deleteDictionary(@PathVariable String id) {
        dictionaryService.deleteDictionary(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
