package com.sparta.itsmine.domain.report.service;

import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.product.repository.ProductRepository;
import com.sparta.itsmine.domain.report.dto.BlockRequestDto;
import com.sparta.itsmine.domain.report.dto.ReportRequestDto;
import com.sparta.itsmine.domain.report.dto.ReportResponseDto;
import com.sparta.itsmine.domain.report.entity.Report;
import com.sparta.itsmine.domain.report.entity.ReportStatus;
import com.sparta.itsmine.domain.report.entity.ReportType;
import com.sparta.itsmine.domain.report.repository.ReportRepository;
import com.sparta.itsmine.domain.user.entity.User;
import com.sparta.itsmine.domain.user.repository.UserAdapter;
import com.sparta.itsmine.domain.user.utils.UserRole;
import com.sparta.itsmine.global.common.response.ResponseExceptionEnum;
import com.sparta.itsmine.global.exception.DataDuplicatedException;
import com.sparta.itsmine.global.exception.DataNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {


    private static final Logger log = LoggerFactory.getLogger(ReportService.class);
    private final ReportRepository reportRepository;
    private final ProductRepository productRepository;
    private final UserAdapter userAdapter;

    public void createReport(User user, ReportRequestDto requestDto) {
        User badPerson = null;
        if (requestDto.getReportType().equals(ReportType.PRODUCT)) {
            Product product = productRepository.findById(requestDto.getTypeId()).orElseThrow(
                    () -> new DataNotFoundException(ResponseExceptionEnum.PRODUCT_NOT_FOUND)
            );
            badPerson = product.getUser();
        } else if (requestDto.getReportType().equals(ReportType.CHAT)) {
            badPerson = userAdapter.findById(requestDto.getTypeId());
        }
        reportRepository.save(new Report(user, badPerson, requestDto));
    }

    @Transactional
    public void updateReport(User user, ReportRequestDto requestDto, Long reportId) {
        Report report = getReport(reportId);
        checkReport(report);
        report.update(requestDto);
    }

    public void deleteReport(User user, Long reportId) {
        checkUserRole(user);
        Report report = getReport(reportId);
        checkReport(report);
        reportRepository.delete(report);
    }

    public Page<ReportResponseDto> getAllReport(User user, Pageable pageable) {
        if (user.getUserRole().equals(UserRole.MANAGER)) {
            return reportRepository.findAll(pageable)
                    .map(ReportResponseDto::new);
        } else {
            return reportRepository.findAllByUserId(user.getId(), pageable)
                    .map(ReportResponseDto::new);
        }
    }

    public void checkReport(Report report) {
        if (report.getReportStatus().equals(ReportStatus.COMPLETE)) {
            throw new DataDuplicatedException(ResponseExceptionEnum.REPORT_COMPLETE_STATUS);
        }
    }

    public Report getReport(Long reportId) {
        return reportRepository.findById(reportId).orElseThrow(
                () -> new DataNotFoundException(ResponseExceptionEnum.REPORT_NOT_FOUND)
        );
    }

    public void checkUserRole(User user) {
        if (!user.getUserRole().equals(UserRole.MANAGER)) {
            throw new DataDuplicatedException(ResponseExceptionEnum.REPORT_MANAGER_STATUS);
        }
    }

    public ReportResponseDto getReportDetail(User user, Long reportId) {
        Report report = getReport(reportId);
        if (!user.getUserRole().equals(UserRole.MANAGER)
                || report.getUser().getId().equals(user.getId())) {
            throw new DataNotFoundException(ResponseExceptionEnum.REPORT_NOT_ROLE);
        }
        return new ReportResponseDto(report);
    }

    @Transactional
    public void blockUser(User user, BlockRequestDto requestDto) {
        if (user.getId().equals(requestDto.getUserId())) {
            throw new DataDuplicatedException(ResponseExceptionEnum.SELF_NOT_BLOCK);
        }
        LocalDateTime blockDate = LocalDateTime.now().plusDays(requestDto.getBlockPlusDate());
        log.info("BLOCK userID : {} ", requestDto.getUserId());
        User blockUser = userAdapter.findById(requestDto.getUserId());
        blockUser.block(blockDate, requestDto.getBenReason());
    }

    @Transactional
    public void completeStatus(User user, Long reportId) {
        checkUserRole(user);
        Report report = getReport(reportId);
        report.statusComp();
    }

//    public List<> reportFarthing(User user, String reportType) {
//    }
}
