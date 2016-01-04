<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>login</title>
	<#include "common/common.ftl" />
</head> 
<body>
<center>
    <div style="margin:200px 0;"></div>
    <div class="easyui-panel" title="Register" style="width:400px;padding:10px 60px 20px 60px">
    	<form id="form" method="post">
	        <table cellpadding="5">
	            <tr>
	                <td>用户名:</td>
	                <td><input class="easyui-validatebox easyui-textbox" id="userno" name="userNo"
	                	data-options="required:true,validType:'length[3,10]',missingMessage:'请填写用户名'"></td>
	            </tr>
	            <tr>
	                <td>密码:</td>
	                <td><input class="easyui-validatebox easyui-textbox" id="pwd" name="pwd"
	                	data-options="required:true,missingMessage:'请填写密码'"></td>
	            </tr>
	            <tr>
	            	<td><input type="hidden" value="${targetUrl}" id="targetUrl"></td>
	            	<td><a href="javascript:void(0);" id="submit" class="easyui-linkbutton"
	            		data-options="disabled:false"
	            		iconCls="icon-ok" style="width:100%;height:32px">登录</a></td>
	        	</tr>
	        </table>
      	</form>
       	<div id="dlg" class="easyui-dialog" title="提示"
       		data-options="iconCls:'icon-save',closed:true, buttons:[{
                text: 'Ok',
                iconCls: 'icon-ok',
      			handler: function () {
                	$('#dlg').dialog('close');
                }
            }]" style="width:400px;height:160px;padding:10px">
			The dialog content.
	    </div>
    </div>
<center>
</body>
</html> 

