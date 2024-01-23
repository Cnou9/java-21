package org.example.movie.app.repository;

import org.example.movie.app.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
    //4 bài blog (có status là true, sắp xếp theo thời gian publishedAt giảm dần)
    Page<Blog> findByStatusAndPublishedAtNotNullOrderByPublishedAtDesc(boolean status, Pageable pageable);

    //JPQL
    @Query("SELECT b FROM Blog b WHERE b.status = :status ORDER BY b.publishedAt DESC ")
    Page<Blog> findByStatusAndOrderByPublishedAtJPQL(@Param("status") boolean status, Pageable pageable);
    //Native Query
    @Query(value = "SELECT * FROM blog WHERE status = :status ORDER BY publishedAt DESC ", nativeQuery = true)
    Page<Blog> findByStatusAndOrderByPublishedAtNQ(@Param("status") boolean status,  Pageable pageable);

    //tim kiem blog theo status va sap xep publishedAt theo giam dan
    Page<Blog> findByStatus(boolean status, Pageable pageable);
    //JPQL
    @Query("SELECT b FROM Blog b WHERE b.status = :status ")
    Page<Blog>findByStatusJPQL(@Param("status") boolean status, Pageable pageable);
    //Native Query
    @Query(value = "SELECT * FROM blog  WHERE b.status = :status ", nativeQuery = true)
    Page<Blog>findByStatusNQ(@Param("status") boolean status, Pageable pageable);

    Optional<Blog> findByStatusAndIdAndSlug(boolean status, int id, String slug);
    //JPQL
    @Query("SELECT b FROM Blog b WHERE b.status = :status AND b.id = :id AND b.slug = :slug")
    Optional<Blog>findByStatusAndIdAndSlugJQPL(@Param("status") boolean status, @Param("id") int id,@Param("slug") String slug );
    //NQ
    @Query(value = "SELECT b FROM blog WHERE status = :status AND id = :id AND slug = :slug", nativeQuery = true)
    Optional<Blog>findByStatusAndIdAndSlugNQ(@Param("status") boolean status, @Param("id") int id,@Param("slug") String slug );
}