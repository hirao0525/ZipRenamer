package renamer.app;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import clib.common.filesystem.CDirectory;
import clib.common.filesystem.CFileElement;
import clib.common.filesystem.CFileSystem;
import clib.view.dnd.CFileDropInDataTransferHandler;
import clib.view.dnd.ICFileDroppedListener;

/*
 * プログラム名： moodleから一括で落としたzipファイルをcsvファイルから読み込んだ情報に従って名前をき換えるプログラム
 * 作成者： motoki hirao
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);
	}

	class DroppedListener implements ICFileDroppedListener {

		JLabel label = new JLabel();

		@Override
		public void fileDropped(List<File> files) {

			label.setText("");
			add(label, BorderLayout.CENTER);
			validate();

			String footerText = JOptionPane.showInputDialog(
					RenamerViewerStart.this,
					"フッター設定（例：入力\"-1\"→出力ファイル：\"名前-1．拡張子\"");

			for (File file : files) {
				label.setText("");
				validate();

				try {
					CDirectory dir = (CDirectory) CFileSystem
							.convertToCFile(file);
					FileReader filereader = new FileReader("./member.csv");
					BufferedReader bufferreader = new BufferedReader(filereader);
					String[] userdata;
					String line = "";

					while ((line = bufferreader.readLine()) != null) {
						userdata = line.split(",");

						for (CFileElement cfile : dir.getChildren()) {
							// 前方一致なら
							if (cfile.getNameByString().indexOf(userdata[0]) >= 0) {
								String extantion = cfile.getNameByString()
										.split("\\.")[1];
								cfile.renameTo(userdata[1] + footerText + "."
										+ extantion);
							}
						}
					}

					// ハイフンで区切ってリネーム(for It's class)
					// for (CFileElement cfile : dir.getChildren()) {
					// String[] userdatas = cfile.getNameByString().split("-");
					// String extantion =
					// cfile.getNameByString().split("\\.")[1];
					// cfile.renameTo(userdatas[1] + "-" + userdatas[2]
					// + footerText + "." + extantion);
					// }

					// csv作成用
					// for (CFileElement cfile : dir.getChildren()) {
					// String[] userdatas = cfile.getNameByString().split("-");
					// System.out.println(userdatas[0] + "-" + userdatas[1]);
					// }

					bufferreader.close();
					filereader.close();

				} catch (Exception ex) {
					ex.printStackTrace();
				}

				label.setText(file.getName() + "Compiled!");
				validate();
				System.out.println(file.getName() + "Finished");
			}
		}
	}
}
