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
public class Person {
  // [START person]
  private String first;
  private String last;
  private String createdBy;
  private String createdById;
  private String jobTitle;
  private String description;

  private String facebook;
  private String instagram;
  private String linkedIn;
  private String twitter;
//  private HashMap<String, String> socialLinks;

  private Long id;
  private String imageUrl;
  // [END person]

  // [START keys]
  public static final String FIRST = "first";
  public static final String LAST = "last";
  public static final String CREATED_BY = "createdBy";
  public static final String CREATED_BY_ID = "createdById";
  public static final String JOB_TITLE = "jobTitle";
  public static final String DESCRIPTION = "description";
  public static final String FACEBOOK = "facebook";
  public static final String INSTAGRAM = "instagram";
  public static final String LINKEDIN = "linkedIn";
  public static final String TWITTER = "twitter";
//  public static final String SOCIAL_LINKS = "socialLinks";

  public static final String ID = "id";
  public static final String IMAGE_URL = "imageUrl";
  // [END keys]

  // [START constructor]
  // We use a Builder pattern here to simplify and standardize construction of Person objects.
  private Person(Builder builder) {
    this.first = builder.first;
    this.last = builder.last;
    this.createdBy = builder.createdBy;
    this.createdById = builder.createdById;
    this.jobTitle = builder.jobTitle;
    this.description = builder.description;

    this.facebook = builder.facebook;
    this.instagram = builder.instagram;
    this.linkedIn = builder.linkedIn;
    this.twitter = builder.twitter;
//    this.socialLinks = builder.socialLinks;

    this.id = builder.id;
    this.imageUrl = builder.imageUrl;
  }
  // [END constructor]

  // [START builder]
  public static class Builder {
    private String first;
    private String last;
    private String createdBy;
    private String createdById;
    private String jobTitle;
    private String publishedDate;
    private String description;

    private String facebook;
    private String instagram;
    private String linkedIn;
    private String twitter;
//    private HashMap<String, String> socialLinks;
    private Long id;
    private String imageUrl;

    public Builder first(String first) {
      this.first = first;
      return this;
    }

    public Builder last(String last) {
      this.last = last;
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

    public Builder jobTitle(String jobTitle) {
      this.jobTitle = jobTitle;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder facebook(String facebook) {
      this.facebook = facebook;
      return this;
    }

    public Builder instagram(String instagram) {
      this.instagram = instagram;
      return this;
    }

    public Builder linkedIn(String linkedIn) {
      this.linkedIn = linkedIn;
      return this;
    }

    public Builder twitter(String twitter) {
      this.twitter = twitter;
      return this;
    }

//    public Builder socialLinks(HashMap<String, String> socialLinks) {
//      this.socialLinks = socialLinks;
//      return this;
//    }

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder imageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
      return this;
    }

    public Person build() {
      return new Person(this);
    }
  }

  public String getFirst() {
    return first;
  }

  public void setFirst(String first) {
    this.first = first;
  }

  public String getLast() {
    return last;
  }

  public void setLast(String last) {
    this.last = last;
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

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getFacebook() {
    return facebook;
  }

  public void setFacebook(String facebook) {
    this.facebook = facebook;
  }

  public String getInstagram() {
    return instagram;
  }

  public void setInstagram(String instagram) {
    this.instagram = instagram;
  }

  public String getLinkedIn() {
    return linkedIn;
  }

  public void setLinkedIn(String linkedIn) {
    this.linkedIn = linkedIn;
  }

  public String getTwitter() {
    return twitter;
  }

  public void setTwitter(String twitter) {
    this.twitter = twitter;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  @java.lang.Override
  public java.lang.String toString() {
    return "Person{" +
            "first='" + first + '\'' +
            ", last='" + last + '\'' +
            ", createdBy='" + createdBy + '\'' +
            ", createdById='" + createdById + '\'' +
            ", jobTitle='" + jobTitle + '\'' +
            ", description='" + description + '\'' +
            ", facebook='" + facebook + '\'' +
            ", instagram='" + instagram + '\'' +
            ", linkedIn='" + linkedIn + '\'' +
            ", twitter='" + twitter + '\'' +
            ", id=" + id +
            ", imageUrl='" + imageUrl + '\'' +
            '}';
  }
}
// [END example]
