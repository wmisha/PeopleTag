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
import com.example.getstarted.objects.PersonPostAssociation;
import com.example.getstarted.objects.CollectionPostAssociation;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// [START example]
@SuppressWarnings("serial")
public class ReadPostServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
      ServletException {
    Long id = Long.decode(req.getParameter("id"));
    PostDao postDao = (PostDao) this.getServletContext().getAttribute("postDao");

    PersonDao persondao = (PersonDao) this.getServletContext().getAttribute("dao");
    CollectionDao collectiondao = (CollectionDao) this.getServletContext().getAttribute("collectionDao");
    PersonPostDao personpostdao = (PersonPostDao) this.getServletContext().getAttribute("personPostDao");
    CollectionPostDao collectionpostdao = (CollectionPostDao) this.getServletContext().getAttribute("collectionPostDao");

    try {
      Post post = postDao.readPost(id);

      //persons = personpostdao.listPersons(id);
      //System.out.println(persons);

      System.out.println("Read Post");

      List<Person> persons = personpostdao.listPersons(id);
      List<Collection> collections = collectionpostdao.listCollections(id);

      List<Person> allpersons = persondao.listAllPersons();
      List<Collection> allcollections = collectiondao.listAllCollections();

      req.setAttribute("persons", persons);
      req.setAttribute("collections", collections);
      req.setAttribute("allpersons", allpersons);
      req.setAttribute("allcollections", allcollections);
      req.setAttribute("post", post);
      req.setAttribute("page", "postView");
      req.getRequestDispatcher("/base.jsp").forward(req, resp);
    } catch (Exception e) {
      throw new ServletException("Error reading post", e);
    }
  }
}
// [END example]
