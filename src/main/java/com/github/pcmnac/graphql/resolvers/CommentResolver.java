package com.github.pcmnac.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.github.pcmnac.graphql.bean.Comment;
import com.github.pcmnac.graphql.bean.Post;
import com.github.pcmnac.graphql.bean.User;
import com.github.pcmnac.graphql.dataloader.PostDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class CommentResolver implements GraphQLResolver<Comment> {

    @Autowired
    private PostDataLoader postDataLoader;

    public CompletableFuture<Post> getPost(Comment comment) {
        return postDataLoader.load(comment.getPostId());
    }
}
