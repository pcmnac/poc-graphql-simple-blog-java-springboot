package com.github.pcmnac.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.github.pcmnac.graphql.bean.Post;
import com.github.pcmnac.graphql.dataloader.PostDataLoader;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.pcmnac.graphql.bean.User;
import com.github.pcmnac.graphql.utils.Futurify;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Component
public class QueryResolver implements GraphQLQueryResolver {

    @Autowired
    private PostDataLoader postDataLoader;

    public String hello() {
        return "Hello GraphQL Java (graphql-java-tools + Spring Boot + DataLoader )";
    }

    // Sync Example
    public List<Post> posts() throws Exception {
        return Arrays.asList(Unirest.get("http://jsonplaceholder.typicode.com/posts")
                .asObject(Post[].class).getBody());
    }

    public CompletableFuture<Post> post(int id) {
        return postDataLoader.load(id);
    }


    // Async example using CompletableFuture
    public CompletableFuture<List<User>> users() throws Exception {
        Future<HttpResponse<User[]>> response = Unirest.get("http://jsonplaceholder.typicode.com/users")
                .asObjectAsync(User[].class);

        return Futurify.futurify(response)
                .thenApply(users -> Arrays.asList(users.getBody()));
    }

}
