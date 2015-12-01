<#macro dataOptions node>
		attributes:{
			kind:'${node.kind}', 
			parent:'${node.parentPath}',
			name:'${node.name}',
			selected:false
		}
</#macro>

<#-- 树递归macro -->
<#macro tree node>
	<#if node??>
		<#if node.kind==0>
			<#if node.parentPath??>
			<ul>
			</#if>
			
			<#assign subNodes=node.nodes />
			<#if subNodes?? && subNodes?size gt 0>
	    	<#list node.nodes as branch>
	    	<li data-options="<@dataOptions node=branch />">
				<span>${branch.name}</span>
				<@tree node=branch />
			</li>
			</#list>
			</#if>
			
			<#if node.parentPath??>
			</ul>
			</#if>
		</#if>
	</#if>
</#macro>