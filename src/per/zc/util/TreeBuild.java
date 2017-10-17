package per.zc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jfinal.kit.StrKit;

import per.zc.system.model.SysMenu;
import per.zc.system.model.SysOrg;

public class TreeBuild {

	/**
	 * menu 转 treenode
	 * 
	 * @param sysMenus
	 * @return
	 */
	public static List<TreeNode> menuToTreeNode(List<SysMenu> sysMenus) {
		List<TreeNode> treeNodes = new ArrayList<>();
		for (SysMenu sysMenu : sysMenus) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(sysMenu.getId());
			treeNode.setText(sysMenu.getName());
			if (StrKit.notBlank(sysMenu.getUrl())) {
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
		List<Integer> pidset = new ArrayList<>();
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
				if(!pidset.contains(pid)){
					pidset.add(pid);
				}
				 
			} 
		}

		// 拿出 有权限的树根
		List<TreeNode> treeList = new ArrayList<>();
		
		for (Integer recordId : pidset) {
			TreeNode rootRecord = map.get(recordId);
			treeList.add(rootRecord);
		}

		return treeList;
	}

	public static List<SysMenu> easyuiMenuTreegridBuild(List<SysMenu> allList, List<SysMenu> sonList) {

		Map<Integer, SysMenu> map = new HashMap<>();

		// 总数据
		for (SysMenu sysOrg : allList) {
			map.put(sysOrg.getId(), sysOrg);
		}

		// 总数据 添加祖孙关系，并拿出 树根
		List<Integer> root = new ArrayList<>();
		for (SysMenu sysOrg : sonList) {
			Integer pid =  sysOrg.getPid();
			if (pid != null) {
				// 父 treenode 子集合
				List<SysMenu> children =   map.get(pid).get("children");
				if (children == null) {
					children = new ArrayList<SysMenu>();
					map.get(pid).put("children", children);
				}

				children.add(sysOrg);
			} else {
				root.add(sysOrg.getId()); // 根（拥有的权限）
			}
		}

		// 拿出 有权限的树根
		List<SysMenu> treeList = new ArrayList<>();
		for (Integer recordId : root) {
			SysMenu rootRecord = map.get(recordId);
			treeList.add(rootRecord);
		}

		return treeList;
	}

	/**
	 * 组织机构 treegrid 
	 * @param allList
	 * @param sonList
	 * @return
	 */
	public static List<SysOrg> easyuiOrgTreegridBuild(List<SysOrg> allList, List<SysOrg> sonList) {

		Map<Integer, SysOrg> map = new HashMap<>();

		// 总数据
		for (SysOrg sysOrg : allList) {
			map.put(sysOrg.getId(), sysOrg);
		}

		// 总数据 添加祖孙关系，并拿出 树根
		List<Integer> root = new ArrayList<>();
		for (SysOrg sysOrg : sonList) {
			Integer pid =  sysOrg.getPid();
			if (pid != null) {
				// 父 treenode 子集合
				List<SysOrg> children =   map.get(pid).get("children");
				if (children == null) {
					children = new ArrayList<SysOrg>();
					map.get(pid).put("children", children);
				}

				children.add(sysOrg);
			} else {
				root.add(sysOrg.getId()); // 根（拥有的权限）
			}
		}

		// 拿出 有权限的树根
		List<SysOrg> treeList = new ArrayList<>();
		for (Integer recordId : root) {
			SysOrg rootRecord = map.get(recordId);
			treeList.add(rootRecord);
		}

		return treeList;
	}

}
