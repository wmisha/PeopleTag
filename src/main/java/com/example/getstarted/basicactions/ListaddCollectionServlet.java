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

import com.example.getstarted.daos.CollectionDao;

import com.example.getstarted.daos.CollectionDatastoreDao;
import com.example.getstarted.objects.Collection;
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
public class ListaddCollectionServlet extends HttpServlet {

  @Override
  public void init() throws ServletException {
    CollectionDao collectionDao = null;

    CloudStorageHelper storageHelper = new CloudStorageHelper();

    // Creates the DAO based on the Context Parameters
    String storageType = this.getServletContext().getInitParameter("personshelf.storageType");
    collectionDao = new CollectionDatastoreDao();
    this.getServletContext().setAttribute("collectionDao", collectionDao);
    this.getServletContext().setAttribute("storageHelper", storageHelper);
    this.getServletContext().setAttribute(
        "isCloudStorageConfigured",  // Hide upload when Cloud Storage is not configured.
        !Strings.isNullOrEmpty(getServletContext().getInitParameter("personshelf.bucket")));
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
      ServletException {
    CollectionDao collectionDao = (CollectionDao) this.getServletContext().getAttribute("collectionDao");
    String startCursor = req.getParameter("cursor");
    List<Collection> collections = null;
    String endCursor = null;
    Long personId = Long.decode(req.getParameter("id"));
    req.getSession().getServletContext().setAttribute("personId", personId);

    try {
      Result<Collection> result = collectionDao.listCollections(startCursor);
      collections = result.result;
      endCursor = result.cursor;
    } catch (Exception e) {
      throw new ServletException("Error listing collections", e);
    }
    req.getSession().getServletContext().setAttribute("collections", collections);
    req.getSession().getServletContext().setAttribute("personId", personId);

    StringBuilder collectionNames = new StringBuilder();
    for (Collection collection : collections) {
      collectionNames.append(collection.getName() + " ");
    }
    req.setAttribute("cursor", endCursor);
    req.setAttribute("page", "addcollection");
    req.getRequestDispatcher("/base.jsp").forward(req, resp);
  }
}
// [END example]
