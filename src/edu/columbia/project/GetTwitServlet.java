package edu.columbia.project;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.columbia.project.server.*;

public class GetTwitServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User currentUser = userService.getCurrentUser();
		
		Key key = KeyFactory.createKey("TwittMap", "TwittMap");
		TwitterDownloader.download(currentUser, key);
		
		//resp.getWriter().println(twit);
		
		resp.sendRedirect("/TwittMap.jsp");
	}
}