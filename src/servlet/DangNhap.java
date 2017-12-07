package servlet;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

//@WebServlet("/DangNhap")
public class DangNhap extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DangNhap() {
		super();
	}

	@Override
	/*public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			Class.forName("com.mysql.jdbc.Driver");// initialize jdbc driver
		} catch (ClassNotFoundException e) {
			throw new ServletException(e);
		}
	}*/

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		if (username != null)
			request.getRequestDispatcher("trangchu").forward(request, response);
		else
			request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer maloainguoidung = Integer.valueOf(request.getParameter("loainguoidung"));
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Connection c = null;

		try {
			c=connect.DBConnect.getConnection();
			/*String url = "jdbc:mysql://localhost/quanlydetainghiencuukhoahoc?user=root&password=15110376";*/
			String sql = "select * from taikhoan, nguoidung where taikhoan.MaNguoiDung = nguoidung.MaNguoiDung and taikhoan.tendangnhap = "
					+ "'" + username + "'" + "and nguoidung.maloainguoidung = " + "'" + maloainguoidung + "'";
			/*c = (Connection) DriverManager.getConnection(url);*/
			Statement stmt = (Statement) c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				if (rs.getString("tendangnhap").equals(username) && rs.getString("matkhau").equals(password)) {
					HttpSession session = request.getSession();
					session.setAttribute("username", username);
					response.sendRedirect("trangchu");
				} else
					response.sendRedirect("dangnhap");
			} else
				response.sendRedirect("dangnhap");

		} catch (Exception e) {
			throw new ServletException(e);
		}

		finally {
			try {
				if (c != null)
					c.close();
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}

	}

}
