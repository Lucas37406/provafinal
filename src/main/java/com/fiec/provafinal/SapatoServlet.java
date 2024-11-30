package com.fiec.provafinal;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/produtos")
public class SapatoServlet extends HttpServlet {
    ;

    public SapatoServlet(){

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*String nome = request.getParameter("nome");
        String preco = request.getParameter("preco");
        String imagem = request.getParameter("imagem");
        Produto p = Produto.builder()
                .nome(nome)
                .preco(Double.valueOf(preco))
                .imagemUrl(imagem)
                .build();
         */


        response.setContentType("text/html");
        response.getWriter().println("Produto Salvo");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");

        //response.getWriter().println(produtos.stream().map(Produto::toString).collect(Collectors.toList()));

    }
    // Update   /produtos/:id
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = getId(request);


        response.setContentType("text/html");
        response.getWriter().println("Produto Atualizado");

    }
    // Delete  /produtos/:id
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = getId(request);

        response.setContentType("text/html");
        response.getWriter().println("Produto Deletado");
    }

    private static String getId(HttpServletRequest req){
        String path = req.getPathInfo();
        String[] paths = path.split("/");
        String id = paths[paths.length - 1];
        return id;
    }

}