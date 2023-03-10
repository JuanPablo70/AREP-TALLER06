package edu.eci.arep.app;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.print.Doc;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static spark.Spark.*;

public class App {

    private static String urlDB = "34.236.242.6:27017";
    private static String urlc = "mongodb://54.163.23.254:27017//?retryWrites=true&w=majority";
    private static MongoClient client = null;
    private static MongoDatabase database = null;
    private static MongoCollection<Document> collection;

    public static void main(String[] args) {
        port(getPort());
        get("/logs", (req,res) -> {
            dbConnection();
            List<String> data = new ArrayList<>();
            for (Document document: collection.find()) {
                data.add(document.toJson());
            }
            client.close();
            return data.subList(Math.max(data.size() - 10, 0), data.size());
        });

        post("/logs", (req,res) -> {
            dbConnection();
            if (req.body() != null) {
                Document doc = new Document();
                doc.put("word", req.body());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                doc.put("date", format.format(new Date()));
                collection.insertOne(doc);
            }
            List<String> data = new ArrayList<>();
            for (Document document: collection.find()) {
                data.add(document.toJson());
            }
            client.close();
            return data.subList(Math.max(data.size() - 10, 0), data.size());
        });

    }

    public static void dbConnection() {
        client = new MongoClient(urlDB);
        database = client.getDatabase("admin");
        collection = database.getCollection("logs");
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35001;
    }
}