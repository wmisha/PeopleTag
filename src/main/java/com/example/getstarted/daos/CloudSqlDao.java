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
import com.example.getstarted.objects.Result;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// [START example]
public class CloudSqlDao implements PersonDao {
  // [START constructor]
  private String sqlUrl;

  /**
   * A data access object for Personshelf using a Google Cloud SQL server for storage.
   */
  public CloudSqlDao(final String url) throws SQLException {

    sqlUrl = url;
    final String createTableSql = "CREATE TABLE IF NOT EXISTS persons4 ( id INT NOT NULL "
        + "AUTO_INCREMENT, last VARCHAR(255), createdBy VARCHAR(255), createdById VARCHAR(255), "
        + "description VARCHAR(255),  first VARCHAR(255), imageUrl "
        + "VARCHAR(255), PRIMARY KEY (id))";
    try (Connection conn = DriverManager.getConnection(sqlUrl)) {
      conn.createStatement().executeUpdate(createTableSql);
    }
  }
  // [END constructor]

  // [START create]
  @Override
  public Long createPerson(Person person) throws SQLException {
    final String createPersonString = "INSERT INTO persons4 "
        + "(last, createdBy, createdById, description,  first, imageUrl) "
        + "VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = DriverManager.getConnection(sqlUrl);
         final PreparedStatement createPersonStmt = conn.prepareStatement(createPersonString,
             Statement.RETURN_GENERATED_KEYS)) {
      createPersonStmt.setString(1, person.getLast());
      createPersonStmt.setString(2, person.getCreatedBy());
      createPersonStmt.setString(3, person.getCreatedById());
      createPersonStmt.setString(4, person.getDescription());
      createPersonStmt.setString(6, person.getFirst());
      createPersonStmt.setString(7, person.getImageUrl());
      createPersonStmt.executeUpdate();
      try (ResultSet keys = createPersonStmt.getGeneratedKeys()) {
        keys.next();
        return keys.getLong(1);
      }
    }
  }
  // [END create]

  // [START read]
  @Override
  public Person readPerson(Long personId) throws SQLException {
    final String readPersonString = "SELECT * FROM persons4 WHERE id = ?";
    try (Connection conn = DriverManager.getConnection(sqlUrl);
         PreparedStatement readPersonStmt = conn.prepareStatement(readPersonString)) {
      readPersonStmt.setLong(1, personId);
      try (ResultSet keys = readPersonStmt.executeQuery()) {
        keys.next();
        return new Person.Builder()
            .last(keys.getString(Person.LAST))
            .createdBy(keys.getString(Person.CREATED_BY))
            .createdById(keys.getString(Person.CREATED_BY_ID))
            .description(keys.getString(Person.DESCRIPTION))
            .id(keys.getLong(Person.ID))
            .first(keys.getString(Person.FIRST))
            .imageUrl(keys.getString(Person.IMAGE_URL))
            .build();
      }
    }
  }
  // [END read]

  // [START update]
  @Override
  public void updatePerson(Person person) throws SQLException {
    final String updatePersonString = "UPDATE persons4 SET last = ?, createdBy = ?, createdById = ?, "
        + "description = ?,  first = ?, imageUrl = ? WHERE id = ?";
    try (Connection conn = DriverManager.getConnection(sqlUrl);
         PreparedStatement updatePersonStmt = conn.prepareStatement(updatePersonString)) {
      updatePersonStmt.setString(1, person.getLast());
      updatePersonStmt.setString(2, person.getCreatedBy());
      updatePersonStmt.setString(3, person.getCreatedById());
      updatePersonStmt.setString(4, person.getDescription());

      updatePersonStmt.setString(6, person.getFirst());
      updatePersonStmt.setString(7, person.getImageUrl());
      updatePersonStmt.setLong(8, person.getId());
      updatePersonStmt.executeUpdate();
    }
  }
  // [END update]

  // [START delete]
  @Override
  public void deletePerson(Long personId) throws SQLException {
    final String deletePersonString = "DELETE FROM persons4 WHERE id = ?";
    try (Connection conn = DriverManager.getConnection(sqlUrl);
         PreparedStatement deletePersonStmt = conn.prepareStatement(deletePersonString)) {
      deletePersonStmt.setLong(1, personId);
      deletePersonStmt.executeUpdate();
    }
  }
  // [END delete]

  // [START listpersons]
  @Override
  public Result<Person> listPersons(String cursor) throws SQLException {
    int offset = 0;
    if (cursor != null && !cursor.equals("")) {
      offset = Integer.parseInt(cursor);
    }
    final String listPersonsString = "SELECT SQL_CALC_FOUND_ROWS last, createdBy, createdById, "
        + "description, id,  first, imageUrl FROM persons4 ORDER BY first ASC "
        + "LIMIT 10 OFFSET ?";
    try (Connection conn = DriverManager.getConnection(sqlUrl);
         PreparedStatement listPersonsStmt = conn.prepareStatement(listPersonsString)) {
      listPersonsStmt.setInt(1, offset);
      List<Person> resultPersons = new ArrayList<>();
      try (ResultSet rs = listPersonsStmt.executeQuery()) {
        while (rs.next()) {
          Person person = new Person.Builder()
              .last(rs.getString(Person.LAST))
              .createdBy(rs.getString(Person.CREATED_BY))
              .createdById(rs.getString(Person.CREATED_BY_ID))
              .description(rs.getString(Person.DESCRIPTION))
              .id(rs.getLong(Person.ID))
              .first(rs.getString(Person.FIRST))
              .imageUrl(rs.getString(Person.IMAGE_URL))
              .build();
          resultPersons.add(person);
        }
      }
      try (ResultSet rs = conn.createStatement().executeQuery("SELECT FOUND_ROWS()")) {
        int totalNumRows = 0;
        if (rs.next()) {
          totalNumRows = rs.getInt(1);
        }
        if (totalNumRows > offset + 10) {
          return new Result<>(resultPersons, Integer.toString(offset + 10));
        } else {
          return new Result<>(resultPersons);
        }
      }
    }
  }
  // [END listpersons]

  // [START listbyuser]
  @Override
  public Result<Person> listPersonsByUser(String userId, String startCursor) throws SQLException {
    int offset = 0;
    if (startCursor != null && !startCursor.equals("")) {
      offset = Integer.parseInt(startCursor);
    }
    final String listPersonsString = "SELECT SQL_CALC_FOUND_ROWS last, createdBy, createdById, "
        + "description, id,  first, imageUrl FROM persons WHERE createdById = ? "
        + "ORDER BY first ASC LIMIT 10 OFFSET ?";
    try (Connection conn = DriverManager.getConnection(sqlUrl);
         PreparedStatement listPersonsStmt = conn.prepareStatement(listPersonsString)) {
      listPersonsStmt.setString(1, userId);
      listPersonsStmt.setInt(2, offset);
      List<Person> resultPersons = new ArrayList<>();
      try (ResultSet rs = listPersonsStmt.executeQuery()) {
        while (rs.next()) {
          Person person = new Person.Builder()
              .last(rs.getString(Person.LAST))
              .createdBy(rs.getString(Person.CREATED_BY))
              .createdById(rs.getString(Person.CREATED_BY_ID))
              .description(rs.getString(Person.DESCRIPTION))
              .id(rs.getLong(Person.ID))
              .first(rs.getString(Person.FIRST))
              .imageUrl(rs.getString(Person.IMAGE_URL))
              .build();
          resultPersons.add(person);
        }
      }
      try (ResultSet rs = conn.createStatement().executeQuery("SELECT FOUND_ROWS()")) {
        int totalNumRows = 0;
        if (rs.next()) {
          totalNumRows = rs.getInt(1);
        }
        if (totalNumRows > offset + 10) {
          return new Result<>(resultPersons, Integer.toString(offset + 10));
        } else {
          return new Result<>(resultPersons);
        }
      }
    }
  }
  // [END listbyuser]
}
// [END example]
