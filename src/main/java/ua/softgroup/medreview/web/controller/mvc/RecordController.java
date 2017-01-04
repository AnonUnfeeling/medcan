package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.service.RecordService;
import ua.softgroup.medreview.service.SearchService;
import ua.softgroup.medreview.web.form.RecordForm;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@RestController
@RequestMapping(value = "/records")
public class RecordController {

    private static final String RECORDS_VIEW = "records";
    private static final String SEARCH_RESULTS_VIEW = "results";
    private static final int NUMBER_OF_PAGES = 10;
    private final RecordService recordService;
    private final SearchService searchService;

    @Autowired
    public RecordController(RecordService recordService, SearchService searchService) {
        this.recordService = recordService;
        this.searchService = searchService;
    }

    //TODO secure this url
    @GetMapping
    public ModelAndView showPrincipalRecords() {
        return new ModelAndView(RECORDS_VIEW);
    }

    //TODO secure this url
    @GetMapping(value = "all")
    public ResponseEntity<?> showPrincipalRecords(@RequestParam int page) {
        return ResponseEntity.ok(recordService.getRecordsByAuthorities(new PageRequest(page - 1, NUMBER_OF_PAGES)));
    }

    @PostMapping(value = "/add")
    public String createRecord(@Valid RecordForm recordForm, BindingResult bindingResult) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Record record = new Record(recordForm.getTitle(), recordForm.getType(), principal);
        recordService.saveRecord(record);
        return "redirect:/" + RECORDS_VIEW;
    }

    @PostMapping(value = "/removeRecord")
    public ResponseEntity removeRecord(@RequestParam String recordTitle) {
        try {
            recordService.deleteRecordByName(recordTitle);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity showRecordById(@PathVariable("id") long id) {
        return ResponseEntity.ok(recordService.getById(id));
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<Record>> findRecordsByKeywords(@RequestParam String keyword) {
        return ResponseEntity.ok(searchService.searchByTitle(keyword, null, null));
    }

    @GetMapping(value = "/results")
    public ModelAndView showSearchResults() {
        return new ModelAndView(SEARCH_RESULTS_VIEW);
    }
}
