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


/**
 *  数组去重
 * @param a
 * @returns {*}
 */
function removeDuplicatedItem(ar) {
    var ret = [];

    for (var i = 0, j = ar.length; i < j; i++) {
        if (ret.indexOf(ar[i]) === -1 ) {
            ret.push(ar[i]);
        }
    }

    return ret;
}


/**
 * 构造菜单树
 * @param treeObj  树dom
 * @param treeData  树数据
 * @param sck   是否显示checkbox
 * @param cbk  树回调函数
 */
function permissMenuTree(treeObj,treeData,sck,cbk){
    var setting = {
        check: {
            enable: sck
        },
        data: {
            simpleData: {
                enable: true,
                idKey:'id',
                pIdKey:'pid',
                rootPId:null
            }
        },
		callback:cbk
    };

    $.fn.zTree.init((treeObj), setting, treeData);
}