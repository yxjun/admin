package per.zc.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * easyui 树形结构
 * @author Administrator
 *
 */
public class TreeNode implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5491443103901272979L;
	
	@JSONField(ordinal=1)
	private Integer id;  
	
	@JSONField(ordinal=2)
	private String text;
	
	@JSONField(ordinal=4)
	private String state = "open"; 
	
	@JSONField(ordinal=5)
	private String iconCls;
	
	@JSONField(ordinal=3)
	private  Map<String, Object>  attributes;
	
	@JSONField(serialize=false)  
	private Integer pid; 
	
	@JSONField(ordinal=7)
	private List<TreeNode> children;
	
	
	
	
	@JSONField(ordinal=8)
	private boolean checked = false;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	
  

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	
}
