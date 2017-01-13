package ua.softgroup.medreview.persistent.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ua.softgroup.medreview.persistent.entity.Note;
import ua.softgroup.medreview.persistent.repository.search.NoteRepositorySearch;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public interface NoteRepository extends CrudRepository<Note, Long>, NoteRepositorySearch {

    Set<Note> findByRecordId(Long recordId);

    Set<Note> findByRecordTitle(String title);

//    List<Note> findByRecordTitle(String title);

    List<Note> findByRecordTitle(String title, Sort sort);

    Page<Note> findByRecordTitle(String title, Pageable pageable);

    Optional<Note> findByTitle(String title);

    @Transactional
    void deleteByTitle(String title);
}
