package cn.gzsxt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.gzsxt.bean.ResultModel;
import cn.gzsxt.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@RequestMapping("/list.gzsxt")
	public String queryProductList(String queryString, String catalogName,
			String price, String sort, Integer currentPage, Model model) {
		ResultModel result = productService.queryProductList(queryString, catalogName, price, sort, currentPage);
		model.addAttribute("result", result);
		model.addAttribute("queryString", queryString);
		model.addAttribute("caltalog_name", catalogName);
		model.addAttribute("price", price);
		model.addAttribute("sort", sort);
		model.addAttribute("page", currentPage);
		return "product_list";
	}
}
