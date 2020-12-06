/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bangnn.main;

import javax.ws.rs.ApplicationPath;

import bangnn.book.filters.JWTAuthenticationFilter;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author bangmaple
 */
@ApplicationPath("api")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        packages("bangnn.book.ws");
        register(JWTAuthenticationFilter.class);
    }

}
