$(function(){
	treeSelect();
	stepNext();
	stepBack();
	addPanel("", globalVars.idx);
});

function treeSelect(){
	$("#tree").tree({
		onSelect: function(node){
			if(node.attributes.kind!=1){
				return;
			}
			
			if(node.attributes.selected){
				alert("selected");
				return;
			}
			node.attributes.selected=true;
			var html = "<p parent='"+node.attributes.parent+"'>"+node.attributes.name+"</p>";
			addPanel(html);
		}
	});
}

function stepNext(){
	$("#button-bar").find("a").eq(1).on('click',function(){
		globalVars.idx++;
		if(globalVars.idx==globalVars.stepNum){
			$(this).find(".l-btn-text").text('完成');
		}
		
		if(globalVars.idx>globalVars.stepNum){
			globalVars.idx--;
			return;
		}else if(globalVars.idx>1 && globalVars.idx<globalVars.stepNum){
			$("#button-bar").find("a").eq(0).show();
		}
		$("#step").accordion('select',globalVars.idx-1);
		
		// 添加panel
		addPanel("", globalVars.idx);
	});
}

function stepBack(){
	$("#button-bar").find("a").eq(0).on('click',function(){
		globalVars.idx--;
		if(globalVars.idx<globalVars.stepNum){
			$("#button-bar").find("a").eq(1).find(".l-btn-text").text('确认');
		}
		if(globalVars.idx<2){
			$(this).hide();
		}
		$("#step").accordion('select',globalVars.idx-1);
		$('#step-accordion').accordion('remove',globalVars.idx);
	});
}

function addPanel(content, index){
	if(!index){
		index=globalVars.idx;
	}
	var contentId="step"+index+"content";
	var title='STEP'+index;
	var panel=$('#step-accordion').accordion('getPanel',title);
	// 如果已创建则添加内容
	if(panel){
		$("#"+contentId).append(content);
		return;
	}
	
    $('#step-accordion').accordion('add',{
        title:title,
        iconCls: 'icon-ok',
        selected:true,
        height:'120px',
        collapsible: true,
        animate:false,
        content:'<div style="padding:10px" id="'+contentId+'">'+content+'</div>'
    });
}