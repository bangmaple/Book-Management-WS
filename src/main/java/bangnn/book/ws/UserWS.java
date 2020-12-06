package bangnn.book.ws;

import bangnn.book.daos.UserDAO;
import bangnn.book.models.User;
import bangnn.main.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("v1/users")
public class UserWS {
    private final UserDAO dao = UserDAO.getInstance();

    @Path("login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkLogin(User u) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        if (dao.checkLogin(u.getUsername(), u.getPassword())) {
            dao.setLoggedIn(u.getUsername());
            node.put("message", "success");
            String token = JWTUtil.generateToken(u.getUsername());
                return Response.ok().header("Authorization", token).entity(node).build();
        } else {
            node.put("message", "failed");
            return Response.status(Response.Status.UNAUTHORIZED).entity(node).build();
        }
    }

    @Path("logout")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkLogin(@Context HttpHeaders headers) {
        String token = headers.getHeaderString("Authorization");
        if (token != null) {
            String username = JWTUtil.getUsernameFromJWT(token);
                if (dao.revokeUser(username)) {
                    return Response.ok().build();
                } else {
                    return Response.status(Response.Status.NOT_ACCEPTABLE).build();
                }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
