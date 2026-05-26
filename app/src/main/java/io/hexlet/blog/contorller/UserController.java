package io.hexlet.blog.contorller;

import io.hexlet.blog.exception.ResourceNotFoundException;
import io.hexlet.blog.model.User;
import io.hexlet.blog.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> index(@RequestParam(defaultValue = "10") Integer limit) {
        var users = userRepository.findAll();
        var limitedUsers = users.stream()
                .limit(limit)
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(limitedUsers);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid User user) {
        var savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("404 Not found"));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid User userData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("404 Not found"));
        user.setFirstname(userData.getFirstname());
        user.setLastname(userData.getLastname());
        user.setEmail(userData.getEmail());
        user.setBirthday(userData.getBirthday());
        var updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
