package com.sparta.itsmine.domain.qna.entity;

import com.sparta.itsmine.domain.comment.entity.Comment;
import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.global.common.TimeStamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Qna extends TimeStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String content;

  @ManyToOne
  private Product product;

  @OneToMany(mappedBy = "qna", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Comment> commentList = new ArrayList<>();
}
