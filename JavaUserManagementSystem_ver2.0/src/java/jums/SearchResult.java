package jums;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import javax.servlet.http.HttpSession;
/**
 *
 * @author hayashi-s
 */
public class SearchResult extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            HttpSession hs = request.getSession();
            
            request.setCharacterEncoding("UTF-8");//リクエストパラメータの文字コードをUTF-8に変更

            //フォームからの入力を取得して、JavaBeansに格納
            UserDataBeans udb = new UserDataBeans();
            udb.setName(request.getParameter("name"));
            udb.setYear(request.getParameter("year"));
            udb.setType(request.getParameter("type"));
            
            //フォームの内容をセッションに格納。検索画面に戻ってくるときに使用
            hs.setAttribute("search",udb);
            
            //DTOオブジェクトにマッピング。DB専用のパラメータに変換
            UserDataDTO searchData = new UserDataDTO();
            udb.UD2DTOMapping(searchData);

            ArrayList resultData = UserDataDAO .getInstance().search(searchData);
            request.setAttribute("resultData", resultData);
            
            request.getRequestDispatcher("/searchresult.jsp").forward(request, response);
        }catch(IllegalMonitorStateException e){
            request.setAttribute("error", "データベースへの接続に失敗しました");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }catch(SQLException e){
            request.setAttribute("error","データベースの操作に失敗しました");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }catch(Exception e){
            //何らかの理由で失敗したらエラーページにエラー文を渡して表示。想定は不正なアクセスとDBエラー
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
        
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}