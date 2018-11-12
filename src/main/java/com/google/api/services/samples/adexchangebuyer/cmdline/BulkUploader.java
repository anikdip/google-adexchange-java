package com.google.api.services.samples.adexchangebuyer.cmdline;

import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.adexchangebuyer.AdExchangeBuyer;
import com.google.api.services.adexchangebuyer.model.Account;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class BulkUploader extends BaseSample {
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static HttpRequestFactory REQ_FACTORY;
    private static HttpTransport TRANSPORT;
    @Override
    public BaseSample.ClientType getClientType() {
        return BaseSample.ClientType.ADEXCHANGEBUYER;
    }

    @Override
    public String getName() {
        return "Bulk Uploader";
    }

    @Override
    public String getDescription() {
        return "Upload various ad IDs to Google user lists for targeting and upload user lists.";
    }

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

    @Override
    public void execute(AbstractGoogleJsonClient client) throws IOException, ParseException {
        AdExchangeBuyer adXClient = (AdExchangeBuyer) client;
        List<Account> allAccounts = adXClient.accounts().list().execute().getItems();

        System.out.println("Give Sample Data As Json Format:");
        JSONObject user_list = new JSONObject(Utils.readInputLine());
        //user_list = Utils.readInputJson();

        if (allAccounts != null && allAccounts.size() > 0) {

            for (Account account : allAccounts) {
                for (Account account_info : allAccounts) {
                    String request_url = "https://cm.g.doubleclick.net/upload?nid="+account_info.getCookieMatchingNid();

                    GenericUrl url = new GenericUrl(request_url);
                    HttpContent content = new JsonHttpContent(JSON_FACTORY, user_list);
                    reqFactory().buildPostRequest(url, content).execute();
                }
            }
        } else {
            System.out.printf("No accounts were found associated to this user\n");
        }
    }
}
