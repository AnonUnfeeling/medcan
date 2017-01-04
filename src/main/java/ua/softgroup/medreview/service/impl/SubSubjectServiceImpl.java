package ua.softgroup.medreview.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.softgroup.medreview.persistent.entity.SubSubject;
import ua.softgroup.medreview.persistent.repository.SubSubjectRepository;
import ua.softgroup.medreview.service.SubSubjectService;

import java.util.List;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
@Service
public class SubSubjectServiceImpl implements SubSubjectService {

    private final SubSubjectRepository subSubjectRepository;

    @Autowired
    public SubSubjectServiceImpl(SubSubjectRepository subSubjectRepository) {
        this.subSubjectRepository = subSubjectRepository;
    }

    @Override
    public List<SubSubject> getAll() {
        return (List<SubSubject>) subSubjectRepository.findAll();
    }
}
