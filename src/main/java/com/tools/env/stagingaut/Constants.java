package com.tools.env.stagingaut;

import java.io.File;

/**
 * Need to make soap and environment always point to the same environment.
 * Issues might arise.
 * 
 * @author voicu.vac
 *
 */
public class Constants {

	
	//Staging AUT uRLs
	public static final String BASE_FE_URL = "http://staging-aut.pippajean.com/";
	public static final String BASE_URL_BE = "http://pippajean:sn0wMob1l!@staging-aut.pippajean.com/index.php/admin/";
	public static final String BASE_URL_FE = "http://pippajean:sn0wMob1l!@staging-aut.pippajean.com/customer/account/create/";
	
	public static final String BASE_URL = "https://staging-aut.pippajean.com/customer/account/login/";
	public static final String BASE_URL_AUT = "https://staging-aut.pippajean.com/customer/account/login/";
	public static final String URL_CART_SUCCESS = "https://staging-aut.pippajean.com/simona/simplecheckout/process/success/";
	public static final String PROFILE_HISTORY_URL = "https://staging-aut.pippajean.com/simona/sales/order/history/";
	public static final String WISHLIST_URL = "https://staging-aut.pippajean.com/qateam/wishlist/";
	public static final String STYLISTS_CUSTOMER_ORDER_REPORT = "http://staging-aut.pippajean.com/qateam/stylereports/order/myorders/?type=customerorders";
	public static final String CART_PAGE_URL = "http://staging-aut.pippajean.com/simona/checkout/cart/";
	public static final String BE_URL_RULE_BUY3GET1 = "https://staging-aut.pippajean.com/index.php/admin/promo_quote/edit/id/5466/key/";
	public static final String BE_URL_RULE_BUY3GET1_FOR_REGULAR = "https://staging-aut.pippajean.com/index.php/admin/promo_quote/edit/id/5467/key/";
	public static final String PARTY_DETAILS_URL = "http://staging-aut.pippajean.com/qateam/stylist/party/details/id/";
	public static final String BE_URL_RULE_BUY3GET1_FOR_HOST = "https://staging-aut.pippajean.com/index.php/admin/promo_quote/edit/id/5478/";

	public static final String API_URI = "https://staging-aut.pippajean.com/index.php/api/v2_soap/index/";
	
	// SOAP user authentication
	public static final String LOGIN_USER_SOAP = "stagingaut";
	public static final String LOGIN_PASS_SOAP = "stagingaut1";


	public static final String URL_WEB_MAIL = "http://mailinator.com/";
	public static final String REGISTER_LANDING_PAGE = "register-landing-page";
	public static final String LANDING_PAGE = "contact-landing-page";

	public static final String RESOURCES_PATH = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "staging-aut" + File.separator;
	public static final String BE_USER = "admin";
	public static final String BE_PASS = "admin1234";
	public static final String BE_STYLIST = "simona.popa@evozon.com";

	public static final String PROFILE_HISTORY = "MEINE BESTELLUNGEN";
	
//	public static final String WEB_MAIL = "mailinator.com";
//	public static final String US_03_FOLDER = "us3";
//	public static final String US_05_FOLDER = "us5";
	
	//messages
	public static final String EXISTING_ACCOUNT_MESSAGE = "WICHTIG: Wenn Du bereits einen PIPPA&JEAN Benutzerkonto hast, dann melde dich bitte hier an, bevor Du die Style Coach Registrierung ausführst!";
	public static final String STYLE_COACH_REG_PAGE_TITLE = "REGISTRIERE DICH ALS STYLE COACH UND ERWERBE DAS STARTER-SET";
	public static final String VOUCHER_DISCOUNT_INCOMPATIBLE = "Gutscheine können nicht in Verbindung mit Schmuckbonus verwendet werden.";
	public static final String PARTY_PLANNED = "Party geplant";
	public static final String PARTY_ACTIVE = "Partytermin vorüber";
	public static final String PARTY_CLOSED = "Party geschlossen";
	public static final String BOUNUS_SUCCESS_MESSAGE = "Bonus was successfully saved";
	public static final String INVITE_EMAIL_SUBJECT = "lädt Dich ein zur PIPPA&JEAN Style Party";
	public static final String PARTY_CREATION_EMAIL_SUBJECT = "PIPPA&JEAN Style Party wurde für Dich angelegt";
	
}