package vertx.project;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class VertxApp extends AbstractVerticle {
    private Router router;
    private Path filePath;

    public static void main(String[] args) {
        io.vertx.core.Launcher.executeCommand("run", VertxApp.class.getName());
    }
  

    @Override
    public void start(Promise<Void> startPromise) {
    
    

        // creating a route
        router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST, "/analyze").handler(arg0 -> {
			try {
				
				JsonObject requestJson = arg0.getBodyAsJson();
		        String inputText = requestJson.getString("text");
		        List<String> words = Files.readAllLines(filePath);
		        String anotherLex_ = null;

		        for (String word: words) {
		            // finding the closest lexical word with comparing with the input
		            if (anotherLex_ == null || word.compareTo(inputText) > 0) {
		                anotherLex_ = word;
		            }
		        }
		        
		        int minValueDifference = Integer.MAX_VALUE;
		        String anotherVal_ = null;

		        for (String word : words) {
		            // calculating the value difference between input and each word
		        	
		        	int valdiff=0;
		        	int sumfirstword=0;
		        	int sumsecondword=0;
		        	 for (char c : inputText.toCharArray()) {
		        		 sumfirstword += Character.toLowerCase(c) - 'a' + 1;
		             }
		        	 for (char b : word.toCharArray()) {
		        		 sumsecondword += Character.toLowerCase(b) - 'a' + 1;
		             }
		            int sumf_ = sumfirstword;
		            int sums_ = sumsecondword;
		            valdiff=Math.abs(sumf_ - sums_);
		            int valueDifference = valdiff;
		            if (valueDifference < minValueDifference) {
		                minValueDifference = valueDifference;
		                anotherVal_ = word;
		            }
		        }

		       
		        String anotherVal = anotherVal_;
		        String anotherLex = anotherLex_;
		        JsonObject responseJson = new JsonObject().put("value", anotherVal).put("lexical", anotherLex);

		        HttpServerResponse response = arg0.response();
		        response.putHeader("content-type", "application/json").end(responseJson.encode());
		        filePath = Paths.get("vertx.project", "testfile.txt");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
        

        // creating an HTTP serve
        vertx.createHttpServer().requestHandler(router).listen(0, "localhost", result -> {
          
             //port is adjusted due to firewall settings
                int actualPort = result.result().actualPort();
                System.out.println("Server started on port: " + actualPort);
                startPromise.complete();
        
        });
    }

  
   
   

   
}
