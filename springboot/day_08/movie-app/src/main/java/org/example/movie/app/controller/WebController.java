package org.example.movie.app.controller;

import lombok.RequiredArgsConstructor;
import org.example.movie.app.entity.Blog;
import org.example.movie.app.entity.Movie;
import org.example.movie.app.entity.Review;
import org.example.movie.app.model.enums.MovieType;
import org.example.movie.app.repository.MovieRepository;
import org.example.movie.app.service.BlogService;
import org.example.movie.app.service.ReviewService;
import org.example.movie.app.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class WebController {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private WebService webService;

    @Autowired
    private BlogService blogService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/")
    public String getHomePage(Model model) {
        Page<Movie> pageDataPhimHot = webService.getHotMovies(true, 1, 8);
        Page<Movie> pageDataPhimLe = webService.getMovieByType(MovieType.PHIM_LE, true, 1, 6);
        Page<Movie> pageDataPhimBo = webService.getMovieByType(MovieType.PHIM_BO, true, 1, 6);
        Page<Movie> pageDataPhimChieuRap = webService.getMovieByType(MovieType.PHIM_CHIEU_RAP, true, 1, 6);
        Page<Blog> pageDataBlog = blogService.getByStatusAndOrderByPublishedAtDesc(true, 1, 4);

        model.addAttribute(" ", pageDataPhimHot.getContent());
        model.addAttribute("listPhimLe", pageDataPhimLe.getContent());
        model.addAttribute("listPhimBo", pageDataPhimBo.getContent());
        model.addAttribute("listPhimChieuRap", pageDataPhimChieuRap.getContent());
        model.addAttribute("listBlog", pageDataBlog.getContent());
        return ("/web/index");
    }

    @GetMapping("/phim/{id}/{slug}")
    public String findById(Model model, @PathVariable int id, @PathVariable String slug, @RequestParam(required = false, defaultValue = "1") Integer page,
                           @RequestParam(required = false, defaultValue = "6") Integer size) {

        //Optional<Movie> optionalMovie = movieRepository.findByTypeAndIdAndSlug(MovieType.PHIM_LE, id, slug);
        Optional<Movie> optionalMovie = webService.getMovieByTypeAndIdAndSlug(MovieType.PHIM_LE, id, slug);
        if (optionalMovie.isPresent()) {
            Movie movie = optionalMovie.get();

            List<Movie> relatedMovies = webService.getByTypeAndStatusAndRatingGreaterThanEqualAndIdNotOrderByRatingDescViewDescPublishedAtDesc(movie.getType(), true, 8, movie.getId(), page, size);
            List<Review> relatedReview = reviewService.getReviewsOfMovie(movie.getId());
            model.addAttribute("movie", movie);
            model.addAttribute("relatedMovies", relatedMovies);
            model.addAttribute("relatedReview", relatedReview);
            return "web/Movie-detail";
        } else {
            return "web/Movie-not-found"; // Điều hướng đến trang thông báo không tìm thấy nếu không có phim
        }
    }

    @GetMapping("/phim-le")
    public String getPhimLePage(Model model, @RequestParam(required = false, defaultValue = "1") Integer page,
                                @RequestParam(required = false, defaultValue = "12") Integer size) {
        Page<Movie> pageData = webService.getMovieByType(MovieType.PHIM_LE, true, page, size);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageData", pageData);
        return "web/phim-le";
    }

    @GetMapping("/phim-bo")
    public String getPhimBoPage(Model model) {
        List<Movie> movies = webService.getMovieByType(MovieType.PHIM_BO, true, Sort.by("publishedAt").descending());
        model.addAttribute("movies", movies);
        return "web/phim-bo";
    }

    @GetMapping("/phim-chieu-rap")
    public String getPhimChieuRapPage(Model model) {
        List<Movie> movies = webService.getMovieByType(MovieType.PHIM_CHIEU_RAP, true, Sort.by("publishedAt").descending());
        model.addAttribute("movies", movies);
        return "web/phim-chieu-rap";
    }

    @GetMapping("/blog")
    public String getBlog(Model model, @RequestParam(required = false, defaultValue = "1") Integer page,
                          @RequestParam(required = false, defaultValue = "12") Integer size) {
        Page<Blog> blogData = blogService.getByStatus(true, page, size);
        model.addAttribute("currentPage", page);
        model.addAttribute("blogData", blogData);

        return "/web/Blog";
    }

    @GetMapping("/tin-tuc/{id}/{slug}")
    public String getById(Model model, @PathVariable int id, @PathVariable String slug) {
        Optional<Blog> optionalBlog = blogService.getByStatusAndIdAndSlug(true, id, slug);
        if (optionalBlog.isPresent()) {
            Blog blog = optionalBlog.get();
            model.addAttribute("blog", blog);
            return "web/Blog-detail";
        } else {
            return "web/Blog-not-found"; // Điều hướng đến trang thông báo không tìm thấy nếu không có phim
        }
    }
}
