package game2017;

import game2017.Model.MType;
import game2017.Model.Message;
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

import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Main_Client extends Application {

	private static final int size = 20;
	private static final int scene_height = size * 20 + 100;
	private static final int scene_width = size * 20 + 200;

	private static Image image_floor;
	private static Image image_wall;
	private static Image hero_right,hero_left,hero_up,hero_down;

//	private static Player player;

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
					case UP:    SendMoveRequest(0,-1, "up");    break;
					case DOWN:  SendMoveRequest(0,+1, "down");    break;
					case LEFT:  SendMoveRequest(-1,0, "left");    break;
					case RIGHT: SendMoveRequest(+1,0, "right");    break;
					default: break;
				}
			});

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private BlockingQueue<Message> outgoing = OutgoingMessageQueue.getOutgoingMessages();

	public void SendNewPlayerData(String user) {
		Message message = new Message(user);
		message.setType(MType.DATA);
		outgoing.add(message);
	}

	public void SendMoveRequest(int x, int y, String direction) {
		Message message = new Message(username);
		message.setXpos(x);
		message.setYpos(y);
		message.setDirection(direction);
		message.setType(MType.MOVE);
		outgoing.add(message);
	}

	public void CreatePlayer(int startX, int startY) {
		fields[startX][startY].setGraphic(new ImageView(hero_up));
	}

	public void playerMoved(Player player, String direction) {
		player.setDirection(direction);
		int x = player.getXpos(),y = player.getYpos();
		int prev_x = player.getPrev_xpos(), prev_y = player.getPrev_ypos();

		fields[prev_x][prev_y].setGraphic(new ImageView(image_floor));

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

		player.setXpos(x);
		player.setYpos(y);
	}

	public void setScoreList(String score) {
		scoreList.setText(score);
	}

	private void CreateClientToServerConnection(Stage stage) {
		Alert alert = ShowAlertMessage("Forbind til Server", "Indtast IP og/eller Port",
				"Indtast venligst IP og PORT");

		GridPane pane = new GridPane();
		pane.setMaxWidth(Double.MAX_VALUE);
		pane.setHgap(10);
		pane.setVgap(10);
		TextField local_IP_Field = new TextField("192.168.0.13");
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
			SendNewPlayerData(username);
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

	public void setMap(String[] map){
	    this.board = map;
    }

	public static void main(String[] args) {
		launch(args);
	}

}

