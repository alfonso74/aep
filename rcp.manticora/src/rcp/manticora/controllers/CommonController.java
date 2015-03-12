package rcp.manticora.controllers;

import rcp.manticora.dao.CommonDAO2;

public class CommonController extends AbstractCommonController<Object> {

	public CommonController() {
		super(new CommonDAO2());
	}

}
