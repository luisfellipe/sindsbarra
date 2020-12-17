package pdf;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class SaveFile extends Application{
	private File file= null;
	@Override
	public void start(Stage stage){
			stage.setTitle("Salvar arquivo");
			FileChooser fc = new FileChooser();
			fc.setTitle("AAAAAA");
			fc.getExtensionFilters().add(new ExtensionFilter("PDF Files", "*.pdf"));
			fc.setInitialFileName("Convenio.pdf");
			fc.showSaveDialog(stage);
			Button btnSave = new Button("Select File");
			btnSave.setOnAction(e -> {
				file = fc.showSaveDialog(stage);
			});
			VBox vbox = new VBox(btnSave);
			Scene scene = new Scene(vbox, 960, 600);
			stage.setScene(scene);
			stage.show();
	}
	
	public void saveFile() {
		
	}

}
