<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>login</title>
	<#include "common/common.ftl" />
    <script type="text/javascript">
        $(function(){
			$("#submit").linkbutton({
 				onClick: function(){
 					sumbmit();
 				}
 			});
        });
        function sumbmit(){
        	$("#form").form('submit',{
        		url:'${sysDomain}/svn/add/do',
        		onSubmit: function(){
        			var isValid = $(this).form('validate');
					return isValid;
        		},
        		success:function(data){
        			var result=eval("("+data+")");
        			if(result=="success"){
        				window.location.href="${sysDomain}/svn/tree?sysCode=${sysCode}";
        			}
        		}
        	});
        }
    </script>
</head> 
<body>
<center>
    <div style="margin:200px 0;"></div>
    <div class="easyui-panel" title="添加系统分支" style="width:400px;padding:10px 60px 20px 60px">
    	<form id="form" method="post">
	        <table cellpadding="5">
	            <tr>
	                <td>系统名称:</td>
	                <td><input type="hidden" name="sysCode" value="${sysCode}">${sysCode}</td>
	            </tr>
	            <tr>
	                <td>svn分支</td>
	                <td>
	                	<input class="easyui-validatebox easyui-textbox" name="branch"
	                		data-options="required:true,missingMessage:'请填分支地址'">
	                </td>
	            </tr>
	            <tr>
	            	<td></td>
	            	<td><a href="javascript:void(0);" id="submit" class="easyui-linkbutton"
	            		data-options="disabled:false"
	            		iconCls="icon-ok" style="width:100%;height:32px">添加</a></td>
	        	</tr>
	        </table>
      	</form>
    </div>
<center>
</body>
</html> 

