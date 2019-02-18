$(".room_list_drop").on('click', function() {
	//alert("room_list_drop clicked");
	//alert( $("#room_list_top").html() );
	$.ajax({
        type : "POST",                  // HTTP通信の種類
        url  : "/getroomlist",                 // リクエストを送信する先のURL
        dataType : "json",              // サーバーから返されるデータの型
        processData : false,
        contentType: false
    }).done(function(data) {        // Ajax通信が成功した時の処理

		var tagStr = "";
    	for( var i =0; i < data.length; i++ ){
    		//ルーム名一覧
    		tagStr += "<li class=''>\n";
    		tagStr += "   <a href='/room/disp?id="+data[i].roomId+"'>"+data[i].name+"</a>\n";
    		//カテゴリの一覧
    		//tagStr += "       <ul class='nav nav-third-level'>\n";
    		//tagStr += "           <li><a href='flot.html'>カテゴリ１</a></li>\n";
    		//tagStr += "           <li><a href='flot.html'>カテゴリ2</a></li>\n";
    		//tagStr += "           <li><a href='flot.html'>カテゴリ3</a></li>\n";
    		//tagStr += "       </ul>\n";
    		tagStr += "</li>\n";
    	}
    	$("#room_list_top").html(tagStr);
		//alert( tagStr );
    	
    }).fail(function(XMLHttpRequest, textStatus, errorThrown) { // Ajax通信が失敗した時の処理
        alert("ルーム情報の取得に失敗しました");
    });
})