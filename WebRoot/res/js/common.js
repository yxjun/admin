function show_T_R_msg(title, msg) {
	var msgObj = {

		style : {
			left : '',
			right : 0,
			top : document.body.scrollTop + document.documentElement.scrollTop,
			bottom : ''
		}
	};
	msgObj.title = title;
	msgObj.msg = msg;
	$.messager.show(msgObj);
}

function successMsgPretty(pos, msg) {

	$.toast({
		heading : 'Success',
		text : msg,
		position : pos == null ? 'top-right' : pos,
		stack : false,
		showHideTransition : 'fade',
		icon : 'success'
	})

}

function errMsgPretty(pos, msg) {
	$.toast({
		heading : 'Error',
		text : msg,
		position : pos == null ? 'top-right' : pos,
		stack : false,
		showHideTransition : 'fade',
		icon : 'error'
	})

}

function unSelect() {
	var selectRow = $("#dg").datagrid("getSelected");
	if (selectRow != null) {
		$("#dg").datagrid("unselectRow",
				$("#dg").datagrid("getRowIndex", selectRow));
	}
}

function unselectAll() {
	$("#dg").datagrid("unselectAll");
}

/**
 * 数组去重
 * 
 * @param a
 * @returns {*}
 */
function removeDuplicatedItem(ar) {
	var ret = [];

	for (var i = 0, j = ar.length; i < j; i++) {
		if (ret.indexOf(ar[i]) === -1 && ar[i] != null) {
			ret.push(ar[i]);
		}
	}

	return ret;
}

 

/**
 * ztree 构造菜单树
 * 
 * @param treeObj
 *            树dom
 * @param treeData
 *            树数据
 * @param sck
 *            是否显示checkbox
 * @param cbk
 *            树回调函数
 */
function menuTree(treeObj, treeData, sck, cbk) {
	var setting = {
		check : {
			enable : sck
		},
		data : {
			simpleData : {
				enable : true,
				idKey : 'id',
				pIdKey : 'pid',
				rootPId : null
			}
		},
		callback : cbk
	};

	var tree = $.fn.zTree.init((treeObj), setting, treeData);
	return tree;
}

/**
 * easyui tree 树形数据处理
 */
var EasyTree = function() {
	/*
	 * 将一般的JSON格式转为EasyUI TreeGrid树控件的JSON格式 @param rows:json数据对象 @param
	 * idFieldName:表id的字段名 @param pidFieldName:表父级id的字段名 @param
	 * fileds:要显示的字段,多个字段用逗号分隔
	 */
	var ConvertToTreeGridJson = function(rows, idFieldName, pidFieldName,
			fileds) {
		function exists(rows, ParentId) {
			for (var i = 0; i < rows.length; i++) {
				if (rows[i][idFieldName] == ParentId)
					return true;
			}
			return false;
		}
		var nodes = [];
		// get the top level nodes
		for (var i = 0; i < rows.length; i++) {
			var row = rows[i];
			if (!exists(rows, row[pidFieldName])) {
				var data = {
					id : row[idFieldName]
				}
				var arrFiled = fileds.split(",");
				for (var j = 0; j < arrFiled.length; j++) {
					if (arrFiled[j] != idFieldName)
						data[arrFiled[j]] = row[arrFiled[j]];
				}
				nodes.push(data);
			}
		}
		// console.info("根目录nodes："+JSON.stringify(nodes));

		var toDo = [];
		for (var i = 0; i < nodes.length; i++) {
			toDo.push(nodes[i]);
		}

		while (toDo.length) {
			var node = toDo.shift(); // the parent node
			// get the children nodes
			for (var i = 0; i < rows.length; i++) {
				var row = rows[i];
				if (row[pidFieldName] == node.id) {
					var child = {
						id : row[idFieldName]
					};
					var arrFiled = fileds.split(",");
					for (var j = 0; j < arrFiled.length; j++) {
						if (arrFiled[j] != idFieldName) {
							child[arrFiled[j]] = row[arrFiled[j]];
						}
					}
					if (node.children) {
						node.children.push(child);
					} else {
						node.children = [ child ];
					}
					toDo.push(child);
				}
			}
		}
		return nodes;
	};
	return {
		build : ConvertToTreeGridJson
	};
}