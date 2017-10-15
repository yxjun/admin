function show_T_R_msg(title,msg){
	var msgObj= {
            
            style:{
                left:'',
                right:0,
                top:document.body.scrollTop+document.documentElement.scrollTop,
                bottom:''
            }
        };
	msgObj.title = title;
	msgObj.msg = msg;
	$.messager.show(msgObj);
}


function successMsgPretty(pos,msg){
	
	$.toast({
		  heading: 'Success',
		    text: msg,
		    position: pos == null ?'top-right':pos,
		    stack: false,
		    showHideTransition: 'fade',
		    icon: 'success'
	})
	
}

function errMsgPretty(pos,msg){
	$.toast({
	    heading: 'Error',
	    text: msg,
	    position: pos == null ?'top-right':pos,
	    stack: false,
	    showHideTransition: 'fade',
	    icon: 'error'
	})
	
}

/*
function cancelSelect(){
	if($("#dg").hasClass('easyui-datagrid')){
		$("#dg").datagrid("unselectAll");
	}
	if($("#dg").hasClass('easyui-treegrid')){
		$("#dg").treegrid("unselectAll");
	}
	 
}*/


/**
 * datagrid 、treegrid 双击取消选中状态
 * @param index
 * @param row
 * @returns
 */
function datagridCancelSelect(index,row){
	$("#dg").datagrid("unselectRow",index);
}

function treegridCancelSelect(row){
	$("#dg").treegrid("unselect",row.id); // 此为role
}
 