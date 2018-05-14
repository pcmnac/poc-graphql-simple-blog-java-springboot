package com.github.pcmnac.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.github.pcmnac.graphql.bean.Post;
import com.github.pcmnac.graphql.dataloader.CommentDataLoader;
import com.github.pcmnac.graphql.dataloader.UserDataLoader;
import com.github.pcmnac.graphql.bean.Comment;
import com.github.pcmnac.graphql.bean.User;
import com.mashape.unirest.http.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class PostResolver implements GraphQLResolver<Post> {

    @Autowired
    private CommentDataLoader commentDataLoader;

    @Autowired
    private UserDataLoader userDataLoader;

    public CompletableFuture<User> getAuthor(Post post) {
        return userDataLoader.load(post.getUserId());
    }

    public CompletableFuture<List<Comment>> getComments(Post post) throws Exception {
        List<Comment> commentIds = Arrays.asList(Unirest.get("http://jsonplaceholder.typicode.com/comments?postId=" + post.getId())
                .asObject(Comment[].class).getBody());

        List<CompletableFuture<Comment>> futures = commentIds.stream()
                .map(comment -> commentDataLoader.load(comment.getId()))
                .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .thenApply(v -> futures.stream()
                        .map(future -> future.join())
                        .collect(Collectors.toList())
                );
    }
}
