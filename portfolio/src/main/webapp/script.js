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
  fetch('/data') .then(response => response.json()).then((comments) => { 
    const commentListElem = document.getElementById('message-container');
    commentListElem.style.display = "none";
    comments.forEach((comment) => {
        commentListElem.appendChild(createCommentElem(comment));
    })
  });
}

function createCommentElem(comment) {
  const commentElem = document.createElement('li');
  commentElem.className = 'comment';

  const bodyElem =document.createElement('span');
  bodyElem.innerText = comment.body;

  commentElem.appendChild(bodyElem);
  return commentElem;
}

function toggleDisplay() {
    var elem = document.getElementById("message-container");
    if (elem.style.display === "none") {
        elem.style.display = "block";
    }
    else {
        elem.style.display = "none";
    }
}