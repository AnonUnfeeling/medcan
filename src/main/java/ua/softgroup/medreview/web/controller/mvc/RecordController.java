package ua.softgroup.medreview.web.controller.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.persistent.repository.RecordRepository;
import ua.softgroup.medreview.service.impl.AuthenticationServiceImpl;
import ua.softgroup.medreview.web.form.RecordForm;

import javax.validation.Valid;

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
    private AuthenticationServiceImpl authenticationService;

    @Autowired
    public RecordController(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    //TODO secure this url
    @GetMapping(value = "/record")
    public ModelAndView showPrincipalRecords() {
        return new ModelAndView(RECORDS_VIEW);
    }

    //TODO secure this url
    @GetMapping(value = "/records")
    public @ResponseBody String showPrincipalRecords(@RequestParam int page) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(recordRepository.findByAuthor(authenticationService.getPrincipal(),new PageRequest(page, 10)));
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    //TODO secure this url for admin only
    @GetMapping(value = "/records/all")
    public ModelAndView showAllRecords() {
        return new ModelAndView(RECORDS_VIEW);
    }

    //TODO secure this url for admin only
    @GetMapping(value = "/records/alls")
    public @ResponseBody String showAllRecords(@RequestParam int page) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(recordRepository.findAll(new PageRequest(page, 10)));
        } catch (JsonProcessingException e) {
            return "";
        }
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
