package com.tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.connectors.http.MagentoProductCalls;
import com.connectors.mongo.MongoConnector;
import com.tools.constants.SoapKeys;
import com.tools.data.soap.ProductDetailedModel;
import com.tools.persistance.MongoWriter;
import com.tools.requirements.Application;
import com.workflows.frontend.AddProductsWorkflow;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Story;
import net.thucydides.core.annotations.WithTag;

@WithTag(name = "US3.1 Shop for myself VAT valid and no SMB billing and shipping AT", type = "Scenarios")
@Story(Application.ShopForMyselfCart.US3_1.class)
@RunWith(SerenityRunner.class)
public class CreateProductsPlaceCustomerCartTest extends BaseTest {

	private ProductDetailedModel genProduct1 = new ProductDetailedModel();
	private ProductDetailedModel genProduct2 = new ProductDetailedModel();
	private ProductDetailedModel genProduct3 = new ProductDetailedModel();
	
	
	String voucherValue;

	@Steps
	public AddProductsWorkflow addProductsWorkflow;
	public static List<ProductDetailedModel> productsList = new ArrayList<ProductDetailedModel>();

	@Before
	public void setUp() throws Exception {

		MongoConnector.cleanCollection(getClass().getSimpleName() + SoapKeys.GRAB);
	}

	@Test
	public void createProductsTest() {
		//// vdv
		genProduct1 = MagentoProductCalls.createProductModel();
		genProduct1.setPrice("90.00");
		genProduct1.setIp("76");
		genProduct1.setSku("0102056NATA080");
		genProduct1.setName("Marie Jo SOFIA Unterlegter BH natur 80A");
		genProduct1.setColor("natur");
		genProduct1.setProductSize("80A");
		genProduct1.setParentProductSku("marie-jo-sofia-unterlegter-bh-0102056");
		productsList.add(genProduct1);
		
		genProduct2 = MagentoProductCalls.createProductModel();
		genProduct2.setPrice("120.00");
		genProduct2.setName("Marie Jo SOFIA Unterlegter BH schwarz 75A");
		genProduct2.setIp("101");
		genProduct2.setSku("0102056ZWAA075");
		genProduct2.setColor("schwarz");
		genProduct2.setProductSize("75A");
		genProduct2.setParentProductSku("marie-jo-sofia-unterlegter-bh-0102056");
		productsList.add(genProduct2);
		
		genProduct3 = MagentoProductCalls.createProductModel();
		genProduct3.setPrice("79.90");
		genProduct3.setName("PrimaDonna PERLE Shapewear Body caffé latte 85B");
		genProduct3.setIp("67");
		genProduct3.setSku("0462342CALB085");
		genProduct3.setColor("caffé latte");
		genProduct3.setProductSize("85B");
		genProduct3.setParentProductSku("primadonna-perle-shapewear-body-0462342");
		productsList.add(genProduct3);

		
	
	}

	@After
	public void saveData() {
		for (ProductDetailedModel product : productsList) {
			MongoWriter.saveProductDetailedModel(product, getClass().getSimpleName() + SoapKeys.GRAB);
		}
	}

}
