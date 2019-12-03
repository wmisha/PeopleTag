package com.example.getstarted.daos;

import com.example.getstarted.objects.Collection;
import com.example.getstarted.objects.Person;
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
public class CollectionDatastoreDao implements CollectionDao {

    // [START constructor]
    private DatastoreService datastore;
    private static final String COLLECTION_KIND = "Collection4";

    public CollectionDatastoreDao() {
        datastore = DatastoreServiceFactory.getDatastoreService(); // Lastized Datastore service
    }
    // [END constructor]

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

    // [START create]
    @Override
    public Long createCollection(Collection collection) {
        Entity incCollectionEntity = new Entity(COLLECTION_KIND);  // Key will be assigned once written
        incCollectionEntity.setProperty(Collection.NAME, collection.getName());
        incCollectionEntity.setProperty(Collection.DESCRIPTION, collection.getDescription());
        incCollectionEntity.setProperty(Collection.IMAGE_URL, collection.getImageUrl());
        incCollectionEntity.setProperty(Collection.CREATED_BY, collection.getCreatedBy());
        incCollectionEntity.setProperty(Collection.CREATED_BY_ID, collection.getCreatedById());


        Key collectionKey = datastore.put(incCollectionEntity); // Save the Entity
        return collectionKey.getId();                     // The ID of the Key
    }
    // [END create]

    // [START read]
    @Override
    public Collection readCollection(Long collectionId) {
        try {
            Entity collectionEntity = datastore.get(KeyFactory.createKey(COLLECTION_KIND, collectionId));
            return entityToCollection(collectionEntity);
        } catch (EntityNotFoundException e) {
            return null;
        }
    }
    // [END read]


    // [START delete]
    @Override
    public void deleteCollection(Long collectionId) {
        Key key = KeyFactory.createKey(COLLECTION_KIND, collectionId);        // Create the Key
        datastore.delete(key);                      // Delete the Entity
    }
    // [END delete]

    // [START entitiesToPersons]
    public List<Collection> entitiesToCollections(Iterator<Entity> results) {
        List<Collection> resultCollections = new ArrayList<>();
        while (results.hasNext()) {  // We still have data
            resultCollections.add(entityToCollection(results.next()));      // Add the Person to the List
        }
        return resultCollections;
    }
    // [END entitiesToPersons]

    // [START listpersons]
    @Override
    public Result<Collection> listCollections(String startCursorString) {
        FetchOptions fetchOptions = FetchOptions.Builder.withLimit(10); // Only show 10 at a time
        if (startCursorString != null && !startCursorString.equals("")) {
            fetchOptions.startCursor(Cursor.fromWebSafeString(startCursorString)); // Where we left off
        }
        Query query = new Query(COLLECTION_KIND) // We only care about Collections
                .addSort(Collection.NAME, SortDirection.ASCENDING); // Use default Index "first"
        PreparedQuery preparedQuery = datastore.prepare(query);
        QueryResultIterator<Entity> results = preparedQuery.asQueryResultIterator(fetchOptions);

        List<Collection> resultCollections = entitiesToCollections(results);     // Retrieve and convert Entities
        Cursor cursor = results.getCursor();              // Where to start next time
        if (cursor != null && resultCollections.size() == 10) {         // Are we paging? Save Cursor
            String cursorString = cursor.toWebSafeString();               // Cursors are WebSafe
            return new Result<>(resultCollections, cursorString);
        } else {
            return new Result<>(resultCollections);
        }
    }
    // [END listpersons]

    // [START listbyuser]
    @Override
    public Result<Collection> listCollectionsByUser(String userId, String startCursorString) {
        FetchOptions fetchOptions = FetchOptions.Builder.withLimit(10); // Only show 10 at a time
        if (startCursorString != null && !startCursorString.equals("")) {
            fetchOptions.startCursor(Cursor.fromWebSafeString(startCursorString)); // Where we left off
        }
        Query query = new Query(COLLECTION_KIND) // We only care about Persons
                // Only for this user
                .setFilter(new Query.FilterPredicate(
                        Collection.CREATED_BY_ID, Query.FilterOperator.EQUAL, userId))
                // a custom datastore index is required since you are filtering by one property
                // but ordering by another
                .addSort(Collection.NAME, SortDirection.ASCENDING);
        PreparedQuery preparedQuery = datastore.prepare(query);
        QueryResultIterator<Entity> results = preparedQuery.asQueryResultIterator(fetchOptions);

        List<Collection> resultCollections = entitiesToCollections(results);     // Retrieve and convert Entities
        Cursor cursor = results.getCursor();              // Where to start next time
        if (cursor != null && resultCollections.size() == 10) {         // Are we paging? Save Cursor
            String cursorString = cursor.toWebSafeString();               // Cursors are WebSafe
            return new Result<>(resultCollections, cursorString);
        } else {
            return new Result<>(resultCollections);
        }
    }
    // [END listbyuser]

    @Override
    public List<Collection> listAllCollections() {
        Query query = new Query(COLLECTION_KIND) // We only care about Collections
                .addSort(Collection.NAME, SortDirection.ASCENDING); // Use default Index "first"
        PreparedQuery preparedQuery = datastore.prepare(query);

        QueryResultIterator<Entity> results = preparedQuery.asQueryResultIterator();
        return entitiesToCollections(results);     // Retrieve and convert Entities
    }
}
// [END example]
