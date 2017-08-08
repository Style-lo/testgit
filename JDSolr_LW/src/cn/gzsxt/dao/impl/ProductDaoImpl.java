package cn.gzsxt.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.gzsxt.bean.Product;
import cn.gzsxt.bean.ResultModel;
import cn.gzsxt.dao.ProductDao;

@Repository
public class ProductDaoImpl implements ProductDao {
	
	@Autowired
	private HttpSolrServer solrServer;

	@SuppressWarnings("unchecked")
	@Override
	public ResultModel queryProductList(SolrQuery solrQuery) {
		ResultModel model = null;
		QueryResponse response = null;
		try {
			model = new ResultModel();
			response = solrServer.query(solrQuery);
			SolrDocumentList results = response.getResults();
			// 给model设置商品的总数
			model.setRecordCount(results.getNumFound());
			
			// 把从索引库中搜索到的对象转换为java
			List<Product> proList = new ArrayList<Product>();
			Product product = null;
			// 高亮信息
			Map<String, Map<String, List<String>>> highlighting = response
					.getHighlighting();
			for (SolrDocument solrDocument : results) {
				product = new Product();
				product.setPid((String)solrDocument.get("id"));
				String proName = (String)solrDocument.get("product_name");
				List<String> list = null;
				if (highlighting.get(solrDocument.get("id")) != null) {
					list = highlighting.get(solrDocument.get("id")).get("product_name");
				}
				if (list != null && list.size() > 0) {
					proName = list.get(0);
				}
				// 设置productP_name之前要添加上高亮信息再设置product_name
				product.setName(proName);
				product.setPrice((float)solrDocument.get("product_price"));
				product.setCatalog_name((String)solrDocument.get("product_catalog_name"));
				product.setPicture(((ArrayList<String>)solrDocument.get("product_picture")).get(0));
				/**description此页面不需要，需要的时候根据pid去数据库查询*/
				proList.add(product);
			}
			model.setProductList(proList);
			return model;
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return null;
	}

}
