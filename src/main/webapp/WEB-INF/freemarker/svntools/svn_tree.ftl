<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>keep it simple!</title>
	<#include "common/common.ftl" />
    <script type="text/javascript" src="${scriptHost}/statics/svn-tree.js"></script>
    <script>
    	var globalVars={
    		idx:1,
    		stepNum:4
    	}
    </script>
</head> 
<body class="easyui-layout">
	<#include "svntools/svn-tree-marco.ftl" />
    <div data-options="region:'north'" style="height:80px">
    	<h2>${svnNode.name}</h2>
    </div>
    <div id="west" data-options="region:'west'" title="svn tree" style="width:25%;padding:10px;">
        <ul id='tree' class="easyui-tree" data-options="lines:true">
        	<@tree node=svnNode />
        </ul>
    </div>
    <div data-options="region:'center'" title="步骤" style="width:45%;" id="center">
    	<div class="easyui-accordion" data-options="multiple:false" id="step">
    		<div title="step1" data-options="" style="overflow:auto;padding:10px;">
           		<h3 style="color:#0099FF;">1. 选择环境变量</h3>
            	<p>选择环境变量，如vars.dev.properties.</p>
        	</div>
        	<div title="step2" data-options="" style="overflow:auto;padding:10px;">
           		<h3 style="color:#0099FF;">2. 选择属性文件</h3>
            	<p>如：main-web-setting.properties</p>
        	</div>
        	<div title="step3" data-options="" style="overflow:auto;padding:10px;">
           		<h3 style="color:#0099FF;">3. 选择freemarker model</h3>
            	<p></p>
        	</div>
            <div title="step4" data-options="" style="overflow:auto;padding:10px;">
           		<h3 style="color:#0099FF;">4. 选择freemarker 配置</h3>
            	<p>如：spring-servlet.xml</p>
        	</div>
    	</div>
    	<p>
    		<div id="button-bar">
		        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-back'" 
		        	style="margin-right:60px;display:none;">上一步</a>
		        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">确认</a>
		    </div>
    	</p>
    </div>
    <div data-options="region:'east'" title="已选择" style="width:30%;" id="east">
   		<div class="easyui-accordion" data-options="multiple:true" id="step-accordion">
    	</div>
    </div>
    
</body> 
</html> 

