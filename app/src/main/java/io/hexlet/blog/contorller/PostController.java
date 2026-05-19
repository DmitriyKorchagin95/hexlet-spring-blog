package io.hexlet.blog.contorller;

import io.hexlet.blog.model.Post;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    List<Post> posts = new ArrayList<>();

    @GetMapping()
    public ResponseEntity<?> index(@RequestParam(defaultValue = "10") Integer limit) {
        var result = posts.stream().limit(limit).toList();

        return ResponseEntity
                .ok()
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(result);
    }

    @PostMapping()
    public ResponseEntity<?> createPost(@RequestBody @Valid Post post) {
        URI location = URI.create(String.format("/api/posts/%s", post.getId()));
        posts.add(post);

        return ResponseEntity
                .created(location)
                .body(post);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showPost(@PathVariable String id) {
        return posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost (
            @PathVariable String id,
            @RequestBody @Valid Post data
    ) {

        var maybePost = posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst();

        if (maybePost.isPresent()) {
            Post post = maybePost.get();
            post.setTitle(data.getTitle());
            post.setContent(data.getContent());
            post.setAuthor(data.getAuthor());

            return ResponseEntity.ok(post);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        posts.removeIf(post -> post.getId().equals(id));
        return ResponseEntity.noContent().build();
    }
}
