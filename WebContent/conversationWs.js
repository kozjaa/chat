
var ws;
function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

var receiver = readCookie("receiver");
var current = readCookie("current");
    
ws = new WebSocket("ws://localhost:8080/chat/conversation/" + receiver);


ws.onmessage = function(event) {
  var log = document.getElementById("log");
  console.log(event.data);
  var message = JSON.parse(event.data);
  if(message.allMessages != null){
    log.innerHTML = "";
    for(var i = 0 ; i < message.allMessages.length ; i++){       
      div = document.createElement('div');
  	  div.className = "col-sm-10";
  	  h2 = document.createElement('h2');
  	  small = document.createElement('small');
  	  p = document.createElement('p');
  	  h2.innerHTML = message.allMessages[i].from;
  	  p.innerHTML = message.allMessages[i].content + ' ';
  	  small.innerHTML = ' ' + message.allMessages[i].sentAt;
  	  h2.appendChild(small);
  	  div.appendChild(h2);
  	  div.appendChild(p);
  	  log.appendChild(div);
    }
  }
};

function send() {
    var content = document.getElementById("msg").value;
    var json = JSON.stringify({
        "content":content
    });
    ws.send(json);
}

function logout() {
    var json = JSON.stringify({
		   "content": "Wylogował się"
	   });
}
