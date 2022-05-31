package management.teacher_management_api.drivers.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import management.teacher_management_api.drivers.api.converters.PostResolver;
import management.teacher_management_api.drivers.api.exceptions.NotFoundException;
import management.teacher_management_api.drivers.api.payloads.posts.CreatePostRequest;
import management.teacher_management_api.drivers.api.payloads.posts.GetPostResponse;
import management.teacher_management_api.drivers.api.payloads.posts.GetPostsResponse;
import management.teacher_management_api.usercases.posts.CreatePostUsecase;
import management.teacher_management_api.usercases.posts.DeletePostUsecase;
import management.teacher_management_api.usercases.posts.GetPostUsecase;
import management.teacher_management_api.usercases.posts.GetPostsUsecase;
import management.teacher_management_api.usercases.posts.IncrementPostViewsUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@CrossOrigin
@RequiredArgsConstructor
public class PostsController {
    private final CreatePostUsecase createPostUsecase;
    private final DeletePostUsecase deletePostUsecase;
    private final GetPostUsecase getPostUsecase;
    private final GetPostsUsecase getPostsUsecase;
    private final PostResolver postResolver;
    private final IncrementPostViewsUsecase incrementPostViewsUsecase;

    @GetMapping(value = "/posts/{post_id}")
    public ResponseEntity<GetPostResponse> getPost(@PathVariable("post_id") long postId) {
        try {
            val result = getPostUsecase.execute(postId);
            return ResponseEntity.ok(
                    GetPostResponse.builder().post(postResolver.toDTO(result)).build());
        } catch (NotFoundException ex) {
            log.error("Could not find post with provided Id", ex);
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/posts")
    public ResponseEntity<GetPostsResponse> getPosts(
            @RequestParam(name = "page") int pageNumber,
            @RequestParam(name = "q", required = false) Optional<String> q,
            @RequestParam(name = "lowest_price", required = true) Double lowestPrice,
            @RequestParam(name = "highest_price", required = true) Double highestPrice,
            @RequestParam(name = "speciality", required = false) Optional<String> specialityId,
            @RequestParam(name = "rating", required = false) Optional<Double> rating,
            @RequestParam(name = "sort_by", required = false) Optional<String> sortBy) {
        try {
            val result =
                    getPostsUsecase.execute(
                            pageNumber, q, lowestPrice, highestPrice, specialityId, rating, sortBy);

            return ResponseEntity.ok(
                    GetPostsResponse.builder()
                            .totalPages(result.getTotalPages())
                            .posts(
                                    result.getPosts().stream()
                                            .map(postResolver::toDTO)
                                            .collect(Collectors.toList()))
                            .build());
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value = "/posts")
    public ResponseEntity<Void> createPost(@RequestBody CreatePostRequest body) {
        val userId = ApiUtils.getAuthenticatedUserId();

        try {
            createPostUsecase.execute(userId, body.getTitle(), body.getContent());
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/posts/{post_id}")
    public ResponseEntity<Void> deletePost(@PathVariable("post_id") long postId) {
        val userId = ApiUtils.getAuthenticatedUserId();

        try {
            deletePostUsecase.execute(userId, postId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = "/posts/{post_id}/views")
    public ResponseEntity<Void> incrementPostViews(@PathVariable("post_id") long postId) {
        try {
            incrementPostViewsUsecase.execute(postId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}
