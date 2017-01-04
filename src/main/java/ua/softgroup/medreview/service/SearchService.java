package ua.softgroup.medreview.service;

import ua.softgroup.medreview.persistent.entity.Note;
import ua.softgroup.medreview.persistent.entity.Record;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public interface SearchService {

    List<Note> searchByKeywords(String keywords, LocalDate from, LocalDate to);

    List<Record> searchByTitle(String keywords, LocalDate from, LocalDate to);
}
