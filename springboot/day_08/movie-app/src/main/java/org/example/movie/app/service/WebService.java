package org.example.movie.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.movie.app.entity.Movie;
import org.example.movie.app.model.enums.MovieType;
import org.example.movie.app.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebService {
    private final MovieRepository movieRepository;



    public Optional<Movie> getMovieByTypeAndIdAndSlug(MovieType movieType, int id, String  slug){
        return movieRepository.findByTypeAndIdAndSlug(movieType, id, slug);
    }
    public List<Movie> getMovieByType(MovieType type, Boolean status, Sort sort){
        return  movieRepository.findByTypeAndStatus(type, status, sort);

    }
    public Page<Movie> getMovieByType(MovieType movieType, Boolean status, Integer page, Integer size){
        PageRequest pageRequest = PageRequest.of(page-1, size, Sort.by("publishedAt").descending());

        return movieRepository.findByTypeAndStatus(movieType, status, pageRequest);
    }

    public Movie getMovie(Integer id, String slug, Boolean status){
        return  movieRepository.findByIdAndSlugAndStatus(id, slug, status).orElse(null);
    }
    public Page<Movie> getHotMovies(Boolean status, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("view").descending()); // page trong jpa bắt đầu từ 0
        return movieRepository.findByStatus(status, pageRequest);
    }
    public List<Movie> getMovieByTypeRelated(MovieType movieType,  Integer page, Integer size){
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return movieRepository.findByType(movieType, pageRequest);
    }
    public List<Movie> getByTypeAndStatusAndRatingGreaterThanEqualAndIdNotOrderByRatingDescViewDescPublishedAtDesc(
            MovieType type,
            Boolean status,
            Integer rating,
            Integer id, Integer page, Integer size){
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return  movieRepository.findByTypeAndStatusAndRatingGreaterThanEqualAndIdNotOrderByRatingDescViewDescPublishedAtDesc(
                type, status, rating, id, pageRequest);


    }


}
