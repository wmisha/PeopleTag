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
public class Post {
  // [START post]
  private String title;
  private String text;

  private Long id;
  private String createdBy;
  private String createdById;
  // [END post]

  // [START keys]
  public static final String TITLE = "title";
  public static final String TEXT = "text";
  public static final String ID = "id";
  public static final String CREATED_BY = "createdBy";
  public static final String CREATED_BY_ID = "createdById";
  // [END keys]

  // [START constructor]
  // We use a Builder pattern here to simplify and standardize construction of Post objects.
  private Post(Builder builder) {
    this.title = builder.title;
    this.text = builder.text;
    this.id = builder.id;
    this.createdBy = builder.createdBy;
    this.createdById = builder.createdById;
  }
  // [END constructor]

  // [START builder]
  public static class Builder {
    private String title;
    private String text;
    private String createdBy;
    private String createdById;
    private Long id;

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder text(String text) {
      this.text = text;
      return this;
    }

    public Builder id(Long id) {
      this.id = id;
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


    public Post build() {
      return new Post(this);
    }
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  @java.lang.Override
  public java.lang.String toString() {
    return "Post{" +
            "title='" + title + '\'' +
            ", text='" + text + '\'' +
            ", id=" + id +
            ", createdBy='" + createdBy + '\'' +
            ", createdById='" + createdById + '\'' +
            '}';
  }
}
// [END example]
