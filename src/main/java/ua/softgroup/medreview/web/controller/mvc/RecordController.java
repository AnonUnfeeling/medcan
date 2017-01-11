package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ua.softgroup.medreview.persistent.entity.NoteStatus;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.entity.RecordType;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.service.AuthenticationService;
import ua.softgroup.medreview.service.RecordService;
import ua.softgroup.medreview.service.SearchService;
import ua.softgroup.medreview.service.UserService;
import ua.softgroup.medreview.web.form.RecordForm;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@RestController
@RequestMapping(value = "/records")
public class RecordController {

    private static final String RECORDS_VIEW = "records";
    private AuthenticationService authenticationService;
    private UserService userService;
    private static final String SEARCH_RESULTS_VIEW = "searchResult";
    private static final int NUMBER_OF_PAGES = 10;
    private final RecordService recordService;
    private final SearchService searchService;

    @Autowired
    public RecordController(RecordService recordService, SearchService searchService, UserService userService, AuthenticationService authenticationService) {
        this.recordService = recordService;
        this.searchService = searchService;
        this.userService = userService;
        this.authenticationService = authenticationService;
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
    public ResponseEntity createRecord(@Valid RecordForm recordForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
        User principal = authenticationService.getPrincipal();
        Record record = new Record(recordForm.getTitle(), recordForm.getType(), principal);
        record.setCountry(recordForm.getCountry());
        record.setStatus(NoteStatus.NEW.getStatus());
        record.setEndConclusion("");
        record.setEndDescription("");
        recordService.saveRecord(record);
        return ResponseEntity.status(HttpStatus.CREATED).body("Record '"+record.getTitle()+"' was created.");
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

    @PostMapping(value = "/getRecordByUser")
    public ResponseEntity<?> getRecordByUser(@RequestParam String userName, @RequestParam int page) {
        try {
            return ResponseEntity.ok(recordService.getByAuthor(userService.findUserByLogin(userName), new PageRequest(page - 1, 10)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
        }
    }

    @GetMapping(value = "/record")
    public ModelAndView getRecordView(@RequestParam String username) {
        return new ModelAndView(RECORDS_VIEW);
    }

    @PostMapping(value = "/getRecord")
    private boolean getRecord(@RequestParam String recordName) {
        return recordService.getByTitle(recordName).getAuthor().getLogin().equals(authenticationService.getPrincipal().getLogin());
    }

    @RequestMapping(value = "getTypeRecord")
    @ResponseBody
    public ResponseEntity<List<String>> getAllRoles() {
        return ResponseEntity.ok(Arrays.stream(RecordType.values())
                .map(RecordType::getType)
                .collect(Collectors.toList()));
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<Record>> findRecordsByKeywords(@RequestParam String keyword) {
        return ResponseEntity.ok(searchService.searchByTitle(keyword, null, null));
    }

    @GetMapping(value = "/results")
    public ModelAndView showSearchResults() {
        return new ModelAndView(SEARCH_RESULTS_VIEW);
    }

    @PostMapping(value = "/edit")
    public ResponseEntity edit(@Valid RecordForm recordForm, BindingResult bindingResult) {
        Record record = recordService.getByTitle(recordForm.getTitle());
        record.setTitle(recordForm.getTitle());
        record.setAuthor(record.getAuthor());
        record.setCountry(record.getCountry());
        record.setEndDescription(recordForm.getEndDescription());
        record.setEndConclusion(recordForm.getEndConclusion());
        record.setStatus(recordForm.getStatus());
        record.setCreationDate(record.getCreationDate());
        record.setNotes(record.getNotes());
        record.setType(record.getType());
        recordService.editRecord(record);
        return ResponseEntity.ok("User was changed successfully.");
    }

    @PostMapping(value = "/editRecord")
    public ResponseEntity editRecord(@Valid RecordForm recordForm, BindingResult bindingResult) {
        Record record = recordService.getByTitle(recordForm.getPreRecordName());
        record.setTitle(recordForm.getTitle());
        record.setAuthor(record.getAuthor());
        record.setCountry(recordForm.getCountry());
        record.setEndDescription(recordForm.getEndDescription());
        record.setEndConclusion(recordForm.getEndConclusion());
        record.setStatus(NoteStatus.IN_REVIEW.getStatus());
        record.setCreationDate(record.getCreationDate());
        record.setNotes(record.getNotes());
        record.setType(recordForm.getType());
        recordService.editRecord(record);
        return ResponseEntity.ok("User was changed successfully.");
    }

    @RequestMapping(value = "/getRecordDetails")
    public ResponseEntity<?> getRecordDeatils(@RequestParam String recordName){
        return ResponseEntity.ok(recordService.getByTitle(recordName));
    }
}
