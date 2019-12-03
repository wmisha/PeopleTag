package com.example.getstarted.daos;

import com.example.getstarted.objects.Collection;
import com.example.getstarted.objects.PersonCollectionAssociation;
import com.example.getstarted.objects.Person;
import com.example.getstarted.objects.Result;

import java.sql.SQLException;
import java.util.List;

// [START example]
public interface PersonCollectionDao {

    Long createPersonCollectionAssociation(PersonCollectionAssociation personcollection) throws SQLException;

    PersonCollectionAssociation readPersonCollection(Long Id) throws SQLException;

    void deletePersonCollection(Long Id) throws SQLException;

    List<Person> listPersons(Long collectionId ) throws SQLException;

    List<Collection> listCollections(Long personId) throws SQLException;


}
// [END example]
