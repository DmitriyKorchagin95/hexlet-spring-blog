package io.hexlet.blog;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createPost_returns201_andBody() throws Exception {

        var body = """
            {
              "title": "First Post",
              "content": "Hello from Spring Boot",
              "published": true
            }
            """;

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("First Post"))
                .andExpect(jsonPath("$.content").value("Hello from Spring Boot"))
                .andExpect(jsonPath("$.published").value(true));
    }

    @Test
    void getAllPosts_returns200_andPage() throws Exception {

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.number").exists());
    }

    @Test
    void getPostById_returns200_andBody() throws Exception {

        var body = """
            {
              "title": "Spring Guide",
              "content": "Spring Boot tutorial",
              "published": false
            }
            """;

        var response = mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();

        var json = response.getResponse().getContentAsString();
        var id = json.replaceAll(".*\"id\":(\\d+).*", "$1");

        mockMvc.perform(get("/api/posts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Integer.parseInt(id)))
                .andExpect(jsonPath("$.title").value("Spring Guide"))
                .andExpect(jsonPath("$.content").value("Spring Boot tutorial"))
                .andExpect(jsonPath("$.published").value(false));
    }

    @Test
    void updatePost_returns200_andUpdatedBody() throws Exception {

        var createBody = """
            {
              "title": "Old Title",
              "content": "Old Content",
              "published": false
            }
            """;

        var response = mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andReturn();

        var json = response.getResponse().getContentAsString();
        var id = json.replaceAll(".*\"id\":(\\d+).*", "$1");

        var updateBody = """
            {
              "title": "New Title",
              "content": "Updated Content",
              "published": true
            }
            """;

        mockMvc.perform(put("/api/posts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.content").value("Updated Content"))
                .andExpect(jsonPath("$.published").value(true));
    }

    @Test
    void deletePost_returns204() throws Exception {

        var body = """
            {
              "title": "Delete Post",
              "content": "Post for delete",
              "published": false
            }
            """;

        var response = mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();

        var json = response.getResponse().getContentAsString();
        var id = json.replaceAll(".*\"id\":(\\d+).*", "$1");

        mockMvc.perform(delete("/api/posts/{id}", id))
                .andExpect(status().isNoContent());
    }
}
