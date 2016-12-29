package ua.softgroup.medreview.web.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.persistent.repository.RecordRepository;
import ua.softgroup.medreview.web.form.RecordForm;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@Controller
public class RecordController {

    private static final String RECORDS_VIEW = "records";
    private static final String ADD_RECORD_VIEW = "addRecord";
    private static final String RECORDS_ATTRIBUTE = "records";

    private final RecordRepository recordRepository;

    @Autowired
    public RecordController(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    //TODO secure this url
    @GetMapping(value = "/records")
    public ModelAndView showPrincipalRecords() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ModelAndView(RECORDS_VIEW, RECORDS_ATTRIBUTE, recordRepository.findByAuthor(principal));
    }

    //TODO secure this url for admin only
    @GetMapping(value = "/records/all")
    public ModelAndView showAllRecords() {
        List<Record> records = new ArrayList<>();
        recordRepository.findAll().forEach(records::add);
        return new ModelAndView(RECORDS_VIEW, RECORDS_ATTRIBUTE, records);
    }

    @PostMapping(value = "/records/add")
    public String createRecord(@ModelAttribute("recordForm") @Valid RecordForm recordForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ADD_RECORD_VIEW;
        }
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Record record = new Record(recordForm.getTitle(), recordForm.getType(), principal);
        recordRepository.save(record);
        return "redirect:/" + RECORDS_VIEW;
    }

}
