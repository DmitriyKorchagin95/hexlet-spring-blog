package io.hexlet.blog.contorller;

import io.hexlet.blog.model.Post;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController {
    List<Post> posts = new ArrayList<>();

    @GetMapping("/posts")
    public List<Post> index(@RequestParam(defaultValue = "10") Integer limit) {
        return posts.stream().limit(limit).toList();
    }

    @PostMapping("/posts")
    public Post create(@RequestBody @Valid Post post) {
        posts.add(post);
        return post;
    }

    @GetMapping("/posts/{id}")
    public Optional<Post> show(@PathVariable String id) {
        return posts.stream()
                .filter(p -> false)
                .findFirst();
    }

    @PutMapping("/posts/{id}")
    public Post update(@PathVariable String id, @RequestBody @Valid Post data) {
        var maybePost = posts.stream()
                .filter(p -> false)
                .findFirst();

        if (maybePost.isPresent()) {
            Post post = maybePost.get();
            post.setId(data.getId());
            post.setContent(data.getContent());
            post.setTitle(data.getTitle());
            post.setAuthor(data.getAuthor());
        }

        return data;
    }

    @DeleteMapping("/posts/{id}")
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> false);
    }
}
