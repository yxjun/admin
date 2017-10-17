package per.zc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.StrKit;

import per.zc.system.model.SysMenu;
import per.zc.system.model.SysOrg;

public class TreeBuild {

    /**
     * SysMenu 数据封装成  easyui tree 格式的数据
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
     * 构造用户的权限菜单， sonList  祖孙关系不能断
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

        // son 从session 中来， all 实时数据， 如果 all 中已经删除？
        for(int i=0;i<sonTreeNodeList.size();i++){
            System.err.println(sonTreeNodeList.get(i).getId()+"---"+sonTreeNodeList.get(i).getPid());
            if(map.get(sonTreeNodeList.get(i).getId())==null  ){
                  sonTreeNodeList.remove(i);
                System.err.println("remove ");
            }

        }

        // 总数据 添加祖孙关系，并拿出 树根
        List<Integer> pidset = new ArrayList<>();
        System.err.println("all size : " + allTreeNodeList.size());
        System.err.println("son size : " + sonTreeNodeList.size());
        for (TreeNode treeNode : sonTreeNodeList) {
            Integer pid = treeNode.getPid();
            if (pid != null) {
                // 父 treenode 子集合
                if( map.get(pid)!=null){
                    List<TreeNode> children = map.get(pid).getChildren();
                    if (children == null) {
                        children = new ArrayList<TreeNode>();
                        map.get(pid).setChildren(children);
                    }
                    children.add(map.get(treeNode.getId()));
                }

            } else {
                pidset.add(treeNode.getId());
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


    /**
     * 全部菜单treegrid
     * @param allList
     * @return
     */
    public static List<SysMenu> easyuiMenuTreegridBuild(List<SysMenu> allList) {

        Map<Integer, SysMenu> map = new HashMap<>();

        // 总数据
        for (SysMenu sysOrg : allList) {
            map.put(sysOrg.getId(), sysOrg);
        }

        // 总数据 添加祖孙关系，并拿出 树根
        List<Integer> root = new ArrayList<>();
        for (SysMenu sysOrg : allList) {
            Integer pid = sysOrg.getPid();
            if (pid != null) {
                // 父 treenode 子集合
                if(map.get(pid)!=null){
                    List<SysMenu> children = map.get(pid).get("children");
                    if (children == null) {
                        children = new ArrayList<SysMenu>();
                        map.get(pid).put("children", children);
                    }
                    children.add(map.get(sysOrg.getId()));
                }

            } else {
                root.add(sysOrg.getId());
            }
        }


        List<SysMenu> treeList = new ArrayList<>();
        for (Integer recordId : root) {
            SysMenu rootRecord = map.get(recordId);
            treeList.add(rootRecord);
        }

        return treeList;
    }


    /**
     * 全部 机构 treegrid
     * @param allList
     * @return
     */
    public static List<SysOrg> easyuiOrgTreegridBuild(List<SysOrg> allList) {

        Map<Integer, SysOrg> map = new HashMap<>();

        // 总数据
        for (SysOrg sysOrg : allList) {
            map.put(sysOrg.getId(), sysOrg);
        }

        List<Integer> root = new ArrayList<>();
        for (SysOrg sysOrg : allList) {
            Integer pid = sysOrg.getPid();
            if (pid != null) {
                // 父 treenode 子集合
                if(map.get(pid)!=null){
                    List<SysOrg> children = map.get(pid).get("children");
                    if (children == null) {
                        children = new ArrayList<SysOrg>();
                        map.get(pid).put("children", children);
                    }

                    children.add(map.get(sysOrg.getId()));
                }

            } else {
                root.add(sysOrg.getId());
            }
        }

        // 拿出  树根
        List<SysOrg> treeList = new ArrayList<>();
        for (Integer recordId : root) {
            SysOrg rootRecord = map.get(recordId);
            treeList.add(rootRecord);
        }

        return treeList;
    }

}
