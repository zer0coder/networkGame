package game2017;

import game2017.Model.Player;
import game2017.Netcode.Client.LocalClient;
import game2017.StorageData.Queues.OutgoingMessageQueue;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Main_Client extends Application {

	private static final int size = 20;
	private static final int scene_height = size * 20 + 100;
	private static final int scene_width = size * 20 + 200;

	private static Image image_floor;
	private static Image image_wall;
	private static Image hero_right,hero_left,hero_up,hero_down;

	private static Player me;

	public static HashMap<String, Player> getPlayers() {
		return players;
	}

	//	private static List<Player> players = new ArrayList<Player>();
	private static HashMap<String, Player> players = new HashMap<>();

	private Label[][] fields;
	private TextArea scoreList;
	private String username;
	
	private  String[] board = {    // 20x20
			"wwwwwwwwwwwwwwwwwwww",
			"w        ww        w",
			"w w  w  www w  w  ww",
			"w w  w   ww w  w  ww",
			"w  w               w",
			"w w w w w w w  w  ww",
			"w w     www w  w  ww",
			"w w     w w w  w  ww",
			"w   w w  w  w  w   w",
			"w     w  w  w  w   w",
			"w ww ww        w  ww",
			"w  w w    w    w  ww",
			"w        ww w  w  ww",
			"w         w w  w  ww",
			"w        w     w  ww",
			"w  w              ww",
			"w  w www  w w  ww ww",
			"w w      ww w     ww",
			"w   w   ww  w      w",
			"wwwwwwwwwwwwwwwwwwww"
	};

	
	// -------------------------------------------
	// | Maze: (0,0)              | Score: (1,0) |
	// |-----------------------------------------|
	// | boardGrid (0,1)          | scorelist    |
	// |                          | (1,1)        |
	// -------------------------------------------

	@Override
	public void start(Stage primaryStage) {
		CreateClientToServerConnection(primaryStage);
	}

	private void Client(Stage primaryStage) {
		try {
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(0, 10, 0, 10));

			Text mazeLabel = new Text("Maze:");
			mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

			Text scoreLabel = new Text("Score:");
			scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

			scoreList = new TextArea();

			GridPane boardGrid = new GridPane();

			image_wall  = new Image(getClass().getResourceAsStream("Image/wall4.png"),size,size,false,false);
			image_floor = new Image(getClass().getResourceAsStream("Image/floor1.png"),size,size,false,false);

			hero_right  = new Image(getClass().getResourceAsStream("Image/heroRight.png"),size,size,false,false);
			hero_left   = new Image(getClass().getResourceAsStream("Image/heroLeft.png"),size,size,false,false);
			hero_up     = new Image(getClass().getResourceAsStream("Image/heroUp.png"),size,size,false,false);
			hero_down   = new Image(getClass().getResourceAsStream("Image/heroDown.png"),size,size,false,false);

			fields = new Label[20][20];
			for (int j=0; j<20; j++) {
				for (int i=0; i<20; i++) {
					switch (board[j].charAt(i)) {
						case 'w':
							fields[i][j] = new Label("", new ImageView(image_wall));
							break;
						case ' ':
							fields[i][j] = new Label("", new ImageView(image_floor));
							break;
						default: throw new Exception("Illegal field value: "+board[j].charAt(i) );
					}
					boardGrid.add(fields[i][j], i, j);
				}
			}
			scoreList.setEditable(false);


			grid.add(mazeLabel,  0, 0);
			grid.add(scoreLabel, 1, 0);
			grid.add(boardGrid,  0, 1);
			grid.add(scoreList,  1, 1);

			Scene scene = new Scene(grid,scene_width,scene_height);
			primaryStage.setScene(scene);
			primaryStage.show();

			scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				switch (event.getCode()) {
					case UP:    AddToOutgoingQueue(username + "," + 0 + "," + (-1) + "," + "up");    break;
					case DOWN:  AddToOutgoingQueue(username + "," + 0 + "," + (+1) + "," + "down");    break;
					case LEFT:  AddToOutgoingQueue(username + "," + (-1) + "," + (0) + "," + "left");    break;
					case RIGHT: AddToOutgoingQueue(username + "," + (+1) + "," + (0) + "," + "right");    break;
					default: break;
				}
			});

			scoreList.setText(getScoreList());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void CreatePlayer(String name, int startX, int startY) {
		Player player = new Player(name,startX,startY,"up");
		players.put(name, player);
		fields[startX][startY].setGraphic(new ImageView(hero_up));
		scoreList.setText(getScoreList());
	}

	public void playerMoved(String name, int delta_x, int delta_y, String direction) {
		me = players.get(name);
		me.setDirection(direction);
		int x = me.getXpos(),y = me.getYpos();

//		AddToOutgoingQueue(x + "," + y + "," + direction);

		if (board[y+delta_y].charAt(x+delta_x)=='w') {
			me.addPoints(-1);
		} 
		else {
			Player p = getPlayerAt(x+delta_x,y+delta_y);
			if (p!=null) {
              me.addPoints(10);
              p.addPoints(-10);
			} else {
				me.addPoints(1);
			
				fields[x][y].setGraphic(new ImageView(image_floor));
				x+=delta_x;
				y+=delta_y;

				if (direction.equals("right")) {
					fields[x][y].setGraphic(new ImageView(hero_right));
				};
				if (direction.equals("left")) {
					fields[x][y].setGraphic(new ImageView(hero_left));
				};
				if (direction.equals("up")) {
					fields[x][y].setGraphic(new ImageView(hero_up));
				};
				if (direction.equals("down")) {
					fields[x][y].setGraphic(new ImageView(hero_down));
				};

				me.setXpos(x);
				me.setYpos(y);
			}
		}
		scoreList.setText(getScoreList());
	}

	public String getScoreList() {
		StringBuffer b = new StringBuffer(100);
		for (Map.Entry<String,Player> p : players.entrySet()) {
			b.append(p.getValue()+"\r\n");
		}
		return b.toString();
	}

	public Player getPlayerAt(int x, int y) {
		for (Map.Entry<String,Player> pa : players.entrySet()) {
			Player p = pa.getValue();
			if (p.getXpos()==x && p.getYpos()==y) {
				return p;
			}
		}
		return null;
	}

	private void CreateClientToServerConnection(Stage stage) {
		Alert alert = ShowAlertMessage("Forbind til Server", "Indtast IP og/eller Port",
				"Indtast venligst IP og PORT");

		GridPane pane = new GridPane();
		pane.setMaxWidth(Double.MAX_VALUE);
		pane.setHgap(10);
		pane.setVgap(10);
		TextField local_IP_Field = new TextField("192.168.1.143");
		TextField local_PORT_Field = new TextField("50000");
		TextField local_USER_field = new TextField("User 1");

		local_IP_Field.setMaxWidth(Double.MAX_VALUE);
		local_PORT_Field.setMaxWidth(Double.MAX_VALUE);
		local_USER_field.setMaxWidth(Double.MAX_VALUE);

		pane.add(new Label("IP:"), 0, 0);
		pane.add(local_IP_Field,1,0);
		pane.add(new Label("PORT:"), 0, 1);
		pane.add(local_PORT_Field,1,1);
		pane.add(new Label("USER:"), 0, 2);
		pane.add(local_USER_field, 1, 2);

		alert.getDialogPane().setContent(pane);
		alert.setWidth(300);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
			String IP = local_IP_Field.getText();
			int PORT = Integer.parseInt(local_PORT_Field.getText());
			username = local_USER_field.getText();

			LocalClient localClient = new LocalClient(IP, PORT, this);
			localClient.start();

			Client(stage);
			AddToOutgoingQueue("NAME," + username + ",2,2");
		}
	}
	private Alert ShowAlertMessage(String title, String header, String context) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(context);
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

		return alert;
	}

	private BlockingQueue<String> outgoing = OutgoingMessageQueue.getOutgoingMessages();
	public void AddToOutgoingQueue(String command) {
		outgoing.add(command);
	}

	public static void main(String[] args) {
		launch(args);
	}
}

