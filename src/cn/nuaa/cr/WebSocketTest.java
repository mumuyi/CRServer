package cn.nuaa.cr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.nuaa.search.BWTCodeLines;
import cn.nuaa.search.Prof;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/websocket")
public class WebSocketTest {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<WebSocketTest> webSocketSet = new CopyOnWriteArraySet<WebSocketTest>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
    	
        System.out.println("来自客户端的消息:" + message);
        
        /*
        //群发消息
        for(WebSocketTest item: webSocketSet){
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
        */
        
    	
        System.out.println("来自客户端的消息:" + message);
        
        String temp = List2Json(getRecommendation(message));
        //String codes = temp.substring(1, temp.length()-1);
        String codes = temp;

        //群发消息
        for(WebSocketTest item: webSocketSet){
            try {
                item.sendMessage(codes);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
        System.out.println(codes);
        
        
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketTest.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketTest.onlineCount--;
    }
    
    public static List<String> getRecommendation(String code){
    	
		try {
			writeFileContent(Prof.filePath + "0.txt",new StringBuffer(code));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return BWTCodeLines.runningDemo2();
    }
    
    /*
    public static void main(String[] args){
    	List<String> list = getRecommendation("dsadasdas");
    	for(String s: list){
    		System.out.println(s);
    		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    	}
    	System.out.println(list.size());
    }
    */

    
	/**
	 * 写入文件;
	 * */
	private static boolean writeFileContent(String filepath, StringBuffer buffer) throws IOException {
		Boolean bool = false;
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		FileOutputStream fos = null;
		PrintWriter pw = null;
		try {
			File file = new File(filepath);// 文件路径(包括文件名称)

			fos = new FileOutputStream(file);
			pw = new PrintWriter(fos);
			pw.write(buffer.toString().toCharArray());
			pw.flush();
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 不要忘记关闭
			if (pw != null) {
				pw.close();
			}
			if (fos != null) {
				fos.close();
			}
			if (br != null) {
				br.close();
			}
			if (isr != null) {
				isr.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
		return bool;
	}
	
	public static String List2Json(List<String> codes){
        JSONArray json = new JSONArray();
        for(int i = 0; i < codes.size();i++){
        	String code = codes.get(i);
            JSONObject jo = new JSONObject();
            jo.put("codesnippet" + i, code); 
            json.put(jo);
        }
        System.out.println(json);
        return json.toString();
	}
}
