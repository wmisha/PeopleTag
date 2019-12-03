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
<!-- [START form] -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div class="container">
  <h3>
    <c:out value="${action}" /> post
  </h3>

  <form method="POST" action="${destination}" enctype="multipart/form-data">

    <div class="form-group">
      <label for="title">Title</label>
      <input type="text" name="title" id="title" value="${fn:escapeXml(post.title)}" class="form-control" />
    </div>
    <div class="form-group">
      <label for="text">Post Text</label>
      <br />Note: All URLs will automatically be converted to hyperlinks.
      <textarea name="text" id="text" class="form-control" rows="20">${fn:escapeXml(post.text)}</textarea>
    </div>

    <button type="submit" class="btn btn-success">Save</button>
  </form>
</div>
<!-- [END form] -->
