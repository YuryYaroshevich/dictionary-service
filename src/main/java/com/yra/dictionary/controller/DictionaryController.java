package com.yra.dictionary.controller;

import com.yra.dictionary.model.Dictionary;
import com.yra.dictionary.service.DictionaryService;
import com.yra.dictionary.service.TagService;
import java.security.Principal;
import java.util.List;
import java.util.Set;
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
@RequestMapping("/dictionary")
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private TagService tagService;

    @GetMapping
    List<Dictionary> getDictionaries(@RequestParam(required = false) List<String> ids,
                                     Principal principal) {
        String user = principal.getName();
        if (ids == null) {
            return dictionaryService.getDictionaries(user);
        } else {
            return dictionaryService.getDictionaries(ids, user);
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    Dictionary getDictionary(@PathVariable String id, Principal principal) {
        return dictionaryService.getDictionary(id, principal.getName());
    }

    @PostMapping
    Dictionary createDictionary(@RequestBody Dictionary dictionary, Principal principal) {
        dictionary.setUser(principal.getName());
        dictionary = dictionaryService.saveDictionary(dictionary);
        return dictionary;
    }

    @PutMapping
    Dictionary updateDictionary(@RequestBody Dictionary dictionary,
                                @RequestParam Set<String> newTags,
                                @RequestParam Set<String> removedTags,
                                Principal principal) {
        dictionary.setUser(principal.getName());
        dictionary = dictionaryService.updateDictionary(dictionary);
        tagService.save(newTags, removedTags, dictionary.getUser());
        return dictionary;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    ResponseEntity deleteDictionary(@PathVariable String id, Principal principal) {
        dictionaryService.deleteDictionary(id, principal.getName());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
