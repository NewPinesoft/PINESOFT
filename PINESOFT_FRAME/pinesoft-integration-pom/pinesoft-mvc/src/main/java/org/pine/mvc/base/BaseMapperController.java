package org.pine.mvc.base;

import org.pine.ibaits.mapper.contract.IMapper;
import org.pine.mvc.MvcProperties;

public abstract class BaseMapperController<T> extends MvcProperties {
	public IMapper<T> mapper;

	public BaseMapperController(IMapper<T> mapper)
	{
		this.mapper=mapper;
	}
}
