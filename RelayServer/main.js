var net = require('net');

var clients = [];
var lightModeManual = false;
var leftPeopleNum = 0;
// 서버를 생성
try{
var server = net.createServer(function(socket){
  console.log(socket.remoteAddress +":" + socket.remotePort + " connected.");
  clients.push(socket);
  
  // client로 부터 오는 data를 화면에 출력
  socket.on('data', function(data){
    try {
        jsondata = JSON.parse(data);
        //var arrSplit = data.toString().split(':');
        console.log(jsondata);
    }catch (exception){
      console.log(exception);
    }
    broadcast(data, socket);
  });
  // client와 접속이 끊기는 메시지 출력
  socket.on('close', function(){
    console.log('client disconnted.');
    clients.splice(clients.indexOf(socket), 1);
  });
  socket.on('error', function(){
    console.log('client error.');
    socket.destroy();
    console.log('socket destroyed.');
  });

   function broadcast(message, sender) {
    clients.forEach(function (client) {
      // Don't want to send it to sender
      if (client === sender) return;
      client.write(message);
    });
    // Log it to the server output too
    console.log("send: " + message)
  }

});


// 에러가 발생할 경우 화면에 에러메시지 출력
server.on('error', function(err){
  console.log('err'+ err  );
});

// Port 5000으로 접속이 가능하도록 대기
server.listen(8107, function(){
  console.log('listening on 8107..');
});
}catch (exception){
  console.log(exception);
}