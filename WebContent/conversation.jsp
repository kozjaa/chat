<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>Chat</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
  <link rel="stylesheet" type="text/css" href="chat.css">
</head>
<body>  
  <div class="container-fluid">
  <div class="row content">
    <div class="col-sm-3 sidenav">
      <h4>Chat</h4>
      <ul class="nav nav-pills nav-stacked">
        <li class="active"><a href="logout">Wyloguj</a></li>
        <li><a href="room">Powrot do pokoju glownego</a></li>
      </ul><br>
    </div>

    <div class="col-sm-9">
     
	<h3>Rozmowa z: ${receiver}</h3>
      <h4>Jestes zalogowany jako: ${name}</h4>
      
        <div class="form-group">
          <textarea class="form-control" rows="3" required id="msg" placeholder="Wpisz wiadomosc"></textarea>
        </div>
        <button class="btn btn-success" onclick="send();">Wyslij</button>
     
      <br><br>
      
      
      <div class="row" id="log">
       
       
     
      </div>
    </div>
  </div>
</div>
</body>	
  <script src="conversationWs.js"></script>
</html>