package com.example.getstarted.daos;

import com.example.getstarted.objects.Post;
import com.example.getstarted.objects.CollectionPostAssociation;
import com.example.getstarted.objects.Collection;
import com.example.getstarted.objects.Result;

import java.sql.SQLException;
import java.util.List;

// [START example]
public interface CollectionPostDao {

    Long createCollectionPostAssociation(CollectionPostAssociation collectionpost) throws SQLException;

    CollectionPostAssociation readCollectionPost(Long Id) throws SQLException;

    void deleteCollectionPost(Long Id) throws SQLException;

    List<Collection> listCollections(Long postId ) throws SQLException;

    Result<Post> listPosts(Long collectionId, String startCursorString) throws SQLException;


}
// [END example]
