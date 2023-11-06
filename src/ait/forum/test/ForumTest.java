package ait.forum.test;

import ait.forum.dao.ForumImpl;
import ait.forum.model.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ForumTest {

    ForumImpl forum;
    Post[] posts;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {

        forum = new ForumImpl();

        posts = new Post[]{
                new Post(1, "Post 1", "Oleg", "Post 1 Content"),
                new Post(2, "Post 2", "Maksim", "Post 2 Content"),
                new Post(3, "Post 3", "Petr", "Post 3 Content"),
                new Post(4, "Post 4", "Oleg", "Post 4 Content"),
                new Post(5, "Post 5", "Oleg", "Post 5 Content"),
                new Post(6, "Post 6", "Tanya", "Post 6 Content"),
                new Post(7, "Post 7", "Petr", "Post 7 Content"),
                new Post(8, "Post 8", "Oleg", "Post 8 Content"),
                new Post(9, "Post 9", "Tanya", "Post 9 Content"),
        };

        posts[0].setDate(LocalDateTime.now().minusHours(570));
        posts[1].setDate(LocalDateTime.now().minusHours(550));
        posts[2].setDate(LocalDateTime.now().minusHours(500));
        posts[3].setDate(LocalDateTime.now().minusHours(485));
        posts[4].setDate(LocalDateTime.now().minusHours(476));
        posts[5].setDate(LocalDateTime.now().minusHours(430));
        posts[6].setDate(LocalDateTime.now().minusHours(355));
        posts[7].setDate(LocalDateTime.now().minusHours(125));
        posts[8].setDate(LocalDateTime.now().minusHours(100));

        for (int i = 0; i < posts.length; i++) {
            forum.addPost(posts[i]);
        }

    }

    @org.junit.jupiter.api.Test
    void addPost() {
        assertFalse(forum.addPost(null));
        assertFalse(forum.addPost(posts[0]));
        assertTrue(forum.addPost(new Post(10, "Post 10", "Maksim", "Post 10 Content")));
        assertEquals(10, forum.size());
    }

    @org.junit.jupiter.api.Test
    void removePost() {
        assertFalse(forum.removePost(11));
        assertTrue(forum.removePost(1));
        assertNull(forum.getPostById(1));
        assertTrue(forum.removePost(4));
        assertNull(forum.getPostById(4));
        assertEquals(7, forum.size());
    }

    @org.junit.jupiter.api.Test
    void updatePost() {
        assertTrue(forum.updatePost(1, "Hello World!"));
        assertEquals("Hello World!", forum.getPostById(1).getContent());
        assertFalse(forum.updatePost(11, "Hello World!"));
    }

    @org.junit.jupiter.api.Test
    void getPostById() {
        assertEquals(posts[3], forum.getPostById(4));
        assertEquals(posts[8], forum.getPostById(9));
        assertNull(forum.getPostById(11));
    }

    @org.junit.jupiter.api.Test
    void getPostsByAuthor() {
        Post[] actual = forum.getPostsByAuthor("Oleg");
        Post[] expected = {posts[0], posts[3], posts[4], posts[7]};
        assertArrayEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void getPostsByAuthorBetweenDates() {
        Post[] actual = forum.getPostsByAuthor("Oleg", LocalDate.now().minusDays(15), LocalDate.now().minusDays(3));
        Post[] expected = {posts[7]};
        assertArrayEquals(expected, actual);

        actual = forum.getPostsByAuthor("Tanya", LocalDate.now().minusDays(20), LocalDate.now().minusDays(3));
        expected = new Post[]{posts[5], posts[8]};
        assertArrayEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void size() {
        assertEquals(9, forum.size());
    }
}