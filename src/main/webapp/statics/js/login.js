$(function(){
	login.submit.bind();
});

var login = login||{};
login.check=function(loginCallback, targetUrl){
	$.ajax({
		url:common.sysDomain+'/login/check',
		type:'get',
		dataType:'json',
		success:function(data){
			var result=eval("("+data+")");
			if(result=="false"){
				if(targetUrl){
					window.location.href=common.sysDomain+"/login?targetUrl="+targetUrl;
				}else{
					window.location.href=common.sysDomain+"/login?targetUrl="+window.location.href;
				}
			}else{
				loginCallback(result);
			}
		}
	});	
}

login.submit={};
login.submit.onSubmit= function (){
	$("#form").form('submit',{
		url:common.sysDomain+'/login/confirm',
		onSubmit: function(){
			var isValid = $(this).form('validate');
			return isValid;
		},
		success:function(data){
			var result=eval("("+data+")");
			if(result.msg){
				$("#dlg").dialog({content:result.msg});
				$("#dlg").dialog('open');
				return;
			}
			
			var url=$('#targetUrl').val();
			if(url){
				window.location.href=url;
			} else{
				window.location.href=common.sysDomain+"/index";
			}
		}
	});
}

login.submit.bind=function(){
	$("#submit").linkbutton({
		onClick: function(){
			login.submit.onSubmit();
		}
	});
}
