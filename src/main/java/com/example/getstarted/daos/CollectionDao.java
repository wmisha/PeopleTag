package com.example.getstarted.daos;

import com.example.getstarted.objects.Collection;
import com.example.getstarted.objects.Result;
import java.util.List;

import java.sql.SQLException;

// [START example]
public interface CollectionDao {
    Long createCollection(Collection collection) throws SQLException;

    Collection readCollection(Long CollectionId) throws SQLException;

    void deleteCollection(Long collectionId) throws SQLException;

    Result<Collection> listCollections(String startCursor) throws SQLException;

    Result<Collection> listCollectionsByUser(String collectionId, String startCursor) throws SQLException;

    List<Collection> listAllCollections() throws SQLException;
}
// [END example]
