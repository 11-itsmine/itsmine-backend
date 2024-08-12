package com.sparta.itsmine.domain.report.dto;

import com.sparta.itsmine.domain.report.entity.Report;
import com.sparta.itsmine.domain.report.entity.ReportStatus;
import com.sparta.itsmine.domain.report.entity.ReportType;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ReportResponseDto {

    private Long reportId;
    private String username;
    private String nickName;
    private String badPerson;
    private String badPersonNickname;
    private String title;
    private String content;
    private ReportType reportType;
    private ReportStatus reportStatus;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public ReportResponseDto(Report report) {
        this.reportId = report.getId();
        this.username = report.getUser().getUsername();
        this.badPerson = report.getBadPerson().getUsername();
        this.badPersonNickname = report.getBadPerson().getNickname();
        this.nickName = report.getUser().getNickname();
        this.title = report.getTitle();
        this.content = report.getContent();
        this.reportType = report.getReportType();
        this.reportStatus = report.getReportStatus();
        this.createAt = report.getCreatedAt();
        this.updateAt = report.getUpdatedAt();
    }
}
