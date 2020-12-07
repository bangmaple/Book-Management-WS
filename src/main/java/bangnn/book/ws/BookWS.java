/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bangnn.book.ws;

import bangnn.book.daos.BookDAO;
import bangnn.book.models.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

/**
 * @author bangmaple
 */
@Path("v1/books")
public class BookWS {

    private final BookDAO dao = BookDAO.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks() {
        return Response.ok().entity(dao.retrieveAllBooks()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{start}/{end}")
    public Response getBooksPaginationOffset(@PathParam("start") int start, @PathParam("end") int end) {
        return Response.ok().entity(dao.retrieveBooksPagination(start, end)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("page/{pageNum}")
    public Response getBooksPaginationOffset(@PathParam("pageNum") int pageNum) {
        int BOOKS_PER_PAGE = 5;
        int start = pageNum * BOOKS_PER_PAGE;
        int end = start + BOOKS_PER_PAGE;
        return Response.ok().entity(dao.retrieveBooksPagination(start, end)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("likeName")
    public List<Book> getBooksByLikeName(@QueryParam("title") String title) {
        return dao.retrieveBooksByLikeName(title);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBook(Book b) {
        boolean check = dao.insertToDB(b);
        ObjectMapper mapper = new ObjectMapper();
        if (check) {
            ObjectNode obj = mapper.createObjectNode();
            obj.put("status", "Successfully added a new book with ID " + b.getIsbn());
            return Response.status(Status.CREATED)
                    .entity(obj)
                    .build();
        }
        ObjectNode obj = mapper.createObjectNode();
        obj.put("status", "Failed to added a new book. Duplicated ID?");
        return Response.status(Status.NOT_ACCEPTABLE)
                .entity(obj)
                .build();
    }

    @GET
    @Path("{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Book getBookByISBN(@PathParam("isbn") String isbn) {
        return dao.retrieveBookByISBN(isbn);
    }

    @PATCH
    @Path("{isbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(Book b) {
        boolean check = dao.updateBookToDB(b);
        ObjectMapper mapper = new ObjectMapper();
        if (check) {
            ObjectNode obj = mapper.createObjectNode();
            obj.put("status", "Successfully updated the book!");
            return Response.status(Status.ACCEPTED)
                    .entity(obj)
                    .build();
        }
        ObjectNode obj = mapper.createObjectNode();
        obj.put("status", "Failed to update the book! ISBN not found?");
        return Response.status(Status.NOT_ACCEPTABLE).entity(null).build();
    }

    @DELETE
    @Path("{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(@PathParam("isbn") String isbn) {
        boolean check = dao.removeBookFromDBByISBN(isbn);

        ObjectMapper mapper = new ObjectMapper();
        if (check) {
            ObjectNode obj = mapper.createObjectNode();
            obj.put("status", "Successfully deleted that book");
            return Response
                    .status(Status.ACCEPTED)
                    .entity(obj)
                    .build();
        }
        ObjectNode obj = mapper.createObjectNode();
        obj.put("status", "Failed to delete the book with ISBN " + isbn + ". ISBN not existed?");
        return Response
                .status(Status.NOT_ACCEPTABLE)
                .entity(obj)
                .build();
    }
}
