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
 			
 			querySvnBranch();
        });
        
        function querySvnBranch(){
        	$('#sys_list').combobox({
        		onSelect:function(record){
        			var val = record.value;
        			if(!val){
        				$("#branch").text("");
        				$("#edit").hide();
        				return;
        			} 
	        		$.ajax({
	        			url: '${sysDomain}/sys/svn',
	        			data: {sysCode:val},
	        			dataType:"json",
	        			success:function(data){
	        				if(data.isSuccess){
	        					$("#branch").text(data.obj);
	        					$("#edit").show();
	        				}
	        			}
	        		});
		        }
        	});
        }
        
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
        				window.location.href="${sysDomain}/svn/tree?sysCode=";
        			}
        		}
        	});
        }
    </script>
</head> 
<body>
<center>
    <div style="margin:200px 0;"></div>
    <div class="easyui-panel" title="请选择系统" style="width:600px;padding:10px 60px 20px 20px">
    	<form id="form" method="post">
	        <table cellpadding="5">
	            <tr>
	                <td>选择:</td>
	                <td>
						<select id="sys_list" class="easyui-combobox" name="sysCode" style="width:200px;">
						<option value="">请选择</option>
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
	                <td>svn地址:</td>
	                <td id="branch"></td>
	            </tr>
	            <tr id="edit" style="display:none;">
	                <td></td>
	                <td>分支不正确？<a href="${sysDomain}/svn/add/show">修改</a></td>
	            </tr>
	            <tr>
	            	<td><input type="hidden" value="${targetUrl}" id="targetUrl"></td>
	            	<td><a href="javascript:void(0);" id="submit" class="easyui-linkbutton"
	            		data-options="disabled:false"
	            		iconCls="icon-ok" style="width:200px;height:32px">选择</a></td>
	        	</tr>
	        </table>
      	</form>
    </div>
<center>
</body>
</html> 

