package your.bank;

import com.mashape.unirest.http.Unirest;
import jooby.helpers.UnirestHelper;
import org.jooby.Jooby;
import org.jooby.Mutant;
import org.jooby.Results;
import org.jooby.hbs.Hbs;
import org.jooby.jdbc.Jdbc;
import org.jooby.json.Jackson;

import javax.sql.DataSource;
import java.util.Random;

/**
 * @author jooby generator
 */
public class App extends Jooby {

  {
    // -- Start Boilerplate Setup --
    use(new UnirestHelper());
    use(new Hbs());
    use(new Jackson());
    use(new Jdbc("db"));

    assets("/bootstrap/**");
    assets("/static/**");

    // -- End Boilerplate Setup --

    // Simple GET Request
    get("/", () -> "Hello World!");


    // GET request which makes a call to another endpoint
    get("/hello",() -> {
      return "Hello " + Unirest.get("http://faker.hook.io/").asString().getBody();
    });

    // GET request with a template and a query parameter /dice?name=Jim
    get("/dice", (request)->{
      String name = "Your";

      // Mutant is Jooby's representation of data which has not been cast to a type (and could be null/not set)
      Mutant query = request.param("name");
      if(query.isSet()) {
        name = query.value() + "'s";
      }

      // This renders public/dice.html with the model populated with random and name
      return Results.html("dice")
              .put("random",new Random().nextInt(6))
              .put("name",name);
    });

    // Perform actions on startup
    onStart(() -> {
      System.out.println("Starting Up...");

        DataSource ds = require(DataSource.class);

    });

    // Perform actions after startup
    onStarted(() -> {
      System.out.println("Started!");
    });

  }

  public static void main(final String[] args) {
    run(App::new, args);
  }

}
