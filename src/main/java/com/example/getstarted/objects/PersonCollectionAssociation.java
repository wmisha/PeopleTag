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

package com.example.getstarted.objects;

// [START example]
public class PersonCollectionAssociation {
  // [START person]
  private String collectionId;
  private String personId;
  private String createdBy;
  private String createdById;
  private Long id;
  // [END person]

  // [START keys]
  public static final String PERSON_ID = "personId";
  public static final String COLLECTION_ID = "collectionId";
  public static final String CREATED_BY = "createdBy";
  public static final String CREATED_BY_ID = "createdById";
  public static final String ID = "id";
  // [END keys]

  // [START constructor]
  // We use a Builder pattern here to simplify and standardize construction of Person objects.
  private PersonCollectionAssociation(Builder builder) {
    this.collectionId = builder.collectionId;
    this.personId = builder.personId;
    this.createdBy = builder.createdBy;
    this.createdById = builder.createdById;
    this.id = builder.id;
  }
  // [END constructor]

  // [START builder]
  public static class Builder {
    private String personId;
    private String collectionId;
    private String createdBy;
    private String createdById;
    private Long id;

    public Builder personId(String personId) {
      this.personId = personId;
      return this;
    }

    public Builder collectionId(String collectionId) {
      this.collectionId = collectionId;
      return this;
    }

    public Builder createdBy(String createdBy) {
      this.createdBy = createdBy;
      return this;
    }

    public Builder createdById(String createdById) {
      this.createdById = createdById;
      return this;
    }


    public Builder id(Long id) {
      this.id = id;
      return this;
    }


    public PersonCollectionAssociation build() {
      return new PersonCollectionAssociation(this);
    }
  }

  public String getPersonId() {
    return personId;
  }

  public void setPersonId(String personId) {
    this.personId = personId;
  }

  public String getCollectionId() {
    return collectionId;
  }

  public void setCollectionId(String collectionId) {
    this.collectionId = collectionId;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getCreatedById() {
    return createdById;
  }

  public void setCreatedById(String createdById) {
    this.createdById = createdById;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @java.lang.Override
  public java.lang.String toString() {
    return "PersonCollectionAssociation{" +
            "collectionId='" + collectionId + '\'' +
            ", personId='" + personId + '\'' +
            ", createdBy='" + createdBy + '\'' +
            ", createdById='" + createdById + '\'' +
            ", id=" + id +
            '}';
  }
}
// [END example]
