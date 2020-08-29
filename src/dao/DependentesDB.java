package dao;

import java.util.Iterator;
import java.util.List;

import model.Dependente;

public class DependentesDB {

	public void save(Dependente dependente) {
		
	}

	public void saveAll(List<Dependente> dep) {
		for (Iterator iterator = dep.iterator(); iterator.hasNext();) {
			this.save( (Dependente) iterator.next());
		}
	}
}
