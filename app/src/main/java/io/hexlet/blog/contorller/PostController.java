package io.hexlet.blog.contorller;

import io.hexlet.blog.model.Post;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PostController {
    List<Post> posts = new ArrayList<>();

    @GetMapping("/posts")
    public ResponseEntity<?> index(@RequestParam(defaultValue = "10") Integer limit) {
        var result = posts.stream().limit(limit).toList();

        return ResponseEntity
                .ok()
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(result);
    }

    @PostMapping("/posts")
    public ResponseEntity<?> create(@RequestBody @Valid Post post) {
        URI location = URI.create(String.format("/posts/%s", post.getId()));
        posts.add(post);

        return ResponseEntity
                .created(location)
                .body(post);
    }

    @GetMapping("/psots/{id}")
    public ResponseEntity<?> show(@PathVariable String id) {
        return posts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<?> update(
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

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        posts.removeIf(post -> post.getId().equals(id));
        return ResponseEntity.noContent().build();
    }
}
