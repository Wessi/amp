/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 *
 */
package org.dgfoundation.amp.onepager.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * @author mpostelnicu@dgateway.org since Sep 28, 2010
 */
public class AmpSectorSearchModel extends
		AbstractAmpAutoCompleteModel<AmpSector> {

	public enum PARAM implements AmpAutoCompleteModelParam {
		SECTOR_SCHEME
	};

	public AmpSectorSearchModel(String input,String language,
			Map<AmpAutoCompleteModelParam, Object> params) {
		super(input, language, params);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 8211300754918658832L;
	private Session session;

	@Override
	protected Collection<AmpSector> load() {
		Collection<AmpSector> ret = null;
		try {
			ret = new ArrayList<AmpSector>();
			session = PersistenceManager.getRequestDBSession();
			
//			BOZO, CONSTANTIN			
//			debug / research code: please leave it here until I remove it later
//			String myQuery = "select sec from " + AmpSector.class.getName() + " sec where sec.name like '%у%'";
//			System.out.println("aaaa start aaaa");
//			for(AmpSector ampSec:((List<AmpSector>)(session.createQuery(myQuery).list())))
//				System.out.println(ampSec.getName());
//			System.out.println("aaaa end aaaa");
			
			Integer maxResults = (Integer) getParams().get(
					AbstractAmpAutoCompleteModel.PARAM.MAX_RESULTS);
			AmpSectorScheme scheme = (AmpSectorScheme) getParams().get(
					PARAM.SECTOR_SCHEME);
			Criteria crit = session.createCriteria(AmpSector.class);
			crit.setCacheable(true);
			Junction junction = Restrictions.conjunction().add(
					Restrictions.eq("ampSecSchemeId", scheme));

			if (input.trim().length() > 0)
				junction.add(getTextCriterion("name", input));
			junction.add( Restrictions.or( Restrictions.isNull("deleted"), Restrictions.eq( "deleted", Boolean.FALSE)));
			crit.add(junction);
			crit.addOrder(Order.asc("name"));
			if (maxResults != null && maxResults != 0)
				crit.setMaxResults(maxResults);
			List<AmpSector> list = crit.list();

			ret = (Collection<AmpSector>) createTreeView(list);

		} catch (HibernateException e) {
			throw new RuntimeException(e);
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			PersistenceManager.releaseSession(session);
		}
		return ret;
	}

}
