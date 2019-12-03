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


import com.example.getstarted.objects.Person;
import com.example.getstarted.objects.PersonPostAssociation;
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
public class PersonPostDatastoreDao implements PersonPostDao {

  // [START constructor]
  private DatastoreService datastore;
  private static final String PERSONPOST_KIND = "PersonPost4";
  private static final String POST_KIND = "Post4";
  private static final String PERSON_KIND = "Person4";


  public PersonPostDatastoreDao() {
    datastore = DatastoreServiceFactory.getDatastoreService(); // Lastized Datastore service
  }
  // [END constructor]

  // [START entityToPerson]
  public PersonPostAssociation entityToPersonPost(Entity entity) {
    return new PersonPostAssociation.Builder()                                     // Convert to Person form
        .personId((String) entity.getProperty(PersonPostAssociation.PERSON_ID))
        .postId((String) entity.getProperty(PersonPostAssociation.POST_ID))
        .id(entity.getKey().getId())
        .createdBy((String) entity.getProperty(PersonPostAssociation.CREATED_BY))
        .createdById((String) entity.getProperty(PersonPostAssociation.CREATED_BY_ID))
        .build();
  }
  // [END entityToPerson]


  // [START create]
  @Override
  public Long createPersonPostAssociation(PersonPostAssociation personpost) {
    Entity incPersonPostEntity = new Entity(PERSONPOST_KIND);  // Key will be assigned once written
    incPersonPostEntity.setProperty(PersonPostAssociation.PERSON_ID, personpost.getPersonId());
    incPersonPostEntity.setProperty(PersonPostAssociation.POST_ID, personpost.getPostId());
    incPersonPostEntity.setProperty(PersonPostAssociation.CREATED_BY, personpost.getCreatedBy());
    incPersonPostEntity.setProperty(PersonPostAssociation.CREATED_BY_ID, personpost.getCreatedById());

    Key personpostKey = datastore.put(incPersonPostEntity); // Save the Entity
    return personpostKey.getId();                     // The ID of the Key
  }
  // [END create]

  // [START read]
  @Override
  public PersonPostAssociation readPersonPost(Long Id) {
    try {
      Entity personpostEntity = datastore.get(KeyFactory.createKey(PERSONPOST_KIND, Id));
      return entityToPersonPost(personpostEntity);
    } catch (EntityNotFoundException e) {
      return null;
    }
  }
  // [END read]


  // [START delete]
  @Override
  public void deletePersonPost(Long Id) {
    Key key = KeyFactory.createKey(PERSONPOST_KIND, Id);        // Create the Key
    datastore.delete(key);                      // Delete the Entity
  }
  // [END delete]


  // [START entitiesToPersons]
  public List<Person> listPersons(Long postId ) {
    Query query = new Query(PERSONPOST_KIND) // We only care about Persons
            // Only for this user
            .setFilter(new Query.FilterPredicate(
                    PersonPostAssociation.POST_ID, Query.FilterOperator.EQUAL, String.valueOf(postId)))
            // a custom datastore index is required since you are filtering by one property
            // but ordering by another
           .addSort(PersonPostAssociation.PERSON_ID, SortDirection.ASCENDING);
    PreparedQuery preparedQuery = datastore.prepare(query);
    QueryResultIterator<Entity> results = preparedQuery.asQueryResultIterator();

    List<Person> persons = new ArrayList<>();
    while (results.hasNext()) {  // We still have data
      String id = String.valueOf((results.next()).getProperty(PersonPostAssociation.PERSON_ID));
      persons.add(readPerson(Long.parseLong(id))); // Add the Person to the List
    }
    return persons;

  }

  // [END entitiesToPersons]

  // [START entitiesToPersons]
  public List<PersonPostAssociation> entitiesToPersonPosts(Iterator<Entity> results) {
    List<PersonPostAssociation> resultPersonPosts = new ArrayList<>();
    while (results.hasNext()) {  // We still have data
      resultPersonPosts.add(entityToPersonPost(results.next()));      // Add the Person to the List
    }
    return resultPersonPosts;
  }
  // [END entitiesToPersons]

  @Override
  public Result<Post> listPosts(Long personId, String startCursorString) {
    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(10); // Only show 10 at a time
    if (startCursorString != null && !startCursorString.equals("")) {
      fetchOptions.startCursor(Cursor.fromWebSafeString(startCursorString)); // Where we left off
    }
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query(PERSONPOST_KIND)
            .setFilter(new Query.FilterPredicate(
                   PersonPostAssociation.PERSON_ID, Query.FilterOperator.EQUAL, String.valueOf(personId)))
            .addSort(PersonPostAssociation.POST_ID, SortDirection.ASCENDING);
    PreparedQuery preparedQuery = datastore.prepare(query);
    QueryResultIterator<Entity> results = preparedQuery.asQueryResultIterator(fetchOptions);
    List<Post> personPosts = new ArrayList<>();
    while (results.hasNext()) {
      String id = String.valueOf((results.next()).getProperty(PersonPostAssociation.POST_ID));
      personPosts.add(readPost(Long.parseLong(id)));
          // Add the Person to the List
    }
    Cursor cursor = results.getCursor();              // Where to start next time
    if (cursor != null && personPosts.size() == 10) {         // Are we paging? Save Cursor
      String cursorString = cursor.toWebSafeString();               // Cursors are WebSafe
      return new Result<>(personPosts, cursorString);
    } else {
      return new Result<>(personPosts);
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

  // [START entityToPerson]
  public Post entityToPost(Entity entity) {
    return new Post.Builder()                                     // Convert to Person form
            .title((String) entity.getProperty(Post.TITLE))
            .text((String) entity.getProperty(Post.TEXT))
            .id(entity.getKey().getId()).build();
  }
  // [END entityToPerson]

  // [START read]
  public Person readPerson(Long personId) {
    try {
      Entity personEntity = datastore.get(KeyFactory.createKey(PERSON_KIND, personId));
      return entityToPerson(personEntity);
    } catch (EntityNotFoundException e) {
      return null;
    }
  }
  // [END read]

  // [START entityToPerson]
  public Person entityToPerson(Entity entity) {
    return new Person.Builder()                                     // Convert to Person form
            .last((String) entity.getProperty(Person.LAST))
            .jobTitle((String) entity.getProperty(Person.JOB_TITLE))
            .id(entity.getKey().getId())
            .imageUrl((String) entity.getProperty(Person.IMAGE_URL))
            .createdBy((String) entity.getProperty(Person.CREATED_BY))
            .createdById((String) entity.getProperty(Person.CREATED_BY_ID))
            .first((String) entity.getProperty(Person.FIRST))
            .description((String) entity.getProperty(Person.DESCRIPTION))
            .facebook((String) entity.getProperty(Person.FACEBOOK))
            .instagram((String) entity.getProperty(Person.INSTAGRAM))
            .linkedIn((String) entity.getProperty(Person.LINKEDIN))
            .twitter((String) entity.getProperty(Person.TWITTER)).build();
  }
  // [END entityToPerson]
}
// [END example]
