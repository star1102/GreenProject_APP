package Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import command.CommandAction;

@WebServlet(
		name = "BoardController", 
		urlPatterns = { 
				"/BoardController", 
				"*.it"
		}, 
		initParams = { 
				@WebInitParam(name = "propertyConfig", value = "commandBoard.properties")
		})
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private HashMap<String, CommandAction> commandSet = new HashMap();
       
    public Controller() {
        super();
    }
    
    public void init(ServletConfig config){
    	String props = config.getInitParameter("propertyConfig");
    	//props = "commandBoard.properties";
    	ServletContext context = config.getServletContext();
    	String filename =  context.getRealPath("/property"); //컨텍스트의(웹어플) 실제 경로(물리적 경로)
    	filename +="\\"+props;
    	Properties pr = new Properties();
    	//filename = "D:\\~~~~~~~\\commandBoard.properties"
    	try{
    		FileInputStream file = new FileInputStream(filename);
    	   	pr.load(file);
    	   	file.close();
    	}catch(FileNotFoundException e){
    		e.printStackTrace();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    	
    	Iterator keyIter = pr.keySet().iterator();
    	while(keyIter.hasNext()){
    		String command = (String)keyIter.next();
    		String className = pr.getProperty(command);
    		//className = "command.QnaListAction";
    		try{
    			Class commandClass = Class.forName(className);
	    		CommandAction action 
	    			= (CommandAction)commandClass.newInstance();
	    		//CommandAction action = new command.QnaListAction();
	    		commandSet.put(command, action);    
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}    	
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String command = request.getRequestURI();
		//command = "/Board2/index.do"
		command = command.substring(request.getContextPath().length());
		//command = "/index.do";
		CommandAction obj = commandSet.get(command);
		String goUrl = obj.process(request, response);
		if(goUrl != null){
			RequestDispatcher dispatcher 
					= request.getRequestDispatcher(goUrl);
			dispatcher.forward(request, response);
		}
	}
}
