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
public class CollectionPostAssociation {
  // [START collection]
  private String collectionId;
  private String postId;
  private String createdBy;
  private String createdById;
  private Long id;
  // [END collection]

  // [START keys]
  public static final String COLLECTION_ID = "collectionId";
  public static final String POST_ID = "postId";
  public static final String CREATED_BY = "createdBy";
  public static final String CREATED_BY_ID = "createdById";
  public static final String ID = "id";
  // [END keys]

  // [START constructor]
  // We use a Builder pattern here to simplify and standardize construction of Collection objects.
  private CollectionPostAssociation(Builder builder) {
    this.collectionId = builder.collectionId;
    this.postId = builder.postId;
    this.createdBy = builder.createdBy;
    this.createdById = builder.createdById;
    this.id = builder.id;
  }
  // [END constructor]

  // [START builder]
  public static class Builder {
    private String collectionId;
    private String postId;
    private String createdBy;
    private String createdById;
    private Long id;

    public Builder collectionId(String collectionId) {
      this.collectionId = collectionId;
      return this;
    }

    public Builder postId(String postId) {
      this.postId = postId;
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


    public CollectionPostAssociation build() {
      return new CollectionPostAssociation(this);
    }
  }

  public String getCollectionId() {
    return collectionId;
  }

  public void setCollectionId(String collectionId) {
    this.collectionId = collectionId;
  }

  public String getPostId() {
    return postId;
  }

  public void setPostId(String postId) {
    this.postId = postId;
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
    return "CollectionPostAssociation{" +
            "postId='" + postId + '\'' +
            ", collectionId='" + collectionId + '\'' +
            ", createdBy='" + createdBy + '\'' +
            ", createdById='" + createdById + '\'' +
            ", id=" + id +
            '}';
  }
}
// [END example]
