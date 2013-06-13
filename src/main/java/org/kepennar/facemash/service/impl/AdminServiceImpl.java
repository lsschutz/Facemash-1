package org.kepennar.facemash.service.impl;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.kepennar.facemash.datatable.PageRequest;
import org.kepennar.facemash.datatable.PageResponse;
import org.kepennar.facemash.datatable.converter.PageRequest2Pageable;
import org.kepennar.facemash.model.Element;
import org.kepennar.facemash.repository.ElementRepository;
import org.kepennar.facemash.repository.components.ElementPredicats;
import org.kepennar.facemash.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.BooleanTemplate;

@Service("adminService")
public class AdminServiceImpl implements AdminService {

	private ElementRepository elementRepository;
	
	@Inject
	public AdminServiceImpl(ElementRepository elementRepository) {
		this.elementRepository = elementRepository;
	}
	
	@Override
	public PageResponse<Element> getElementPage(PageRequest pageRequest) {
		Preconditions.checkNotNull(pageRequest);
		
		BooleanExpression predicat = BooleanTemplate.create("1=1");
		if (!StringUtils.isEmpty(pageRequest.getsSearch())) {
			predicat = predicat.and(ElementPredicats.nameLike(pageRequest.getsSearch()));
			predicat = predicat.or(ElementPredicats.descriptionLike(pageRequest.getsSearch()));
		}
		
		Pageable pageable = PageRequest2Pageable.convert(pageRequest);
		Page<Element> page = elementRepository.findAll(predicat, pageable);
		return new PageResponse<Element>(page.getContent(), pageRequest, page.getTotalElements());

	}

}
