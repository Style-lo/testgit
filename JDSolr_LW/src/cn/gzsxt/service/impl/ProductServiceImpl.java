package cn.gzsxt.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.gzsxt.bean.ResultModel;
import cn.gzsxt.dao.ProductDao;
import cn.gzsxt.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Override
	public ResultModel queryProductList(String queryString, String catalogName,
			String price, String sort, Integer currentPage) {
		// 根据前台页面传入的参数，封装SolrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		// 设置搜索关键字
		if (StringUtils.isEmpty(queryString)) {
			solrQuery.setQuery("*:*");
		} else {
			solrQuery.setQuery(queryString);
		}
		// 设置过滤条件-->根据分类名称
		if (!StringUtils.isEmpty(catalogName)) {
			solrQuery.addFilterQuery("product_catalog_name:" + catalogName);
		}
		// 获取价格区间-->根据价格区间
		if (!StringUtils.isEmpty(price)) {
			String[] str = price.split("-");
			if (2 == str.length) {
				solrQuery.addFilterQuery("product_price:[" + str[0] + " TO "
						+ str[1] + "]");
			} else {
				solrQuery.addFilterQuery("product_price:["
						+ price.substring(0, 2) + " TO *]");
			}
		}
		// 设置排序 1:降序 0:升序
		if ("1".equals(sort)) {
			solrQuery.setSort("product_price", ORDER.desc);
		} else {
			solrQuery.setSort("product_price", ORDER.asc);
		}
		if (currentPage == null || "".equals(currentPage)) {
			currentPage = 1;
		}
		// 设置分页信息
		solrQuery.setStart((currentPage - 1) * 20);
		solrQuery.setRows(20);

		// 设置默认搜索域
		solrQuery.set("df", "product_name");

		// 设置高亮
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("product_name");
		solrQuery.setHighlightSimplePre("<font style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</font>");
		ResultModel resultModel = productDao.queryProductList(solrQuery);
		resultModel.setCurrentPage(currentPage);
		// 计算总页数
		if (0 == resultModel.getRecordCount() % 20) {
			resultModel.setPageCount((int)(resultModel.getRecordCount() / 20));
		} else {
			resultModel.setPageCount((int)(resultModel.getRecordCount() / 20) + 1);
		}
		return resultModel;
	}

}
