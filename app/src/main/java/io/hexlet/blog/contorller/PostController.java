package io.hexlet.blog.contorller;

import io.hexlet.blog.exception.ResourceNotFoundException;
import io.hexlet.blog.model.Post;
import io.hexlet.blog.repository.PostRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostRepository postRepository;

    @GetMapping
    public ResponseEntity<?> index(@RequestParam(defaultValue = "10") Integer limit) {
        var posts = postRepository.findAll();
        var result = posts.stream()
                .limit(limit)
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(result);
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody @Valid Post post) {
        Post savedPost = postRepository.save(post);
        URI location = URI.create(
                String.format("/api/posts/%s", savedPost.getId())
        );

        return ResponseEntity
                .created(location)
                .body(savedPost);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showPost(@PathVariable Long id) {
        return postRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("404 Not found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestBody @Valid Post post
    ) {

        return postRepository.findById(id)
                .map(p -> {
                    p.setTitle(post.getTitle());
                    p.setContent(post.getContent());
                    p.setPublished(post.isPublished());
                    Post updatedPost = postRepository.save(p);

                    return ResponseEntity.ok(updatedPost);
                })
                .orElseThrow(() -> new ResourceNotFoundException("404 Not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {

        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("404 Not found");
        }

        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();

    }
}
