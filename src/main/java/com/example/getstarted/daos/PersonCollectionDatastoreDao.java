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
import com.example.getstarted.objects.PersonCollectionAssociation;
import com.example.getstarted.objects.Collection;
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
public class PersonCollectionDatastoreDao implements PersonCollectionDao {

  // [START constructor]
  private DatastoreService datastore;
  private static final String PERSONCOLLECTION_KIND = "PersonCollection4";
  private static final String COLLECTION_KIND = "Collection4";
  private static final String PERSON_KIND = "Person4";


  public PersonCollectionDatastoreDao() {
    datastore = DatastoreServiceFactory.getDatastoreService(); // Lastized Datastore service
  }
  // [END constructor]

  // [START entityToPerson]
  public PersonCollectionAssociation entityToPersonCollection(Entity entity) {
    return new PersonCollectionAssociation.Builder()                                     // Convert to Person form
        .personId((String) entity.getProperty(PersonCollectionAssociation.PERSON_ID))
        .collectionId((String) entity.getProperty(PersonCollectionAssociation.COLLECTION_ID))
        .id(entity.getKey().getId())
        .createdBy((String) entity.getProperty(PersonCollectionAssociation.CREATED_BY))
        .createdById((String) entity.getProperty(PersonCollectionAssociation.CREATED_BY_ID))
        .build();
  }
  // [END entityToPerson]


  // [START create]
  @Override
  public Long createPersonCollectionAssociation(PersonCollectionAssociation personcollection) {
    Entity incPersonCollectionEntity = new Entity(PERSONCOLLECTION_KIND);  // Key will be assigned once written
    incPersonCollectionEntity.setProperty(PersonCollectionAssociation.PERSON_ID, personcollection.getPersonId());
    incPersonCollectionEntity.setProperty(PersonCollectionAssociation.COLLECTION_ID, personcollection.getCollectionId());
    incPersonCollectionEntity.setProperty(PersonCollectionAssociation.CREATED_BY, personcollection.getCreatedBy());
    incPersonCollectionEntity.setProperty(PersonCollectionAssociation.CREATED_BY_ID, personcollection.getCreatedById());

    Key personcollectionKey = datastore.put(incPersonCollectionEntity); // Save the Entity
    return personcollectionKey.getId();                     // The ID of the Key
  }
  // [END create]

  // [START read]
  @Override
  public PersonCollectionAssociation readPersonCollection(Long Id) {
    try {
      Entity personcollectionEntity = datastore.get(KeyFactory.createKey(PERSONCOLLECTION_KIND, Id));
      return entityToPersonCollection(personcollectionEntity);
    } catch (EntityNotFoundException e) {
      return null;
    }
  }
  // [END read]


  // [START delete]
  @Override
  public void deletePersonCollection(Long Id) {
    Key key = KeyFactory.createKey(PERSONCOLLECTION_KIND, Id);        // Create the Key
    datastore.delete(key);                      // Delete the Entity
  }
  // [END delete]


  // [START entitiesToPersons]
  public List<Person> listPersons(Long collectionId ) {
    Query query = new Query(PERSONCOLLECTION_KIND) // We only care about Persons
            // Only for this user
            .setFilter(new Query.FilterPredicate(
                    PersonCollectionAssociation.COLLECTION_ID, Query.FilterOperator.EQUAL, String.valueOf(collectionId)))
            // a custom datastore index is required since you are filtering by one property
            // but ordering by another
           .addSort(PersonCollectionAssociation.PERSON_ID, SortDirection.ASCENDING);
    PreparedQuery preparedQuery = datastore.prepare(query);
    QueryResultIterator<Entity> results = preparedQuery.asQueryResultIterator();

    List<Person> persons = new ArrayList<>();
    while (results.hasNext()) {  // We still have data
      String id = String.valueOf((results.next()).getProperty(PersonCollectionAssociation.PERSON_ID));
      persons.add(readPerson(Long.parseLong(id))); // Add the Person to the List
    }
    return persons;

  }

  // [END entitiesToPersons]

  // [START entitiesToPersons]
  public List<PersonCollectionAssociation> entitiesToPersonCollections(Iterator<Entity> results) {
    List<PersonCollectionAssociation> resultPersonCollections = new ArrayList<>();
    while (results.hasNext()) {  // We still have data
      resultPersonCollections.add(entityToPersonCollection(results.next()));      // Add the Person to the List
    }
    return resultPersonCollections;
  }
  // [END entitiesToPersons]

  public List<Collection> listCollections(Long personId) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query(PERSONCOLLECTION_KIND)
            .setFilter(new Query.FilterPredicate(
                   PersonCollectionAssociation.PERSON_ID, Query.FilterOperator.EQUAL, String.valueOf(personId)))
            .addSort(PersonCollectionAssociation.COLLECTION_ID, SortDirection.ASCENDING);
    PreparedQuery preparedQuery = datastore.prepare(query);
    QueryResultIterator<Entity> results = preparedQuery.asQueryResultIterator();
    List<Collection> PersonCollections = new ArrayList<>();
    while (results.hasNext()) {
      String id = String.valueOf((results.next()).getProperty(PersonCollectionAssociation.COLLECTION_ID));
      PersonCollections.add(readCollection(Long.parseLong(id)));
          // Add the Person to the List
    }
    return PersonCollections;

  }

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

  // [START entityToPerson]
  public Collection entityToCollection(Entity entity) {
    return new Collection.Builder()                                     // Convert to Person form
            .name((String) entity.getProperty(Collection.NAME))
            .id(entity.getKey().getId())
            .createdBy((String) entity.getProperty(Collection.CREATED_BY))
            .createdById((String) entity.getProperty(Collection.CREATED_BY_ID))
            .imageUrl((String) entity.getProperty(Collection.IMAGE_URL))
            .description((String) entity.getProperty(Collection.DESCRIPTION)).build();
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
