
import java.net.*;
import java.io.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ChatC extends Application {

	public static int PORT;
	public static String HOST;
	public static BufferedReader key;
	public static String name;
	public static Socket sc;
	@FXML
	public static TextField mesbox;
	@FXML
	public static TextArea message;

	public void init(){
		try{

			key = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("ポート番号を入力してください");
			PORT = Integer.parseInt(key.readLine());

			System.out.println("ホスト名を入力してください");
			HOST = key.readLine();

			System.out.println("相手に表示する名前を入力してください");
			name = key.readLine();

			sc = new Socket(HOST,PORT);
			System.out.println("接続しました");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start(Stage stage) throws Exception{

		Jusin j = new Jusin();
		j.start();

		stage.setTitle("チャットクライアント");
		stage.setWidth(520);
		stage.setHeight(500);
		stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));

		VBox root = (VBox)FXMLLoader.load(getClass().getResource("root.fxml"));
		stage.setScene(new Scene(root));

		stage.show();


	}
	@FXML
	public void sousin(){
		try{
			PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sc.getOutputStream())));

			String me = mesbox.getText();

			pw.println(name+">"+me);
			pw.flush();

			mesbox.setText("");

			pw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
class Jusin extends Thread {
	public void run(){
		while(true){
			try{
				ChatC cc = new ChatC();
				BufferedReader br = new BufferedReader(new InputStreamReader(cc.sc.getInputStream()));

				String you = cc.message.getText()+"\n"+br.readLine()+"\n";
				Platform.runLater(new Runnable() {
					@Override
					public void run(){
						cc.message.setText(you);
					}
				});
			}catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}
}