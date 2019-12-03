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


import com.example.getstarted.objects.Collection;
import com.example.getstarted.objects.CollectionPostAssociation;
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
public class CollectionPostDatastoreDao implements CollectionPostDao {

  // [START constructor]
  private DatastoreService datastore;
  private static final String COLLECTIONPOST_KIND = "CollectionPost4";
  private static final String POST_KIND = "Post4";
  private static final String COLLECTION_KIND = "Collection4";


  public CollectionPostDatastoreDao() {
    datastore = DatastoreServiceFactory.getDatastoreService(); // Lastized Datastore service
  }
  // [END constructor]

  // [START entityToCollection]
  public CollectionPostAssociation entityToCollectionPost(Entity entity) {
    return new CollectionPostAssociation.Builder()                                     // Convert to Collection form
        .collectionId((String) entity.getProperty(CollectionPostAssociation.COLLECTION_ID))
        .postId((String) entity.getProperty(CollectionPostAssociation.POST_ID))
        .id(entity.getKey().getId())
        .createdBy((String) entity.getProperty(CollectionPostAssociation.CREATED_BY))
        .createdById((String) entity.getProperty(CollectionPostAssociation.CREATED_BY_ID))
        .build();
  }
  // [END entityToCollection]


  // [START create]
  @Override
  public Long createCollectionPostAssociation(CollectionPostAssociation collectionpost) {
    Entity incCollectionPostEntity = new Entity(COLLECTIONPOST_KIND);  // Key will be assigned once written
    incCollectionPostEntity.setProperty(CollectionPostAssociation.COLLECTION_ID, collectionpost.getCollectionId());
    incCollectionPostEntity.setProperty(CollectionPostAssociation.POST_ID, collectionpost.getPostId());
    incCollectionPostEntity.setProperty(CollectionPostAssociation.CREATED_BY, collectionpost.getCreatedBy());
    incCollectionPostEntity.setProperty(CollectionPostAssociation.CREATED_BY_ID, collectionpost.getCreatedById());

    Key collectionpostKey = datastore.put(incCollectionPostEntity); // Save the Entity
    return collectionpostKey.getId();                     // The ID of the Key
  }
  // [END create]

  // [START read]
  @Override
  public CollectionPostAssociation readCollectionPost(Long Id) {
    try {
      Entity collectionpostEntity = datastore.get(KeyFactory.createKey(COLLECTIONPOST_KIND, Id));
      return entityToCollectionPost(collectionpostEntity);
    } catch (EntityNotFoundException e) {
      return null;
    }
  }
  // [END read]


  // [START delete]
  @Override
  public void deleteCollectionPost(Long Id) {
    Key key = KeyFactory.createKey(COLLECTIONPOST_KIND, Id);        // Create the Key
    datastore.delete(key);                      // Delete the Entity
  }
  // [END delete]


  // [START entitiesToCollections]
  public List<Collection> listCollections(Long postId ) {
    Query query = new Query(COLLECTIONPOST_KIND) // We only care about Collections
            // Only for this user
            .setFilter(new Query.FilterPredicate(
                    CollectionPostAssociation.POST_ID, Query.FilterOperator.EQUAL, String.valueOf(postId)))
            // a custom datastore index is required since you are filtering by one property
            // but ordering by another
           .addSort(CollectionPostAssociation.COLLECTION_ID, SortDirection.ASCENDING);
    PreparedQuery preparedQuery = datastore.prepare(query);
    QueryResultIterator<Entity> results = preparedQuery.asQueryResultIterator();

    List<Collection> collections = new ArrayList<>();
    while (results.hasNext()) {  // We still have data
      String id = String.valueOf((results.next()).getProperty(CollectionPostAssociation.COLLECTION_ID));
      collections.add(readCollection(Long.parseLong(id))); // Add the Collection to the List
    }
    return collections;

  }

  // [END entitiesToCollections]

  // [START entitiesToCollections]
  public List<CollectionPostAssociation> entitiesToCollectionPosts(Iterator<Entity> results) {
    List<CollectionPostAssociation> resultCollectionPosts = new ArrayList<>();
    while (results.hasNext()) {  // We still have data
      resultCollectionPosts.add(entityToCollectionPost(results.next()));      // Add the Collection to the List
    }
    return resultCollectionPosts;
  }
  // [END entitiesToCollections]

  @Override
  public Result<Post> listPosts(Long collectionId, String startCursorString) {
    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(10); // Only show 10 at a time
    if (startCursorString != null && !startCursorString.equals("")) {
      fetchOptions.startCursor(Cursor.fromWebSafeString(startCursorString)); // Where we left off
    }
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query(COLLECTIONPOST_KIND)
            .setFilter(new Query.FilterPredicate(
                    CollectionPostAssociation.COLLECTION_ID, Query.FilterOperator.EQUAL, String.valueOf(collectionId)))
            .addSort(CollectionPostAssociation.POST_ID, SortDirection.ASCENDING);
    PreparedQuery preparedQuery = datastore.prepare(query);
    QueryResultIterator<Entity> results = preparedQuery.asQueryResultIterator(fetchOptions);
    List<Post> collectionPosts = new ArrayList<>();
    while (results.hasNext()) {
      String id = String.valueOf((results.next()).getProperty(CollectionPostAssociation.POST_ID));
      collectionPosts.add(readPost(Long.parseLong(id)));
      // Add the Collection to the List
    }
    Cursor cursor = results.getCursor();              // Where to start next time
    if (cursor != null && collectionPosts.size() == 10) {         // Are we paging? Save Cursor
      String cursorString = cursor.toWebSafeString();               // Cursors are WebSafe
      return new Result<>(collectionPosts, cursorString);
    } else {
      return new Result<>(collectionPosts);
    }
  }

  // [START read]

  public Post readPost(Long postId) {
    try {
      Entity postEntity = datastore.get(KeyFactory.createKey(POST_KIND, postId));
      return entityToPost(postEntity);
    } catch (EntityNotFoundException e) {
      return null;
    }
  }
  // [END read]

  // [START entityToCollection]
  public Post entityToPost(Entity entity) {
    return new Post.Builder()                                     // Convert to Collection form
            .title((String) entity.getProperty(Post.TITLE))
            .text((String) entity.getProperty(Post.TEXT))
            .id(entity.getKey().getId()).build();
  }
  // [END entityToCollection]

  // [START read]
  public Collection readCollection(Long collectionId) {
    try {
      Entity collectionEntity = datastore.get(KeyFactory.createKey(COLLECTION_KIND, collectionId));
      return entityToCollection(collectionEntity);
    } catch (EntityNotFoundException e) {
      return null;
    }
  }
  // [END read]

  // [START entityToCollection]
  public Collection entityToCollection(Entity entity) {
    return new Collection.Builder()                                     // Convert to Collection form
            .name((String) entity.getProperty(Collection.NAME))
            .id(entity.getKey().getId())
            .createdBy((String) entity.getProperty(Collection.CREATED_BY))
            .createdById((String) entity.getProperty(Collection.CREATED_BY_ID))
            .imageUrl((String) entity.getProperty(Collection.IMAGE_URL))
            .description((String) entity.getProperty(Collection.DESCRIPTION)).build();
  }
  // [END entityToCollection]
}
// [END example]
