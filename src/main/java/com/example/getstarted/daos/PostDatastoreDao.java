/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.getstarted.daos;

import com.example.getstarted.objects.Post;
import com.example.getstarted.objects.Result;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// [START example]
public class PostDatastoreDao implements PostDao {

  // [START constructor]
  private DatastoreService datastore;
  private static final String POST_KIND = "Post4";

  public PostDatastoreDao() {
    datastore = DatastoreServiceFactory.getDatastoreService(); // Lastized Datastore service
  }
  // [END constructor]

  // [START entityToPost]
  public Post entityToPost(Entity entity) {
    return new Post.Builder()                                     // Convert to Post form
        .id(entity.getKey().getId())
        .title((String) entity.getProperty(Post.TITLE))
        .text((String) entity.getProperty(Post.TEXT))
        .createdBy((String) entity.getProperty(Post.CREATED_BY))
        .createdById((String) entity.getProperty(Post.CREATED_BY_ID)).build();
  }
  // [END entityToPost]

  // [START create]
  @Override
  public Long createPost(Post post) {
    Entity incPostEntity = new Entity(POST_KIND);  // Key will be assigned once written
    incPostEntity.setProperty(Post.TEXT, post.getText());
    incPostEntity.setProperty(Post.TITLE, post.getTitle());
    incPostEntity.setProperty(Post.CREATED_BY, post.getCreatedBy());
    incPostEntity.setProperty(Post.CREATED_BY_ID, post.getCreatedById());

    Key postKey = datastore.put(incPostEntity); // Save the Entity
    return postKey.getId();                     // The ID of the Key
  }
  // [END create]

  // [START read]
  @Override
  public Post readPost(Long postId) {
    try {
      Entity postEntity = datastore.get(KeyFactory.createKey(POST_KIND, postId));
      System.out.println(postEntity);
      return entityToPost(postEntity);
    } catch (EntityNotFoundException e) {
      return null;
    }
  }
  // [END read]

  // [START update]
  @Override
  public void updatePost(Post post) {
    Key key = KeyFactory.createKey(POST_KIND, post.getId());  // From a post, create a Key
    Entity entity = new Entity(key);         // Convert Post to an Entity
    entity.setProperty(Post.TEXT, post.getText());
    entity.setProperty(Post.TITLE, post.getTitle());
    entity.setProperty(Post.CREATED_BY, post.getCreatedBy());
    entity.setProperty(Post.CREATED_BY_ID, post.getCreatedById());
    datastore.put(entity);                   // Update the Entity
  }
  // [END update]

  // [START delete]
  @Override
  public void deletePost(Long postId) {
    Key key = KeyFactory.createKey(POST_KIND, postId);        // Create the Key
    datastore.delete(key);                      // Delete the Entity
  }
  // [END delete]

  // [START entitiesToPosts]
  public List<Post> entitiesToPosts(Iterator<Entity> results) {
    List<Post> resultPosts = new ArrayList<>();
    while (results.hasNext()) {  // We still have data
      resultPosts.add(entityToPost(results.next()));      // Add the Post to the List
    }
    return resultPosts;
  }
  // [END entitiesToPosts]

  // [START listposts]
  @Override
  public Result<Post> listPosts(String startCursorString) {
    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(10); // Only show 10 at a time
    if (startCursorString != null && !startCursorString.equals("")) {
      fetchOptions.startCursor(Cursor.fromWebSafeString(startCursorString)); // Where we left off
    }
    Query query = new Query(POST_KIND); // We only care about Posts
       //.addSort(Post.ID, SortDirection.DESCENDING); // Use default Index "first"
    PreparedQuery preparedQuery = datastore.prepare(query);

    QueryResultIterator<Entity> results = preparedQuery.asQueryResultIterator(fetchOptions);
    List<Post> resultPosts = entitiesToPosts(results);     // Retrieve and convert Entities
    System.out.println(resultPosts.size());
    Cursor cursor = results.getCursor();              // Where to start next time
    if (cursor != null && resultPosts.size() == 10) {         // Are we paging? Save Cursor
      String cursorString = cursor.toWebSafeString();               // Cursors are WebSafe
      return new Result<>(resultPosts, cursorString);
    } else {
      return new Result<>(resultPosts);
    }
  }
  // [END listposts]
}
// [END example]
