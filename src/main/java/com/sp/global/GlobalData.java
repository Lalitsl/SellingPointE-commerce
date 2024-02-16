package com.sp.global;

import java.util.ArrayList;
import java.util.List;

import com.sp.entities.Product;

public class GlobalData {

	public static List<Product> cart;
	public static List<Product> liked;
	
	static {
		cart = new ArrayList<Product>();
		liked = new ArrayList<Product>();
	}
}
