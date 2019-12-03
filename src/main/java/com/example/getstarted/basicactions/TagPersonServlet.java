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

package com.example.getstarted.basicactions;

import com.example.getstarted.daos.PostDao;
import com.example.getstarted.daos.PersonPostDao;
import com.example.getstarted.objects.Post;
import com.example.getstarted.daos.PersonPostDatastoreDao;
import com.example.getstarted.objects.PersonPostAssociation;
import com.example.getstarted.util.CloudStorageHelper;
import com.google.common.base.Strings;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.lang.String;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

// [START example]
@SuppressWarnings("serial")
public class TagPersonServlet extends HttpServlet {

  // [START formpost]
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    assert ServletFileUpload.isMultipartContent(req);
    CloudStorageHelper storageHelper =
            (CloudStorageHelper) getServletContext().getAttribute("storageHelper");

    Long postId = Long.decode(req.getParameter("postId"));
    System.out.println("POST ID : "+postId);
    HttpSession session = req.getSession();
    Long personId = Long.decode(req.getParameter("personId"));
    System.out.println("PERSON ID : "+personId);


    // [START createdBy]
    String createdByString = "";
    String createdByIdString = "";
    if (session.getAttribute("userEmail") != null) { // Does the user have a logged in session?
      createdByString = (String) session.getAttribute("userEmail");
      createdByIdString = (String) session.getAttribute("userId");
    }
    // [END createdBy]

    // [START postBuilder]
    PersonPostAssociation personpost = new PersonPostAssociation.Builder()
        .personId(String.valueOf(personId))
        .postId(String.valueOf(postId))
        .createdBy(createdByString)
        .createdById(createdByIdString)
        // [END auth]
        .build();
    // [END postBuilder]

    PersonPostDao personpostdao = (PersonPostDao) this.getServletContext().getAttribute("personPostDao");

    try {
      Long id = personpostdao.createPersonPostAssociation(personpost);
      resp.sendRedirect("/readPost?id=" + postId.toString());   // back to the post page
    } catch (Exception e) {
      throw new ServletException("Error creating post", e);
    }
  }
  // [END formpost]
}
// [END example]