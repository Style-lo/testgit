package cn.gzsxt.dao;

import org.apache.solr.client.solrj.SolrQuery;

import cn.gzsxt.bean.ResultModel;

public interface ProductDao {
	// 根据页面传入的条件，查询商品,返回结果
	public ResultModel queryProductList(SolrQuery solrQuery);
}
