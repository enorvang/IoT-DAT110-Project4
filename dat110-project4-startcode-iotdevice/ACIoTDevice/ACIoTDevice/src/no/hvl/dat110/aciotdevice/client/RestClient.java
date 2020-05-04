package no.hvl.dat110.aciotdevice.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import sun.security.krb5.Config;

import java.io.IOException;

public class RestClient {
	private OkHttpClient client;
	private Gson gson;
	public static final MediaType JSON
			= MediaType.parse("application/json; charset=utf-8");


	public RestClient() {
		// TODO Auto-generated constructor stub
		gson = new Gson();
		client = new OkHttpClient();
	}

	private static String logpath = "/accessdevice/log";

	public void doPostAccessEntry(String message) {

		AccessMessage amsg = new AccessMessage(message);

		RequestBody body = RequestBody.create(JSON, gson.toJson(amsg));

		Request request = new Request.Builder()
				.url(Configuration.host + Configuration.port + logpath)
				.post(body)
				.build();

		try (Response response = client.newCall(request).execute()){
			System.out.println("doPostAccessEntry() body: "+ response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// TODO: implement a HTTP POST on the service to post the message
		
	}
	
	private static String codepath = "/accessdevice/code";
	
	public AccessCode doGetAccessCode() {

		AccessCode code = null;

		Request req = new Request.Builder()
				.url(Configuration.host + Configuration.port + codepath)
				.get()
				.build();

		try (Response response = client.newCall(req).execute()){

			code = gson.fromJson(response.body().string(), AccessCode.class);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// TODO: implement a HTTP GET on the service to get current access code
		
		return code;
	}
}
