package com.example.getstarted.daos;

import com.example.getstarted.objects.Post;
import com.example.getstarted.objects.PersonPostAssociation;
import com.example.getstarted.objects.Person;
import com.example.getstarted.objects.Result;

import java.sql.SQLException;
import java.util.List;

// [START example]
public interface PersonPostDao {

    Long createPersonPostAssociation(PersonPostAssociation personpost) throws SQLException;

    PersonPostAssociation readPersonPost(Long Id) throws SQLException;

    void deletePersonPost(Long Id) throws SQLException;

    List<Person> listPersons(Long postId ) throws SQLException;

    Result<Post> listPosts(Long personId, String startCursorString) throws SQLException;


}
// [END example]
