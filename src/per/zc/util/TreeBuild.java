package per.zc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.StrKit;

import per.zc.system.model.SysMenu; 

public class TreeBuild {
	
	
	
	/**
	 * menu 转  treenode
	 * @param sysMenus
	 * @return
	 */
	public static List<TreeNode> menuToTreeNode(List<SysMenu> sysMenus){
		List<TreeNode> treeNodes  = new ArrayList<>();
		for(SysMenu sysMenu : sysMenus){
			TreeNode treeNode = new TreeNode();
			treeNode.setId(sysMenu.getId());
			treeNode.setText(sysMenu.getName());
			if(StrKit.notBlank(sysMenu.getUrl())){
				Map<String, Object> attributes = new HashMap<>();
				attributes.put("url", sysMenu.getUrl());
				treeNode.setAttributes(attributes);
			}
		 
			treeNode.setPid(sysMenu.getPid());
			treeNode.setIconCls(sysMenu.getIcon());
			treeNodes.add(treeNode);
		}
		
		return treeNodes;
	}
	
	public static List<Map<String, Object>> menuToMap(List<SysMenu> sysMenus){
		List<Map<String, Object>> mapList= new ArrayList<>();
		for(SysMenu sysMenu : sysMenus){
			Map<String, Object> map = new HashMap<>();
			map.put("id", sysMenu.getId());
			map.put("name", sysMenu.getName());
			map.put("iconCls", sysMenu.getIcon());
			map.put("sort", sysMenu.getSort());
			map.put("url", sysMenu.getUrl());
			map.put("pid", sysMenu.getPid() );
			mapList.add(map);
		}
		
		return mapList;
	}

	/**
	 * 构造 符合 easyui 树结构工具, 用于freemarker 中
	 * 
	 * @param allList
	 * @param sonList
	 * @return
	 */
	public static List<TreeNode> easyuiTreeBuild(List<SysMenu> allList, List<SysMenu> sonList) {
		
		List<TreeNode> allTreeNodeList = menuToTreeNode(allList);
		List<TreeNode> sonTreeNodeList = menuToTreeNode(sonList);
		
		Map<Integer, TreeNode> map = new HashMap<>();

		// 总数据
		for (TreeNode treeNode : allTreeNodeList) {
			map.put(treeNode.getId(), treeNode);
		}

		// 总数据 添加祖孙关系，并拿出 树根
		List<Integer> root = new ArrayList<>();
		for (TreeNode treeNode : sonTreeNodeList) {
			Integer pid = treeNode.getPid();
			if (pid != null) {
				// 父 treenode 子集合
				List<TreeNode> children = map.get(pid).getChildren();
				if (children == null) {
					children = new ArrayList<TreeNode>();
					 map.get(pid).setChildren(children);
				}
			    children.add(treeNode);
			}else{
				root.add(treeNode.getId());  // 根（拥有的权限根）
			}
		}
		
		
		
		// 拿出 有权限的树根
		List<TreeNode> treeList = new ArrayList<>();
		for(Integer recordId : root ){
		     TreeNode rootRecord = map.get(recordId);
		     treeList.add(rootRecord);
		}
		
		 
		return treeList;
	}

	
	
	public static List<Map<String, Object>> easyuiTreeBuild2(List<SysMenu> allList, List<SysMenu> sonList) {
		
		List<Map<String, Object>> allTreeNodeList = menuToMap(allList);
		List<Map<String, Object>> sonTreeNodeList = menuToMap(sonList);
		
		Map<Integer, Map<String, Object>> map = new HashMap<>();

		// 总数据
		for (Map<String, Object> treeNode : allTreeNodeList) {
			map.put((Integer)treeNode.get("id"), treeNode);
		}

		// 总数据 添加祖孙关系，并拿出 树根
		List<Integer> root = new ArrayList<>();
		for (Map treeNode : sonTreeNodeList) {
			Integer pid = (Integer) treeNode.get("pid");
			if (pid != null) {
				// 父 treenode 子集合
				List<Map<String, Object>> children =   (List<Map<String, Object>>) map.get(pid).get("children");
				if (children == null) {
					children = new ArrayList<Map<String, Object>>();
					map.get(pid).put("children", children);
				}
				 
			    children.add(treeNode);
			}else{
				root.add((Integer)treeNode.get("id"));  // 根（拥有的权限）
			}
		}
		
		
		
		// 拿出 有权限的树根
		List<Map<String, Object>> treeList = new ArrayList<>();
		for(Integer recordId : root ){
			 Map<String, Object> rootRecord = map.get(recordId);
		     treeList.add(rootRecord);
		}
		
		 
		return treeList;
	}
}
