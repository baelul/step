// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.sps.data.Comment;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    ArrayList<Comment> comments = new ArrayList();
    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      String body = (String) entity.getProperty("body");
      long timestamp = (long) entity.getProperty("timestamp");

      Comment comment = new Comment(id, body, timestamp);
      comments.add(comment);
    }

    Gson gson = new Gson();

    response.setContentType("application/json");
    response.getWriter().println(gson.toJson(comments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String body = request.getParameter("comment");
      long timestamp = System.currentTimeMillis();

      Entity commentEntity = new Entity("Comment");
      commentEntity.setProperty("body", body);
      commentEntity.setProperty("timestamp", timestamp);
      datastore.put(commentEntity);

      response.sendRedirect("/comments.html");
  }
}