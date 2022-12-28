import java.io.*;



class Client_thread extends Thread {
    public void run()
    {
        try {
	    	while (true) {
                //scroll_pane_inside.getChildren().add(new Label("started server"));
	    		  final Socket clientSocket = server.accept();
                Platform.runLater(new Runnable() {
	    			@Override
	    			public void run() {
                        scroll_pane_inside.getChildren().add(new Label("Client "+ clientSocket.getRemoteSocketAddress()+ " connected"));
	    			}
	    		  });
                synchronized(outputs) {
                    outputs.add(new DataOutputStream(clientSocket.getOutputStream()));
                }
	    
                new Thread( new Runnable() {
                    public void run() {
                        try (DataInputStream incomingMessageReader = new DataInputStream(clientSocket.getInputStream())) {
		                	while (true) {
		                		String message_to_server = incomingMessageReader.readUTF();
                                System.out.println("Number of active threads from the given thread: " + Thread.activeCount()+"\n");
                                Platform.runLater(new Runnable() {
	    		                	@Override
	    		                	public void run() {
                                        scroll_pane_inside.getChildren().add(new Label("client "+clientSocket.getRemoteSocketAddress()+" sent : "+message_to_server));
                                    }
	    		                });
                                
                                synchronized (outputs) {
		                            for (DataOutputStream output : outputs) {
                                        output.writeUTF(message_to_server);
		                            }
                                }
		                	}
		                } catch (SocketException e) {
		                	//baseServer.clientDisconnected(this);
		                } catch (IOException e) {
		                	e.printStackTrace();
		                }
                    }
                }).start();
	    	}
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
    }
    public static void main(String[] args)
    {
        GFG g = new GFG(); // creating thread
        g.start(); // starting thread
    }
}