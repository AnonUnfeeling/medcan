package ua.softgroup.medreview.service;

import ua.softgroup.medreview.persistent.entity.Subject;

import java.util.List;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public interface SubjectService {

    List<Subject> getAll();
}
