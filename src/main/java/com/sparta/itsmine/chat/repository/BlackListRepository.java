package com.sparta.itsmine.chat.repository;

import com.sparta.itsmine.chat.entity.BlackList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {

    Optional<BlackList> findByFromUserIdAndToUserId(Long frmUserId, Long toUserId);
}
