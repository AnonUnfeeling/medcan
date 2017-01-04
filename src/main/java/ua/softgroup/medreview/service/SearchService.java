package ua.softgroup.medreview.service;

import ua.softgroup.medreview.persistent.entity.Record;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public interface SearchService {

    List<Record> searchByTitle(String text, LocalDate from, LocalDate to);
}
