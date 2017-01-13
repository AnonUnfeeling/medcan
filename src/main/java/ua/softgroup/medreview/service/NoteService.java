package ua.softgroup.medreview.service;

import org.springframework.data.domain.Page;
import ua.softgroup.medreview.persistent.entity.Note;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.web.dto.NoteDto;

import java.util.Optional;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
public interface NoteService {

    void createNote(NoteDto noteDto, Record record);

    void editNote(NoteDto noteDto, Note noteForEdit);

    void deleteNoteById(Long id);

    void deleteNoteByTitle(String name);

    Optional<Note> findById(Long id);

    Optional<Note> findByTitle(String title);

    Page<Note> getNotesByRecordTitle(String title, int page);

    Page<NoteDto> getNoteDtosByRecordTitle(String title, int page);

    Page<Note> getSortedNotesByRecordTitle(String title, int page, String sortDirection, String sortField);

    Page<NoteDto> getSortedNoteDtosByRecordTitle(String title, int page, String sortDirection, String sortField);
}
