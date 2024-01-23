package org.example.movie.app.repository;

import org.example.movie.app.entity.Movie;
import org.example.movie.app.model.enums.MovieType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// Movie : Đối tuợng cần thao tác
// Integer : Kiểu dữ liệu của khóa chính
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    List<Movie> findByTitle(String title);

    // JPQL
    @Query("SELECT m FROM Movie m WHERE m.title = ?1")
    List<Movie> findByTitleJPQL(String title);

    @Query("SELECT m FROM Movie m WHERE m.title = :title")
    List<Movie> findByTitleJPQL1(@Param("title") String title);

    // Native Query
    @Query(value = "SELECT * FROM movies WHERE title = ?1", nativeQuery = true)
    List<Movie> findByTitleNQ(String title);

    // timf kiếm phim theo tiêu đề có từ khóa
    List<Movie> findByTitleContaining(String title);
    //JPQL
    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Movie> findByTitleContainingJPQL(@Param("title") String title);
    //native Query
    @Query(value = "SELECT * FROM movies m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))", nativeQuery = true)
    List<Movie> findByTitleContainingNQ(@Param("title") String title);

    // kiếm tra phim có tồn tại theo tiêu đề
    boolean existsByTitle(String title);
    //JPQL
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END FROM Movie m WHERE LOWER(m.title) = LOWER(:title)")
    boolean existsByTitleJPQL(@Param("title") String title);
    //nativquery
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM movies m WHERE LOWER(m.title) = LOWER(:title)", nativeQuery = true)
    boolean existsByTitleNQ(@Param("title") String title);

    // đếm số lượng phim theo tiêu đề
    long countByTitle(String title);
    //JPQL
    @Query("SELECT COUNT(m) FROM Movie m  WHERE LOWER(m.title) = LOWER(:title) ")
    long countByTitleJPQL(@Param("title") String title);
    //native query
    @Query(value = "SELECT COUNT(m) FROM movies m  WHERE LOWER(m.title) = LOWER(:title) ", nativeQuery = true)
    long countByTitleNQ(@Param("title") String title);

    //tìm kiếm phim theo tiêu đề và trạng thái
    List<Movie> findByStatusAndType(boolean status, MovieType type);
    //JPQL
    @Query("SELECT m FROM Movie m WHERE m.status = :status AND m.type = :type")
    List<Movie> findByStatusAndTypeJPQL(@Param("status")boolean status, @Param("type") MovieType type);
    //Native ẻuery
    @Query(value = "SELECT * FROM movies m WHERE m.status = :status AND m.type = :type", nativeQuery = true)
    List<Movie> findByStatusAndTypNQ(@Param("status")boolean status, @Param("type") MovieType type);

    //sắp xếp các bộ phim theo view giảm dần
    List<Movie> findAllByOrderByViewDesc();
    @Query ("SELECT m from Movie m ORDER BY m.view DESC ")
    List<Movie> findAllByOrderByViewDescJPQL();
    @Query(value = "SELECT * FROM movies m ORDER BY m.view desc ", nativeQuery = true)
    List<Movie> findAllByOrderByViewDescNQ();

    // tìm kiếm phim theo trạng thái và sắp xếp theo tiêu chí nào đó
    List<Movie> findByStatus(boolean status, Sort sort);
    @Query("SELECT m FROM Movie m WHERE m.status = :status ORDER BY :#{#sort.toJpaSort()}")
    List<Movie> findByStatusJPQL(@Param("status") boolean status,@Param("sort") Sort sort);
    @Query(value = "SELECT * FROM movies m WHERE m.status = :status ORDER BY :#{#sort.toJpaSort()}", nativeQuery = true)
    List<Movie> findByStatusNQ(@Param("status") boolean status,@Param("sort") Sort sort);

    //tim kiem theo type va status sap xep pushlishedAt theo chieu giam dan
    List<Movie> findByTypeAndStatusOrderByPublishedAtDesc(MovieType type, boolean status);
    @Query("SELECT m FROM Movie m WHERE m.type = :type AND m.status = :status ORDER BY m.publishedAt DESC ")
    List<Movie> findByTypeAndStatusOrderByPublishedAtDescJPQL(@Param("type") MovieType type, @Param("status") boolean status);


    List<Movie> findByTypeAndStatus(MovieType type, boolean status, Sort sort);
    Page<Movie> findByTypeAndStatus(MovieType type, boolean status, Pageable pageable);
    //JPQL
    @Query("SELECT m FROM Movie m WHERE m.type = :type AND m.status = :status ")
    Page<Movie> findByTypeAndStatusJPQL(@Param("type") MovieType type,@Param("status") boolean status, Pageable pageable);
    //Native query
//    @Query(value = "SELECT * FROM movies m WHERE m.type = :type AND m.status = :status ", nativeQuery = true)
//    Page<Movie> findByTypeAndStatusNQ(@Param("type") MovieType type,@Param("status") boolean status, Pageable pageable);

    //Đe thucej hện phaan trang can them countQuery
    @Query(nativeQuery = true,
            value = "SELECT * FROM movies WHERE type = :type AND status = :status",
            countQuery = "SELECT COUNT(*) FROM movies WHERE type = :type AND status = :status")
    Page<Movie> findByTypeAndStatusNQ(@Param("type") MovieType type, @Param("status") boolean status, Pageable pageable);
    Optional<Movie> findByTypeAndIdAndSlug(MovieType type, int id, String slug);
    @Query("SELECT m FROM Movie m WHERE m.type = :type AND m.id = :id AND m.slug = :slug")
    Optional<Movie> findByTypeAndIdAndSlugJPQL(@Param("type") MovieType type,@Param("id") int id,@Param("slug") String slug);
    @Query(value = "SELECT * FROM movies m WHERE m.type = :type AND m.id = :id AND m.slug = :slug", nativeQuery = true)
    Optional<Movie> findByTypeAndIdAndSlugNQ(@Param("type") MovieType type,@Param("id") int id,@Param("slug") String slug);
    Optional<Movie> findByIdAndSlugAndStatus(int id, String slug, boolean status);

    Page<Movie> findByStatus(boolean status, Pageable pageable);
    //thay doi thông tin doi tuong
    //update
    @Modifying
    @Query("UPDATE Movie m SET m.title = ?1 WHERE m.id = 72")
    void  updateTitleById(String title, Integer id);

    //delete
    @Modifying
    @Query("DELETE FROM Movie m WHERE m.id = :id AND m.slug = :slug")
    void  deleteByIdAndSlug(@Param("id") Integer id, @Param("slug") String slug);

    List<Movie> findByType(MovieType type,  Pageable pageable);
    //jPQL
    @Query("SELECT m FROM Movie m WHERE m.type = :type")
    List<Movie> findByTypeJPQL(@Param("type") MovieType type, Pageable pageable);
    //NQ
    @Query(value = "SELECT * FROM movies m WHERE m.type = :type", nativeQuery = true)
    List<Movie> findByTypeNQ(@Param("type") MovieType type,  Pageable pageable);

    // sách phim đề xuất sẽ là những phim có cùng type với phim đó, có status = true, và có rating >= 8,
    // và sắp xếp theo rating giảm dần, view giảm dần, publishedAt giảm dần. Nhưng không chứa phim có id = 1
    // C1: Method query
    List<Movie> findByTypeAndStatusAndRatingGreaterThanEqualAndIdNotOrderByRatingDescViewDescPublishedAtDesc(
            MovieType type,
            Boolean status,
            Integer rating,
            Integer id,
            Pageable pageable
    );

    // C2: JPQL
    @Query("SELECT m FROM Movie m WHERE m.type = :type AND m.status = true AND m.rating >= 8 AND m.id != :id ORDER BY m.rating DESC, m.view DESC, m.publishedAt DESC")
    List<Movie> findRecommendedMovies(MovieType type, Integer id);

    // C3: Native Query
    @Query(value = "SELECT * FROM movies WHERE type = :type AND status = true AND rating >= 8 AND id != :id ORDER BY rating DESC, view DESC, published_at DESC", nativeQuery = true)
    List<Movie> findRecommendedMoviesNQ(MovieType type, Integer id);
}
