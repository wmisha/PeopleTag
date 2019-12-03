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

import com.example.getstarted.daos.PersonDao;
import com.example.getstarted.daos.PostDao;
import com.example.getstarted.daos.CollectionDao;
import com.example.getstarted.daos.PersonPostDao;
import com.example.getstarted.daos.CollectionPostDao;
import com.example.getstarted.objects.Person;
import com.example.getstarted.objects.Post;
import com.example.getstarted.objects.Collection;

import com.example.getstarted.daos.PostDatastoreDao;
import com.example.getstarted.daos.PersonPostDatastoreDao;
import com.example.getstarted.daos.CollectionPostDatastoreDao;
import com.example.getstarted.objects.Post;
import com.example.getstarted.objects.Result;
import com.example.getstarted.util.CloudStorageHelper;

import com.google.common.base.Strings;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// [START example]
@SuppressWarnings("serial")
public class ListPostServlet extends HttpServlet {
  @Override
  public void init() throws ServletException {
    CloudStorageHelper storageHelper = new CloudStorageHelper();

    // Creates the DAO based on the Context Parameters
//    String storageType = this.getServletContext().getInitParameter("personshelf.storageType");
    PostDao dao = new PostDatastoreDao();
    PersonPostDao personPostDao = new PersonPostDatastoreDao();
    CollectionPostDao collectionPostDao = new CollectionPostDatastoreDao();
//    PersonCollectionDao personcollectiondao = new PersonCollectionDatastoreDao();
    this.getServletContext().setAttribute("postDao", dao);
    this.getServletContext().setAttribute("personPostDao", personPostDao);
    this.getServletContext().setAttribute("collectionPostDao", collectionPostDao);
//    this.getServletContext().setAttribute("personcollectiondao", personcollectiondao);
//    this.getServletContext().setAttribute("storageHelper", storageHelper);
//    this.getServletContext().setAttribute(
//            "isCloudStorageConfigured",  // Hide upload when Cloud Storage is not configured.
//            !Strings.isNullOrEmpty(getServletContext().getInitParameter("personshelf.bucket")));
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
      ServletException {
    PostDao postDao = (PostDao) this.getServletContext().getAttribute("postDao");
    String startCursor = req.getParameter("cursor");
    List<Post> posts = null;
    String endCursor = null;
    try {
      Result<Post> result = postDao.listPosts(startCursor);
      posts = result.result;
      System.out.println(posts);
      endCursor = result.cursor;
    } catch (Exception e) {
      throw new ServletException("Error listing posts", e);
    }
    req.setAttribute("posts", posts);
    req.setAttribute("cursor", endCursor);
    req.setAttribute("page", "postList");
    req.getRequestDispatcher("/base.jsp").forward(req, resp);
  }
}
// [END example]
