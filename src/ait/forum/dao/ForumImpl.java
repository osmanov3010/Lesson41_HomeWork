package ait.forum.dao;

import ait.forum.model.Post;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

public class ForumImpl implements Forum {
    private Post[] posts;
    private int size;
    private Comparator<Post> comparator = (p1, p2) -> {

        int res = p1.getAuthor().compareTo(p2.getAuthor());
        return res != 0 ? res : p1.getDate().compareTo(p2.getDate());

    };

    @Override
    public boolean addPost(Post post) {

        if (posts == null) {
            posts = new Post[1];
        }

        if (post == null || getPostById(post.getPostId()) != null) {
            return false;
        }

        if (posts.length == size) {
            Post[] arrayForMorePosts = new Post[size + 100];
            System.arraycopy(posts, 0, arrayForMorePosts, 0, size);
            posts = arrayForMorePosts;
        }

        int index = Arrays.binarySearch(posts, 0, size, post, comparator);
        index = index >= 0 ? index : -index - 1;

        System.arraycopy(posts, index, posts, index + 1, size - index);
        posts[index] = post;
        size++;

        return true;
    }

    @Override
    public boolean removePost(int postId) {
        for (int i = 0; i < size; i++) {
            if (postId == posts[i].getPostId()) {
                System.arraycopy(posts, i + 1, posts, i, size - i);
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updatePost(int postId, String postContent) {
        Post post = getPostById(postId);

        if (post != null) {
            post.setContent(postContent);
            return true;
        }

        return false;
    }

    @Override
    public Post getPostById(int postId) {
        for (int i = 0; i < size; i++) {
            if (postId == posts[i].getPostId()) {
                return posts[i];
            }
        }
        return null;
    }

    @Override
    public Post[] getPostsByAuthor(String author) {
        return getPostsByAuthor(author, LocalDate.MIN, LocalDate.MAX);
    }

    @Override
    public Post[] getPostsByAuthor(String author, LocalDate dateFrom, LocalDate dateTo) {

        Post pattern = new Post(0, null, author, null);
        pattern.setDate(dateFrom.atStartOfDay());
        int from = Arrays.binarySearch(posts, 0, size, pattern, comparator);
        from = from < 0 ? -from - 1 : from;

        pattern = new Post(0, null, author, null);
        pattern.setDate(dateTo.atTime(LocalTime.MAX));
        int to = Arrays.binarySearch(posts, 0, size, pattern, comparator);
        to = to < 0 ? -to - 1 : to;

        return Arrays.copyOfRange(posts, from, to);
    }

    @Override
    public int size() {
        return size;
    }

}
