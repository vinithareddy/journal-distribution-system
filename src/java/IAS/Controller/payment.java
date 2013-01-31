/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IAS.Controller;

import IAS.Class.Ajax.AjaxResponse;
import IAS.Class.Ajax.AjaxSuccess;
import IAS.Model.Payment.PaymentModel;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author smahapat
 */
public class payment extends JDSController {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int invoice_id = Integer.parseInt(request.getParameter("id"));
        //int subscription_id = Integer.parseInt(request.getParameter("subscriptionid"));
        String remarks = request.getParameter("remarks");
        float payment = Float.parseFloat(request.getParameter("payment"));
        int inward_id = Integer.parseInt(request.getParameter("inwardid"));

        try {
            PaymentModel paymentmodel = new PaymentModel();
            int rc = paymentmodel.UpdatePayment(invoice_id, inward_id, payment, remarks);
            AjaxResponse ajaxresponse = new AjaxResponse();
            String xml;
            if (rc == 1) {
                xml = ajaxresponse.getSuccessXML(true, "");
            } else {
                xml = ajaxresponse.getSuccessXML(false, "");
            }
            response.getWriter().write(xml);
        } catch (SQLException | ParserConfigurationException | TransformerException ex) {
            throw new ServletException(ex);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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