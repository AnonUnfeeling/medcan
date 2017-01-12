package ua.softgroup.medreview.persistent.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.softgroup.medreview.persistent.entity.Company;
import ua.softgroup.medreview.persistent.entity.Record;
import ua.softgroup.medreview.persistent.entity.User;
import ua.softgroup.medreview.persistent.repository.search.RecordRepositorySearch;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
public interface RecordRepository extends PagingAndSortingRepository<Record, Long>, RecordRepositorySearch {

    Record findByTitle(String name);

    Page<Record> findByAuthor(User author, Pageable pageable);

    Page<Record> findByAuthorCompany(Company company, Pageable pageable);

    Page<Record> findAll(Pageable pageable);

}
