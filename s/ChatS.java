
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

public class ChatS extends Application {

	public static int PORT;
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

			System.out.println("相手に表示する名前を入力してください");
			name = key.readLine();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void start(Stage stage)throws Exception{

		Connect cn = new Connect();
		cn.start();

		Jusin j = new Jusin();
		j.start();

		stage.setTitle("チャットサーバー");
		stage.setWidth(520);
		stage.setHeight(500);
		stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));

		FXMLLoader load = new FXMLLoader(getClass().getResource("root.fxml"));
		load.setController(this);

		VBox root = load.load();
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
class Connect extends Thread {

	public void run(){
		try{
			ChatS cs = new ChatS();
			ServerSocket ss = new ServerSocket(cs.PORT);

			System.out.println("待機します");

			while(true){
				try{
					cs.sc = ss.accept();

					System.out.println("接続しました");

				}catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
class Jusin extends Thread {

	public void run(){

		while(true){
			if (ChatS.sc!=null) {
				try{
					ChatS cs = new ChatS();
					BufferedReader br = new BufferedReader(new InputStreamReader(cs.sc.getInputStream()));

					String you = cs.message.getText()+"\n"+br.readLine()+"\n";
					Platform.runLater(new Runnable() {
						@Override
						public void run(){
							cs.message.setText(you);
						}
					});
					br.close();
				}catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}
}