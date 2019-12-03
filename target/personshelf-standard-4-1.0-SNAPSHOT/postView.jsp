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
  <h3>Post</h3>
  <div class="btn-group">
    <a href="/deletePost?id=${post.id}" class="btn btn-danger btn-sm">
      <i class="glyphicon glyphicon-trash"></i>
      Delete post
    </a>
  </div>

  <div class="media">
    <div class="media-body">
      <h4 class="post-title">
        ${fn:escapeXml(post.title)}
      </h4>
      <p class="post-description">${post.text}</p>
      <small class="post-added-by">Added by
        ${fn:escapeXml(not empty post.createdBy?post.createdBy:'Anonymous')}</small>
      <hr />
      Tags: <br />
      <c:forEach items="${persons}" var="person">
        <a href="/read?id=${person.id}">#${person.first}${person.last}</a>
      </c:forEach>
      <form method="post" action="/tagperson">
        <input type="hidden" name="postId" value="${post.id}" />
        <select name="personId">
          <c:forEach items="${allpersons}" var="person">
            <option value="${person.id}">${person.first} ${person.last}</option>
          </c:forEach>
        </select>
        <input type="submit" value="Tag Person!" class="btn btn-success btn-sm" />
      </form>
      <br />
      <c:forEach items="${collections}" var="collection">
        <a href="/readCollection?id=${collection.id}">#${collection.name}</a>
      </c:forEach>
      <form method="post" action="/tagcollection">
        <input type="hidden" name="postId" value="${post.id}" />
        <select name="collectionId">
          <c:forEach items="${allcollections}" var="collection">
            <option value="${collection.id}">${collection.name}</option>
          </c:forEach>
        </select>
        <input type="submit" value="Tag Collection!" class="btn btn-success btn-sm" />
      </form>

    </div>
  </div>

</div>
<!-- [END view] -->
