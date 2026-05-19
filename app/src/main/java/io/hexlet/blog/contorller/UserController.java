package io.hexlet.blog.contorller;

import io.hexlet.blog.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    List<User> users = new ArrayList<>();

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity
                .ok()
                .body(users);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid User user) {
        URI location = URI.create(String.format("/api/users/%s", user.getId()));
        users.add(user);

        return ResponseEntity
                .created(location)
                .body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser (
            @PathVariable String id,
            @RequestBody @Valid User data
    ) {

        var maybeUser = users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();

        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            user.setName(data.getName());
            user.setEmail(data.getEmail());

            return ResponseEntity.ok(user);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showUser(@PathVariable String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        users.removeIf(user -> user.getId().equals(id));
        return ResponseEntity.noContent().build();
    }
}
