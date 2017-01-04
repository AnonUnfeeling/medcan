package ua.softgroup.medreview.web.controller.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.persistent.repository.RecordRepository;
import ua.softgroup.medreview.service.RecordService;
import ua.softgroup.medreview.service.UserService;
import ua.softgroup.medreview.service.impl.AuthenticationServiceImpl;
import ua.softgroup.medreview.web.form.RecordForm;

import javax.validation.Valid;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@RestController
@RequestMapping(value = "/records")
public class RecordController {

    private static final String RECORDS_VIEW = "records";
    private RecordService recordService;

    @Autowired
    private UserService userService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    //TODO secure this url
    @GetMapping
    public ModelAndView showPrincipalRecords() {
        return new ModelAndView(RECORDS_VIEW);
    }

    //TODO secure this url
    @GetMapping(value = "all")
    public ResponseEntity<?> showPrincipalRecords(@RequestParam int page) {
        return ResponseEntity.ok(recordService.getRecordsByAuthorities(new PageRequest(page - 1, 10)));
    }

    @PostMapping(value = "/add")
    public String createRecord(@Valid RecordForm recordForm, BindingResult bindingResult) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Record record = new Record(recordForm.getTitle(), recordForm.getType(), principal);
        recordService.saveRecord(record);
        return "redirect:/" + RECORDS_VIEW;
    }

    @PostMapping(value = "/removeRecord")
    public ResponseEntity removeRecord(@RequestParam String recordTitle){
        try {
            recordService.deleteRecordByName(recordTitle);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(null);
        }
        return ResponseEntity.ok(null);
    }

    @PostMapping(value = "/getRecordByUser")
    public ResponseEntity<?> getRecordByUser(@RequestParam String userName, @RequestParam int page){
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
}
