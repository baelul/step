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

function getMessages() {
  fetch('/data') 
  .then(response => response.json())
  .then((message) => { 
    console.log(message.message1);
    console.log(message.message2);
    console.log(message.message3);
    var randMessage = randomMessage([message.message1, message.message2, message.message3]);
  document.getElementById('message-container').innerHTML = randMessage;
  });
}

function randomMessage(messagesList) {
    var randInd = Math.floor(Math.random() * 3);
    return messagesList[randInd];
}