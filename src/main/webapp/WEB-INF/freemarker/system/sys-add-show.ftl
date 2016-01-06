<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>login</title>
	<#include "common/common.ftl" />
    <script type="text/javascript">
        $(function(){
			$("#submit").linkbutton({
 				onClick: function(){
 					$("#dlg").dialog('open');
 				}
 			});
        });
        function sumbmit(){
        	$("#form").form('submit',{
        		url:'${sysDomain}/sys/add/do',
        		onSubmit: function(param){
        			console.log(param);
        			var isValid = $(this).form('validate');
					return isValid;
        		},
        		success:function(data){
        			var result=eval("("+data+")");
        			if(result=="success"){
        				var sys = $("#sysCode").textbox("getValue");
        				window.location.href="${sysDomain}/svn/add/show/"+sys;
        			}
        		}
        	});
        }
    </script>
</head> 
<body>
<center>
    <div style="margin:200px 0;"></div>
    <div class="easyui-panel" title="添加新系统" style="width:400px;padding:10px 60px 20px 60px">
    	<form id="form" method="post">
	        <table cellpadding="5">
	            <tr>
	                <td>系统名称:</td>
	                <td>
	                	<input class="easyui-validatebox easyui-textbox" name="sysName"
	                		data-options="required:true,validType:'length[3,10]',missingMessage:'请填系统名称'">
	                </td>
	            </tr>
	            <tr>
	                <td>系统编码:</td>
	                <td>
	                	<input class="easyui-validatebox easyui-textbox" name="sysCode" id="sysCode"
	                		data-options="required:true,validType:'length[1,10]',missingMessage:'请填系统编码'">
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
      	<div id="dlg" class="easyui-dialog" title="提示"
       		data-options="iconCls:'icon-save',closed:true, buttons:[{
                text: 'Ok',
                iconCls: 'icon-ok',
                handler: function () {
                	sumbmit();
                }
            }, {
                text: 'Cancel',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#dlg').dialog('close');
                }
            }]" style="width:400px;height:160px;padding:10px">
			系统一经提交则不能修改，请确认是否添加？
	    </div>
    </div>
<center>
</body>
</html> 

