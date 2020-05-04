package no.hvl.dat110.ac.restservice;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;
import spark.Access;

/**
 * Hello world!
 */
public class App {

    static AccessLog accesslog = null;
    static AccessCode accesscode = null;

    public static void main(String[] args) {

        port(getHerokuAssignedPort());

        // objects for data stored in the service

        accesslog = new AccessLog();
        accesscode = new AccessCode();


        after((req, res) -> {
            res.type("application/json");
        });

        // for basic testing purposes
        get("/accessdevice/hello", (req, res) -> {

            Gson gson = new Gson();

            return gson.toJson("IoT Access Control Device");
        });

        get("/accessdevice/log", (req, res) -> accesslog.toJson());

        post("/accessdevice/log", (req, res) -> {

            Gson gson = new Gson();

            AccessMessage message = gson.fromJson(req.body(), AccessMessage.class);

            int id = accesslog.add(message.getMessage());

            return gson.toJson(accesslog.get(id));

        });

        get("/accessdevice/log/:id", (req, res) -> {
            String idString = req.params(":id");
            Gson gson = new Gson();

			if(idString.matches("^[0-9]*$")){
				int id = Integer.parseInt(idString);
				AccessEntry entry = accesslog.get(id);
				if (entry != null) {
					return gson.toJson(entry);
				} else {
					return gson.toJson("Invalid request...");
				}
			}else {
				return gson.toJson("Invalid request...");
			}
        });

        put("/accessdevice/code", (req, res) -> {
            Gson gson = new Gson();
            AccessCode newCode = gson.fromJson(req.body(), AccessCode.class);
            accesscode.setAccesscode(newCode.getAccesscode());
            return gson.toJson(newCode);
        });

        get("/accessdevice/code", (req,res) -> {
        	Gson gson = new Gson();
        	return gson.toJson(accesscode);
        });

        delete("/accessdevice/log", (req, res) -> {
        	accesslog.clear();
        	return accesslog.toJson();
		});

        // TODO: implement the routes required for the access control service
        // as per the HTTP/REST operations described in the project description

    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 8080; //return default port if heroku-port isn't set (i.e. on localhost)
    }

}
