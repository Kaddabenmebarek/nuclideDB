package org.research.kadda.nuclide.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.QueryHints;
import org.research.kadda.nuclide.entity.NuclideDisposalRoute;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional
public class NuclideDisposalRouteDaoImpl implements NuclideDisposalRouteDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NuclideDisposalRoute> findAll() {
		return sessionFactory.getCurrentSession().createQuery("from NuclideDisposalRoute").setHint(QueryHints.HINT_CACHEABLE, true).list();
	}

}
