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
import com.example.getstarted.daos.PersonCollectionDao;
import com.example.getstarted.daos.CollectionPostDao;
import com.example.getstarted.daos.CollectionDao;
import com.example.getstarted.objects.Person;
import com.example.getstarted.objects.PersonCollectionAssociation;
import com.example.getstarted.objects.CollectionPostAssociation;
import com.example.getstarted.objects.Collection;
import com.example.getstarted.objects.Post;
import com.example.getstarted.objects.Result;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// [START example]
@SuppressWarnings("serial")
public class ReadCollectionServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
      ServletException {
    Long id = Long.decode(req.getParameter("id"));
    String startCursor = req.getParameter("cursor");

    CollectionDao collectionDao = (CollectionDao) this.getServletContext().getAttribute("collectionDao");
    PersonCollectionDao personcollectiondao = (PersonCollectionDao) this.getServletContext().getAttribute("personcollectiondao");
    CollectionPostDao collectionpostdao = (CollectionPostDao) this.getServletContext().getAttribute("collectionPostDao");
    List<Person> persons = null;

    try {
      Collection collection = collectionDao.readCollection(id);

      persons = personcollectiondao.listPersons(id);
      System.out.println(persons);

      Result<Post> result = collectionpostdao.listPosts(id, startCursor);
      List<Post> posts = result.result;
      //System.out.println(posts);
      String endCursor = result.cursor;

      System.out.println("Read Collection");

      req.setAttribute("persons", persons);
      req.setAttribute("collection", collection);
      req.setAttribute("posts", posts);
      req.setAttribute("cursor", endCursor);
      req.setAttribute("page", "collectionView");
      req.getRequestDispatcher("/base.jsp").forward(req, resp);
    } catch (Exception e) {
      throw new ServletException("Error reading collection", e);
    }
  }
}
// [END example]
