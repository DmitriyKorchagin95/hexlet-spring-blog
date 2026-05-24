package io.hexlet.blog.contorller;

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
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestBody @Valid Post data
    ) {

        return postRepository.findById(id)
                .map(post -> {
                    post.setTitle(data.getTitle());
                    post.setContent(data.getContent());
                    post.setPublished(data.isPublished());

                    Post updatedPost = postRepository.save(post);
                    return ResponseEntity.ok(updatedPost);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {

        if (!postRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();

    }
}
