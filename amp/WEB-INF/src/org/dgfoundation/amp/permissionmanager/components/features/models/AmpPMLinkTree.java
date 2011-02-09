/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LinkIconPanel;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.permissionmanager.components.features.fields.AmpPMTreeField;

/**
 * @author dan
 *
 */
public class AmpPMLinkTree extends LinkTree {

	/**
	 * @param id
	 */
	public AmpPMLinkTree(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 */
	public AmpPMLinkTree(String id, IModel<TreeModel> model) {
		super(id, model);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param model
	 */
	public AmpPMLinkTree(String id, TreeModel model) {
		super(id, model);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected Component newNodeComponent(String id, IModel<Object> model)
	{
		return new LinkIconPanel(id, model, AmpPMLinkTree.this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onNodeLinkClicked(Object node, BaseTree tree, AjaxRequestTarget target)
			{
				//super.onNodeLinkClicked(node, tree, target);
				//AmpPMLinkTree.this.onNodeLinkClicked(node, tree, target);
				//target.addComponent(AmpPMLinkTree.this);
				System.out.println("aaa");
			}

			@Override
			protected Component newContentComponent(String componentId, BaseTree tree, IModel<Object> model)
			{
				DefaultMutableTreeNode o = (DefaultMutableTreeNode)model.getObject();
				//return new Label(componentId, getNodeTextModel(model));
				IModel<Object> nodeTextModel = getNodeTextModel(model);
				return new AmpPMTreeField(componentId, nodeTextModel,componentId);
			}
		};
	}
	
	
	@Override
	protected void onNodeLinkClicked(Object node, BaseTree tree, AjaxRequestTarget target)
	{
		//tree.getTreeState().selectNode(node, !tree.getTreeState().isNodeSelected(node));
		//tree.updateTree(target);
	}
	

}
