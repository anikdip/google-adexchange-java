package com.google.api.services.samples.adexchangebuyer.bulk_uploader;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.adexchangebuyer.AdExchangeBuyer;
import com.google.api.services.adexchangebuyer.AdExchangeBuyerScopes;
import com.google.api.services.adexchangebuyer.model.Account;
import com.google.api.services.adexchangebuyer2.v2beta1.AdExchangeBuyerII;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class Upload {

    /**
     * Be sure to specify the name of your application. If the application name is
     * {@code null} or blank, the application will log a warning. Suggested format
     * is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "";

    /** Full path to JSON Key file - include file name */
    private static final java.io.File JSON_FILE =
            new java.io.File("adplay-690b2276a90e.json");

    /** Global instance of the HTTP transport. */
    private static HttpTransport httpTransport;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Authorizes the installed application to access user's protected data.
     * @throws IOException
     * */
    private static Credential authorize() throws Exception {
        return GoogleCredential.fromStream(new FileInputStream(JSON_FILE))
                .createScoped(AdExchangeBuyerScopes.all());
    }

    /**
     * Performs all necessary setup steps for running requests against the
     * Ad Exchange Buyer API.
     *
     * @return An initialized AdExchangeBuyer service object.
     */
    private static AdExchangeBuyer initAdExchangeBuyerClient(Credential credential) {
        AdExchangeBuyer client = new AdExchangeBuyer.Builder(
                httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
        return client;
    }

    /**
     * Performs all necessary setup steps for running requests against the
     * Ad Exchange Buyer II API.
     *
     * @return An initialized AdExchangeBuyerII service object.
     */
    private static AdExchangeBuyerII initAdExchangeBuyerIIClient(Credential credential) {
        AdExchangeBuyerII client = new AdExchangeBuyerII.Builder(
                httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
        return client;
    }

    private static final JsonFactory JSON_FACTORY_GOOGLE = new JacksonFactory();
    private static HttpRequestFactory REQ_FACTORY;
    private static HttpTransport TRANSPORT;

    private static HttpTransport transport() {
        if (null == TRANSPORT) {
            TRANSPORT = new NetHttpTransport();
        }
        return TRANSPORT;
    }

    private static HttpRequestFactory reqFactory() {
        if (null == REQ_FACTORY) {
            REQ_FACTORY = transport().createRequestFactory();
        }
        return REQ_FACTORY;
    }

    public static String upload(String user_list) throws Exception {
        String status = null;

        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credentials = authorize();
        AdExchangeBuyer adXBuyerClient = initAdExchangeBuyerClient(credentials);
        AdExchangeBuyerII adXBuyerIIClient = initAdExchangeBuyerIIClient(
                credentials);

        //BaseSample.ClientType clientType = sample.getClientType();
        List<Account> allAccounts = adXBuyerClient.accounts().list().execute().getItems();

        //AdExchangeBuyer adXClient = (AdExchangeBuyer) client;
        //List<Account> allAccounts = adXClient.accounts().list().execute().getItems();
        //user_list = Utils.readInputJson();

        JSONObject ListUser = new JSONObject(user_list);

        if (allAccounts != null && allAccounts.size() > 0) {

            for (Account account : allAccounts) {
                for (Account account_info : allAccounts) {
                    String request_url = "https://cm.g.doubleclick.net/upload?nid="+account_info.getCookieMatchingNid();

                    GenericUrl url = new GenericUrl(request_url);
                    HttpContent content = new JsonHttpContent(JSON_FACTORY_GOOGLE, ListUser);
                    reqFactory().buildPostRequest(url, content).execute();
                }
            }
        } else {
            status = "No accounts were found associated to this user\n";
        }

        return status;
    }
}
