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

function getComments() {
  fetch('/data').then(response => response.json()).then((comments) => { 
    const commentListElem = document.getElementById('message-container');
    comments.forEach((comment) => {
        const Url = fetchBlobstoreUrl();
        commentListElem.appendChild(createCommentElem(comment, Url));
    })
  });
}

function createCommentElem(comment, Url) {
  const commentElem = document.createElement('li');
  commentElem.className = 'comment';

  const imgElem = document.createElement('img');
  imgElem.src = Url;
  imgElem.innerText = comment.body;

  const bodyElem = document.createElement('span');
  bodyElem.innerText = comment.body;

  commentElem.appendChild(bodyElem);
  return commentElem;
}

function toggleData() {
  var content = document.getElementById("message-container");
  if (content.style.display === "none") {
    content.style.display = "block";
  } else {
    content.style.display = "none";
  }
}

function fetchBlobstoreUrl() {
  fetch('/blobstore-url').then((response) => {
      return response.text();
  }).then((imageUploadUrl) => {
      const commentForm = document.getElementById('comment-form');
      commentForm.action = imageUploadUrl;
  });
}