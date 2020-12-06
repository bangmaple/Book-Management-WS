package bangnn.book.filters;


import bangnn.book.daos.UserDAO;
import bangnn.main.JWTUtil;
import org.glassfish.jersey.server.spi.Container;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Provider
public class JWTAuthenticationFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String USER_WS_END_POINT = "api/v1/users";
    private static final List<String> END_POINTS = Arrays.asList("api/v1/books", "api/v1/users");

    @Context
    private UriInfo uriInfo;

    private final UserDAO dao = UserDAO.getInstance();

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        //Get Authorization token from header
        String token = ctx.getHeaderString("Authorization");
        //Check if the Authorization header is bound.
        if (token != null) {
            //Validate the public key of JWT with the server private key
            if (JWTUtil.validateToken(token)) {
                //Check if the user is logged in (In case of re-use JWT after logged out).
                String username = JWTUtil.getUsernameFromJWT(token);
                if (dao.isLoggedIn(username)) {
                    return;
                }
            }
        } else {
            //If Authorization is not found and user want to login.
            if (uriInfo.getRequestUri().getPath().contains(USER_WS_END_POINT)) {
                return;
            }
        }
        ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }

    @Override
    public void filter(ContainerRequestContext reqCtx, ContainerResponseContext resCtx) throws IOException {
        String token = reqCtx.getHeaderString("Authorization");
        if (token != null) {
            String username = JWTUtil.getUsernameFromJWT(token);
            //Get new JWT Token
            token = JWTUtil.generateToken(username);
            //Add new JWT token to the header for responding
            resCtx.getHeaders().add("Authorization", token);
        }
    }
}
