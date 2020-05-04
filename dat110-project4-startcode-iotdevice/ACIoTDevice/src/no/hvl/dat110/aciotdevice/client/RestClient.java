package no.hvl.dat110.aciotdevice.client;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

public class RestClient {
	private OkHttpClient client;
	private Gson gson;
	public static final MediaType JSON
			= MediaType.parse("application/json; charset=utf-8");


	public RestClient() {
		gson = new Gson();
		client = new OkHttpClient();
	}

	private static String logpath = "/accessdevice/log";

	public void doPostAccessEntry(String message) {

		AccessMessage amsg = new AccessMessage(message);

		RequestBody body = RequestBody.create(JSON, gson.toJson(amsg));

		String url = Configuration.host + logpath;
		String urlwithport = Configuration.host + Configuration.port + logpath; //brukes bare om portnummer må spesifiseres

		Request request = new Request.Builder()
				.url(url)
				.post(body)
				.build();

		try (Response response = client.newCall(request).execute()){
			System.out.println("doPostAccessEntry() body: "+ response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	
	private static String codepath = "/accessdevice/code";
	
	public AccessCode doGetAccessCode() {

		AccessCode code = null;
		String url = Configuration.host + codepath;
		String urlwithport = Configuration.host + Configuration.port + codepath; //brukes bare om portnummer må spesifiseres

		Request req = new Request.Builder()
				.url(url)
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
