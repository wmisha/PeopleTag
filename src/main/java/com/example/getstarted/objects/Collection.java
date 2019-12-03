package com.example.getstarted.objects;

// [START example]
public class Collection {
    // [START collection]
    private String name;
    private String description;
    private String createdBy;
    private String createdById;
    private Long id;
    private String imageUrl;
    // [END collection]

    // [START keys]
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String CREATED_BY = "createdBy";
    public static final String CREATED_BY_ID = "createdById";
    public static final String ID = "id";
    public static final String IMAGE_URL = "imageUrl";
    // [END keys]

    // [START constructor]

    private Collection(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.createdBy = builder.createdBy;
        this.createdById = builder.createdById;
        this.id = builder.id;
        this.imageUrl = builder.imageUrl;
    }
    // [END constructor]

    // [START builder]
    public static class Builder {
        private String name;
        private String description;
        private String createdBy;
        private String createdById;
        private Long id;
        private String imageUrl;

        public Builder name(String name) {
            this.name = name;
            return this;
        }


        public Builder description(String description) {
            this.description = description;
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


        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Collection build() {
            return new Collection(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Collection{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdById='" + createdById + '\'' +
                ", id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
// [END example]
