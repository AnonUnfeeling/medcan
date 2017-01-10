package ua.softgroup.medreview.service;

import ua.softgroup.medreview.persistent.entity.Note;
import ua.softgroup.medreview.persistent.entity.Record;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public interface SearchService {

    List<Record> searchByTitle(String text, LocalDate from, LocalDate to);

    List<Note> searchByAllFields(String text, LocalDate from, LocalDate to);

    List<Note> searchNotesInRecord(String recordTitle, String text, String category, String subCategory, String treatment);
}
