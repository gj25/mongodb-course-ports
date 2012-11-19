package models;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.PostData.Comment;
import play.Logger;

public class Posts {

    public List<PostData> getPosts() {
        List<PostData> posts = new ArrayList();
        DBCollection postsColl = MongoDB.getCollection("posts");
        DBObject sort = new BasicDBObject();
        sort.put("date", 1);
        DBCursor cursor = postsColl.find().sort(sort).limit(10);
        try {
            while (cursor.hasNext()) {
                PostData post = Posts.dbPostToObj(cursor.next());
                posts.add(post);
            }
        }
        finally {
            cursor.close();
        }
        return posts;
    }
    
    public PostData getByPermalink(String permalink)
    {
        DBCollection postsColl = MongoDB.getCollection("posts");
        DBObject query = new BasicDBObject();
        query.put("permalink", permalink);
        DBObject dbPost = postsColl.findOne(query); 
        if(dbPost == null)
        {
            Logger.info("Post with permalink not in database: " + permalink);
            return null;
        }
        
        return Posts.dbPostToObj(dbPost);
    }
    
    private static PostData dbPostToObj(DBObject dbPost) {
        PostData post = new PostData();
        
        post.setTitle((String) dbPost.get("title"));
        post.setAuthor((String) dbPost.get("author"));
        post.setBody((String) dbPost.get("body"));
        post.setPermalink((String) dbPost.get("permalink"));
        post.setPostDate((Date) dbPost.get("date"));
        post.setTags((List<String>) dbPost.get("tags"));
        List<DBObject> dbComments = (List<DBObject>) dbPost.get("comments");
        if (dbComments != null) {
            List<PostData.Comment> comments = new ArrayList();
            for (DBObject dbComment : dbComments) {
                PostData.Comment comment = new PostData.Comment();
                comment.setAuthor((String) dbComment.get("author"));
                comment.setBody((String) dbComment.get("body"));
                comments.add(comment);
            }
            post.setComments(comments);
        }
        return post;
    }

    public void addComment(String permalink, Comment comment) {
        DBCollection postsColl = MongoDB.getCollection("posts");
        DBObject query = new BasicDBObject();
        query.put("permalink", permalink);
        
        DBObject dbComment = new BasicDBObject();
        dbComment.put("author", comment.getAuthor());
        if (comment.getEmail() != null) {
            dbComment.put("email", comment.getEmail());
        }
        dbComment.put("body", comment.getBody());
        
        BasicDBObject updateCommand = new BasicDBObject();
        updateCommand.put( "$push", new BasicDBObject( "comments", dbComment ) );
        
        postsColl.findAndModify(query, updateCommand);
    }

    public String insertEntry(String username, String title, String post, List<String> tagList) {  
        Logger.info("inserting blog entry: " + title + " " + post);
        
        String permalink = title.replaceAll("\\s", "_").replaceAll("\\W", "");      
        DBObject doc = new BasicDBObject();
        doc.put("title", title);
        doc.put("author", username);
        doc.put("body", post);
        doc.put("permalink", permalink);
        doc.put("date", new Date());
        doc.put("tags", tagList);
        DBCollection postsColl = MongoDB.getCollection("posts");
        postsColl.insert(doc, WriteConcern.SAFE);
        
        return permalink;
    }

}
