package renamer.app;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;

import clib.common.filesystem.CDirectory;
import clib.common.filesystem.CFileElement;
import clib.common.filesystem.CFileSystem;
import clib.view.dnd.CFileDropInDataTransferHandler;
import clib.view.dnd.ICFileDroppedListener;

/*
 * プログラム名： 
 * 作成者： 
 */

public class RenamerViewerStart extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 起動処理
	public static void main(String[] args) {
		RenamerViewerStart renamerFrame = new RenamerViewerStart();
		renamerFrame.main();
	}

	// メイン
	void main() {
		openframe();
	}

	public void openframe() {
		JLabel label = new JLabel("drop unzip moodle folder");
		add(label, BorderLayout.NORTH);
		CFileDropInDataTransferHandler.set(this, new DroppedListener());

		setBounds(0, 0, 200, 200);

		setVisible(true);
	}

	class DroppedListener implements ICFileDroppedListener {

		@Override
		public void fileDropped(List<File> files) {
			for (File file : files) {
				try {
					CDirectory dir = (CDirectory) CFileSystem
							.convertToCFile(file);
					FileReader filereader = new FileReader("./prog2013.csv");
					BufferedReader bufferreader = new BufferedReader(filereader);
					String[] userdata;
					String line = "";

					while ((line = bufferreader.readLine()) != null) {
						userdata = line.split(",");

						for (CFileElement cfile : dir.getChildren()) {
							String fileCreaterName = cfile.getNameByString()
									.split("_")[0];
							if (userdata[0].equals(fileCreaterName)) {
								String extantion = cfile.getNameByString()
										.split("\\.")[1];
								cfile.renameTo(userdata[1] + "." + extantion);
							}
						}
					}

					// for (CFileElement cfile : dir.getChildren()) {
					// String filename = cfile.getNameByString();
					// String seg = filename.substring(0,
					// filename.length() - 4);
					// cfile.renameTo(seg + "-2" + ".zip");
					// }

					bufferreader.close();
					filereader.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			add(new JLabel("Compiled!"), BorderLayout.CENTER);
			validate();
			System.out.println("Finished");
		}
	}
}
