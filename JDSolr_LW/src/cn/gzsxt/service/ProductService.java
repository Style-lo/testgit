package cn.gzsxt.service;

import cn.gzsxt.bean.ResultModel;

public interface ProductService {
	public ResultModel queryProductList(String queryString, String catalogName, String price, String sort, Integer currentPage);
}
