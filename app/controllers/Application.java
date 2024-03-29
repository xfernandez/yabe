package controllers;

import java.util.List;

import models.Post;
import play.Play;
import play.cache.Cache;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller
{

    @Before
    static void addDefaults()
    {
        renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
        renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
    }

    public static void index()
    {
        Post frontPost = Post.find("order by postedAt desc").first();
        List<Post> olderPosts = Post.find("order by postedAt desc").from(1).fetch(10);
        render(frontPost, olderPosts);

    }

    public static void show(final Long id)
    {
        Post post = Post.findById(id);
        String randomID = Codec.UUID();
        render(post, randomID);

    }

    public static void postComment(final Long postId,
        @Required(message = "Author is required") final String author,
        @Required(message = "A message is required") final String content,
        @Required(message = "Please type the code") final String code, final String randomID)
    {
        Post post = Post.findById(postId);
        if (!Play.id.equals("test"))
        {
            validation.equals(code, Cache.get(randomID)).message(
                "Invalid code. Please type it again");
        }

        if (validation.hasErrors())
        {
            render("Application/show.html", post, randomID);
        }
        post.addComment(author, content);
        flash.success("Thanks for posting %s", author);
        Cache.delete(randomID);
        show(postId);
    }

    public static void captcha(final String id)
    {
        Images.Captcha captcha = Images.captcha();
        String code = captcha.getText("#E4EAFD");
        Cache.set(id, code, "10mn");
        renderBinary(captcha);
    }

    public static void listTagged(final String tag)
    {
        List<Post> posts = Post.findTaggedWith(tag);
        render(tag, posts);
    }

}
