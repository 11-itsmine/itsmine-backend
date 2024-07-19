package com.sparta.itsmine.domain.qnaJH.service;

import com.sparta.itsmine.domain.qnaJH.QnaRepositoryJH;
import com.sparta.itsmine.domain.qnaJH.dto.QnaRequestDtoJH;
import com.sparta.itsmine.domain.qnaJH.entity.QnaJH;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QnaServiceJH {

    private final QnaRepositoryJH qnaRepository;

    public void addQna(QnaRequestDtoJH requestDto) {
        QnaJH qnaJH = new QnaJH(requestDto);

        qnaRepository.save(qnaJH);
    }
}
