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
    <a href="/deleteCollection?id=${collection.id}" class="btn btn-danger btn-sm">
      <i class="glyphicon glyphicon-trash"></i>
      Delete collection
    </a>

    <a href="/associate?id=${collection.id}" class="btn btn-success btn-sm">
          <i class="glyphicon glyphicon-plus"></i>
          Add Person to Collection
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

  <div style="display: grid; grid-template-columns: 300px 300px 300px;">
    <c:forEach items="${persons}" var="person">
    <div class="media" style="border: 1px solid #666">
      <a href="/read?id=${person.id}">
        <div class="media-left">
          <img alt="ahhh" height="200"src="${fn:escapeXml(not empty person.imageUrl?person.imageUrl:'http://placekitten.com/g/128/192')}">
        </div>
        <div class="media-body">
          <h4 class="person-first">
            ${fn:escapeXml(person.first)}
          </h4>
          <h5 class="person-last">${fn:escapeXml(not empty person.last?person.last:'Unknown')}</h5>
          <p class="person-description">${fn:escapeXml(person.description)}</p>
          <p class="person-jobTitle">${fn:escapeXml(person.jobTitle)}</p>
          <hr />
          <c:if test="${not empty person.facebook}">
            <a href="${fn:escapeXml(person.facebook)}">
              <img src="https://blog-assets.hootsuite.com/wp-content/uploads/2018/09/f-ogo_RGB_HEX-58.png" width="32">
            </a>
          </c:if>
          <c:if test="${not empty person.instagram}">
            <a href="${fn:escapeXml(person.instagram)}">
              <img src="https://blog.hootsuite.com/wp-content/uploads/2018/09/glyph-logo_May2016-150x150.png" width="32">
            </a>
          </c:if>
          <c:if test="${not empty person.linkedIn}">
            <a href="${fn:escapeXml(person.linkedIn)}">
              <img src="https://blog-assets.hootsuite.com/wp-content/uploads/2018/09/In-2C-54px-R.png" width="32">
            </a>
          </c:if>
          <c:if test="${not empty person.twitter}">
            <a href="${fn:escapeXml(person.twitter)}">
              <img src="https://blog.hootsuite.com/wp-content/uploads/2018/09/Twitter_Logo_Blue-150x150.png" width="32">
            </a>
          </c:if>
          <hr />
          <small class="person-added-by">Added by
            ${fn:escapeXml(not empty person.createdBy?person.createdBy:'Anonymous')}</small>
        </div>
      </a>
    </div>
    </c:forEach>
  </div>
  <h4>Posts tagged with this collection:</h4>
  <c:choose>
  <c:when test="${empty posts}">
  <p>No posts found</p>
  </c:when>
  <c:otherwise>
  <c:forEach items="${posts}" var="post">
  <div class="media">
    <a href="/readPost?id=${post.id}"><b>${fn:escapeXml(post.title)}</b></a></br>
  </div>
  </c:forEach>
  <c:if test="${not empty cursor}">
         <nav>
           <ul class="pager">
             <li><a href="?id=${collection.id}&cursor=${fn:escapeXml(cursor)}">More</a></li>
           </ul>
         </nav>
         </c:if>
  </c:otherwise>
  </c:choose>

</div>
<!-- [END view] -->
