<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>freemarker-test-suit</title>
	<#include "common/common.ftl" />
</head> 
<body>
	<center>
    <div style="margin:200px 0;">
    </div>
    <div class="easyui-panel" title="欢迎使用本系统，使用步骤如下：" style="width:700px;padding:10px 60px 20px 60px">
        <table cellpadding="5">
            <tr>
                <td>1:</td>
                <td>
                	使用工号登录本系统。
				</td>
            </tr>
            <tr>
                <td>2:</td>
                <td>
                	选择系统。如果没有您需要的系统，请先添加系统，维护系统相关信息。如果您的系统分支发生变化，请修改系统信息。
				</td>
            </tr>
            <tr>
                <td>3:</td>
                <td>
                	选择freemarker文件，点击预览，即可。
				</td>
            </tr>
            <tr>
            	<td></td>
            	<td><a href="javascript:void(0);"class="easyui-linkbutton"
            		iconCls="icon-ok" style="width:120px;height:32px" 
            		onClick="javascript:window.location.href='${sysDomain}/login?targetUrl=${sysDomain}/sys/list'">现在登录?</a></td>
        	</tr>
        </table>
    </div>
	</center>
</body> 
</html> 

