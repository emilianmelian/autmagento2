
package com.tools.data.soap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailedModel {

	private String type;
	private String set;
	private String sku;
	private List<String> categoriesArray = new ArrayList<String>();
	private List<String> websiteArray = new ArrayList<String>();
	private List<String> availableCartsArray = new ArrayList<String>();
	private String name;
	private String description;
	private String shortDescription;
	private String weight;
	private String status;
	private String urlKey;
	private String urlPath;
	private String visibility;
	private List<String> categoryIdsArray = new ArrayList<String>();
	private List<String> websiteIdsArray = new ArrayList<String>();
	private String hasOptions;
	private String giftMessageAvailable;
	private String price;
	private String specialPrice;
	private String specialFromDate;
	private String specialToDate;
	private String taxClassId;
	private List<TierPriceModel> tierPrice = new ArrayList<TierPriceModel>();
	private String metaTitle;
	private String metaKeyword;
	private String metaDescription;
	private String customDesign;
	private String customLayoutUpdate;
	private String optionsContainer;
	private StockDataModel stockData;
	private Map<String, String> additionalAttributes = new HashMap<String, String>();
	private String store;
	private String ip;
	private String newsFromDate;
	private String newsToDate;
	private String jewerlyBonusValue;
	private String jewelryBonusCart;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public List<String> getCategoriesArray() {
		return categoriesArray;
	}

	public void setCategoriesArray(List<String> categoriesArray) {
		this.categoriesArray = categoriesArray;
	}

	public List<String> getWebsiteArray() {
		return websiteArray;
	}

	public void setWebsiteArray(List<String> websiteArray) {
		this.websiteArray = websiteArray;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUrlKey() {
		return urlKey;
	}

	public void setUrlKey(String urlKey) {
		this.urlKey = urlKey;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public List<String> getCategoryIdsArray() {
		return categoryIdsArray;
	}

	public void setCategoryIdsArray(List<String> categoryIdsArray) {
		this.categoryIdsArray = categoryIdsArray;
	}

	public List<String> getWebsiteIdsArray() {
		return websiteIdsArray;
	}

	public void setWebsiteIdsArray(List<String> websiteIdsArray) {
		this.websiteIdsArray = websiteIdsArray;
	}

	public String getHasOptions() {
		return hasOptions;
	}

	public void setHasOptions(String hasOptions) {
		this.hasOptions = hasOptions;
	}

	public String getGiftMessageAvailable() {
		return giftMessageAvailable;
	}

	public void setGiftMessageAvailable(String giftMessageAvailable) {
		this.giftMessageAvailable = giftMessageAvailable;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSpecialPrice() {
		return specialPrice;
	}

	public void setSpecialPrice(String specialPrice) {
		this.specialPrice = specialPrice;
	}

	public String getSpecialFromDate() {
		return specialFromDate;
	}

	public void setSpecialFromDate(String specialFromDate) {
		this.specialFromDate = specialFromDate;
	}

	public String getSpecialToDate() {
		return specialToDate;
	}

	public void setSpecialToDate(String specialToDate) {
		this.specialToDate = specialToDate;
	}

	public String getTaxClassId() {
		return taxClassId;
	}

	public void setTaxClassId(String taxClassId) {
		this.taxClassId = taxClassId;
	}

	public List<TierPriceModel> getTierPriceList() {
		return tierPrice;
	}

	public void setTierPrice(List<TierPriceModel> tierPrice) {
		this.tierPrice = tierPrice;
	}

	public String getMetaTitle() {
		return metaTitle;
	}

	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}

	public String getMetaKeyword() {
		return metaKeyword;
	}

	public void setMetaKeyword(String metaKeyword) {
		this.metaKeyword = metaKeyword;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	public String getCustomDesign() {
		return customDesign;
	}

	public void setCustomDesign(String customDesign) {
		this.customDesign = customDesign;
	}

	public String getCustomLayoutUpdate() {
		return customLayoutUpdate;
	}

	public void setCustomLayoutUpdate(String customLayoutUpdate) {
		this.customLayoutUpdate = customLayoutUpdate;
	}

	public String getOptionsContainer() {
		return optionsContainer;
	}

	public void setOptionsContainer(String optionsContainer) {
		this.optionsContainer = optionsContainer;
	}

	public StockDataModel getStockData() {
		return stockData;
	}

	public void setStockData(StockDataModel stockData) {
		this.stockData = stockData;
	}

	public Map<String, String> getAdditionalAttributes() {
		return additionalAttributes;
	}

	public void setAdditionalAttributes(Map<String, String> additionalAttributes) {
		this.additionalAttributes = additionalAttributes;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getNewsFromDate() {
		return newsFromDate;
	}

	public void setNewsFromDate(String newsFromDate) {
		this.newsFromDate = newsFromDate;
	}

	public String getNewsToDate() {
		return newsToDate;
	}

	public void setNewsToDate(String newsToDate) {
		this.newsToDate = newsToDate;
	}

	public List<String> getAvailableCartsArray() {
		return availableCartsArray;
	}

	public void setAvailableCartsArray(List<String> availableCartsArray) {
		this.availableCartsArray = availableCartsArray;
	}

	public String getJewerlyBonusValue() {
		return jewerlyBonusValue;
	}

	public void setJewerlyBonusValue(String jewerlyBonusValue) {
		this.jewerlyBonusValue = jewerlyBonusValue;
	}

	public String getJewelryBonusCart() {
		return jewelryBonusCart;
	}

	public void setJewelryBonusCart(String jewelryBonusCart) {
		this.jewelryBonusCart = jewelryBonusCart;
	}

	public String toString() {
		return sku + ", " + name;

	}
}
