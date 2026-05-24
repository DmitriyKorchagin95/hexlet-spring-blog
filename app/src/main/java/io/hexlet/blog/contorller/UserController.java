package io.hexlet.blog.contorller;

import io.hexlet.blog.model.User;
import io.hexlet.blog.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity
                .ok().body(userRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid User user) {
        var savedUser = userRepository.save(user);
        URI location = URI.create(String.format("/api/users/%s", savedUser.getId()));

        return ResponseEntity
                .created(location).body(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
            @RequestBody @Valid User data
    ) {

        return userRepository.findById(id).map(user -> {
            user.setFirstname(data.getFirstname());
            user.setLastname(data.getLastname());
            user.setEmail(data.getEmail());
            user.setBirthday(data.getBirthday());
            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(updatedUser);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
