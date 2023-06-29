package com.debuggeando_ideas.music_app.service.common;

import java.io.Serializable;
import java.util.Set;

public interface SimpleService<E extends Serializable, ID> {
	
	public E findById(ID id);
	public Set<E> getAll();
	public E save(E model);
	public void delete(ID id);
	public E update(E model, ID id);

}
