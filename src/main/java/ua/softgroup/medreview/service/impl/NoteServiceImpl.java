package ua.softgroup.medreview.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.softgroup.medreview.persistent.entity.Note;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.repository.NoteRepository;
import ua.softgroup.medreview.service.AuthenticationService;
import ua.softgroup.medreview.service.NoteService;
import ua.softgroup.medreview.web.dto.NoteDto;

import java.util.Optional;

/**
 * @author Sergiy Perevyazko <sg.sergiyp@gmail.com>
 */
@Service
public class NoteServiceImpl implements NoteService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final NoteRepository noteRepository;
    private final AuthenticationService authenticationService;
    private static final int NUMBER_OF_PAGES = 10;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, AuthenticationService authenticationService) {
        this.noteRepository = noteRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public void createNote(NoteDto noteDto, Record record) {
        noteRepository.save(convertDtoToEntity(noteDto, record));
    }

    @Override
    public void editNote(NoteDto noteDto, Note noteForEdit) {
        noteForEdit.setTitle(noteDto.getTitle());
        noteForEdit.setDescription(noteDto.getDescription());
        noteForEdit.setConclusion(noteDto.getConclusion());
        noteForEdit.setKeywords(noteDto.getKeywords());
        noteForEdit.setSubject(noteDto.getCategory());
        noteForEdit.setSubSubject(noteDto.getSubCategory());
        noteForEdit.setTreatment(noteDto.getTreatment());
        noteRepository.save(noteForEdit);
    }

    @Override
    public void deleteNoteById(Long id) {
        noteRepository.delete(id);
    }

    @Override
    public void deleteNoteByTitle(String title) {
        noteRepository.deleteByTitle(title);
    }

    @Override
    public Optional<Note> findById(Long id) {
        return Optional.ofNullable(noteRepository.findOne(id));
    }

    @Override
    public Optional<Note> findByTitle(String title) {
        return noteRepository.findByTitle(title);
    }

    @Override
    public Page<Note> getNotesByRecordTitle(String title, int page) {
        return getByRecordTitle(title, page);
    }

    @Override
    public Page<NoteDto> getNoteDtosByRecordTitle(String title, int page) {
        return getByRecordTitle(title, page).map(this::convertEntityToDto);
    }

    @Override
    public Page<Note> getSortedNotesByRecordTitle(String title, int page, String sortDirection, String sortField) {
        Page<Note> sortedNotes;
        try {
            sortedNotes = noteRepository.findByRecordTitle(title, new PageRequest(page, NUMBER_OF_PAGES, new Sort(Sort.Direction.valueOf(sortDirection), sortField)));
        } catch (org.springframework.data.mapping.PropertyReferenceException e) { //bad sort parameters
            sortedNotes = getByRecordTitle(title, page);
        }
        return sortedNotes;
    }

    @Override
    public Page<NoteDto> getSortedNoteDtosByRecordTitle(String title, int page, String sortDirection, String sortField) {
        return getSortedNotesByRecordTitle(title, page, sortDirection, sortField).map(this::convertEntityToDto);
    }

    private Page<Note> getByRecordTitle(String title, int page) {
        return noteRepository.findByRecordTitle(title, new PageRequest(page, NUMBER_OF_PAGES));
    }

    private Note convertDtoToEntity(NoteDto noteDto, Record record) {
        Note note = new Note(noteDto.getTitle(),
                noteDto.getDescription(),
                noteDto.getConclusion(),
                noteDto.getKeywords(),
                noteDto.getCategory(),
                noteDto.getSubCategory(),
                record.getCountry(),
                authenticationService.getPrincipal().getLanguage(),
                noteDto.getTreatment());
        note.setRecord(record);
        return note;
    }

    private NoteDto convertEntityToDto(Note note) {
        return new NoteDto(note.getId(),
                note.getTitle(),
                note.getDescription(),
                note.getConclusion(),
                note.getKeywords(),
                note.getSubject(),
                note.getSubSubject(),
                note.getTreatment(),
                note.getRecord().getTitle());
    }
}
