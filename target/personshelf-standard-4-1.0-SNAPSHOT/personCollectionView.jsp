<!--
Copyright 2016 Google Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->
<!-- [START view] -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div class="container">
  <h3>Collection</h3>
  <div class="btn-group">
    <a href="/deletePersonCollection?id=${collection.id}" class="btn btn-danger btn-sm">
      <i class="glyphicon glyphicon-trash"></i>
      Delete collection
    </a>
  </div>

  <div class="media">
    <div class="media-left">
      <img class="collection-image" height="200" src="${fn:escapeXml(not empty collection.imageUrl?collection.imageUrl:'http://placekitten.com/g/128/192')}">
    </div>
    <div class="media-body">
      <h4 class="collection-name">
        ${fn:escapeXml(collection.name)}
      </h4>
      <p class="collection-description">${fn:escapeXml(collection.description)}</p>
      <small class="collection-added-by">Added by
        ${fn:escapeXml(not empty collection.createdBy?collection.createdBy:'Anonymous')}</small>
    </div>
  </div>
</div>
<!-- [END view] -->
