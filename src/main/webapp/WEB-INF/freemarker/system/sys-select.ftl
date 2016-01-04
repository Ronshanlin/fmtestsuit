<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>选择系统</title>
	<#include "common/common.ftl" />
    <script type="text/javascript">
        $(function(){
			$("#submit").linkbutton({
 				onClick: function(){
 					login.check(function(){
 						sumbmit();
 					});
 				}
 			});
        });
        function sumbmit(){
        	$("#form").form('submit',{
        		url:'${sysDomain}/sys/select',
        		onSubmit: function(){
        			var isValid = $(this).form('validate');
					return isValid;
        		},
        		success:function(data){
        			var result=eval("("+data+")");
        			if(result=="success"){
        				window.location.href="${sysDomain}/svn/tree";
        			}
        		}
        	});
        }
    </script>
</head> 
<body>
<center>
    <div style="margin:200px 0;"></div>
    <div class="easyui-panel" title="请选择系统" style="width:400px;padding:10px 60px 20px 60px">
    	<form id="form" method="post">
	        <table cellpadding="5">
	            <tr>
	                <td>选择:</td>
	                <td>
						<select id="cc" class="easyui-combobox" name="sysCode" style="width:100%;">
						<#if sysList?? && sysList?size gt 0>
						<#list sysList as sys>
							<option value="${sys.sysCode}">${sys.sysCode}(${sys.sysName})</option>
						</#list>
						</#if>
						</select>
					</td>
	            </tr>
	            <tr>
	                <td></td>
	                <td>没有系统？<a href="${sysDomain}/sys/add/show">新增</a></td>
	            </tr>
	            <tr>
	            	<td><input type="hidden" value="${targetUrl}" id="targetUrl"></td>
	            	<td><a href="javascript:void(0);" id="submit" class="easyui-linkbutton"
	            		data-options="disabled:false"
	            		iconCls="icon-ok" style="width:100%;height:32px">选择</a></td>
	        	</tr>
	        </table>
      	</form>
    </div>
<center>
</body>
</html> 

