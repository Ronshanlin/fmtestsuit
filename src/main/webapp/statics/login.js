$.ajax({
	url:'${sysDomain}/login/confirm',
	data:{userNo:$("#userno").textbox('getValue'),pwd:$("#pwd").textbox('getValue')},
	type:'get',
	dataType:'json',
	success: function(data){
	if(data.msg){
		$("#dlg").dialog({content:data.msg});
		$("#dlg").dialog('open');
		return;
	}
	
	var url=$('#targetUrl').val();
	if(url){
		window.location.href=url;
	} else{
		window.location.href="${sysDomain}/index";
		}
	}					
});