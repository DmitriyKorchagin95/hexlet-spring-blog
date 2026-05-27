package io.hexlet.blog.contorller;

import io.hexlet.blog.exception.ResourceNotFoundException;
import io.hexlet.blog.model.Post;
import io.hexlet.blog.repository.PostRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostRepository postRepository;

    @GetMapping
    public ResponseEntity<?> getPublishedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(postRepository.findByPublishedTrue(pageable));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid Post post) {
        var savedPost = postRepository.save(post);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        var post = postRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("404 Not found"));
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid Post postData) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("404 Not found"));

        post.setTitle(postData.getTitle());
        post.setContent(postData.getContent());
        post.setPublished(postData.isPublished());
        var updatedPost = postRepository.save(post);

        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        postRepository.deleteById(id);
    }
}
