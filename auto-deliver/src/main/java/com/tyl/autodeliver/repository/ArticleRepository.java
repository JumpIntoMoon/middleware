package com.tyl.autodeliver.repository;

import com.tyl.autodeliver.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-06-03 16:15
 **/
@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    @Query(value = "SELECT * FROM t_article WHERE is_sent = 0 and category = ?1 LIMIT ?2", nativeQuery = true)
    List<Article> findNotSentByCategoryLimit(String category, Integer scalar);

    @Query(value = "SELECT * FROM t_article WHERE url = ?1 AND source = ?2", nativeQuery = true)
    List<Article> findAllBySourceUrl(String url, String source);

    @Transactional
    @Modifying
    @Query(value = "UPDATE t_article SET is_sent = ?1 WHERE id =?2", nativeQuery = true)
    Integer updateSentStatus(Integer sentStatus, Long id);

}
