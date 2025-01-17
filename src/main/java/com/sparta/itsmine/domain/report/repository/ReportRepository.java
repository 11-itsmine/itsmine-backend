package com.sparta.itsmine.domain.report.repository;

import com.sparta.itsmine.domain.report.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Page<Report> findAllByUserId(Long userId, Pageable pageable);
}
