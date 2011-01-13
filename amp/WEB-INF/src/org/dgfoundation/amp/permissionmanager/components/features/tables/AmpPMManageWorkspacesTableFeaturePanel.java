/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.tables;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.dgfoundation.amp.onepager.OnePagerUtil;
import org.dgfoundation.amp.onepager.components.TransparentWebMarkupContainer;
import org.dgfoundation.amp.onepager.components.features.tables.AmpFormTableFeaturePanel;
import org.digijava.module.aim.dbentity.AmpTeam;

/**
 * @author dan
 *
 */
public class AmpPMManageWorkspacesTableFeaturePanel extends AmpFormTableFeaturePanel {

	private TransparentWebMarkupContainer slider;
	private List<TransparentWebMarkupContainer> sliders;
	
	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @throws Exception
	 */
	public AmpPMManageWorkspacesTableFeaturePanel(String id, IModel model,
			String fmName) throws Exception {
		super(id, model, fmName);
		// TODO Auto-generated constructor stub
//		slider = new TransparentWebMarkupContainer("sliderWorkspaceInfo");
//		slider.setOutputMarkupId(true);
//		add(slider);
	}

	/**
	 * @param id
	 * @param model
	 * @param fmName
	 * @param hideLeadingNewLine
	 * @throws Exception
	 */
	public AmpPMManageWorkspacesTableFeaturePanel(String id, IModel<Set<AmpTeam>> model, String fmName, boolean hideLeadingNewLine) throws Exception {
		super(id, model, fmName, hideLeadingNewLine);
		sliders = new ArrayList<TransparentWebMarkupContainer>();

		
		AbstractReadOnlyModel<List<AmpTeam>> listModel = OnePagerUtil.getReadOnlyListModelFromSetModel(model);
		list = new PageableListView<AmpTeam>("usersList", listModel, 5) {
			private static final long serialVersionUID = 7218457979728871528L;
			@Override
			protected void populateItem(final ListItem<AmpTeam> item) {
				final MarkupContainer listParent=this.getParent();
				item.add(new Label("workspaceName", item.getModelObject().getName()));
				slider = new TransparentWebMarkupContainer("sliderWorkspaceInfo");
				slider.setOutputMarkupId(true);
				item.add(slider);
				sliders.add(slider);
				AmpPMViewUsersTableFeaturePanel usersList = null;
				try {
						usersList = new AmpPMViewUsersTableFeaturePanel("workspaceMembers", item.getModel(), "Workspace Members", false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				item.add(usersList);

			}
		};
		list.setReuseItems(true);
		add(list);
		
	}
	
	public TransparentWebMarkupContainer getSlider() {
		return slider;
	}
	
	public List<TransparentWebMarkupContainer> getSliders() {
		return sliders;
	}
}

