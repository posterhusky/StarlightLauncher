package net.vanolex.tasks


import net.vanolex.Window
import net.vanolex.epicapi.AsyncTask
import javax.swing.JFileChooser

class SelectFileTask: AsyncTask() {
    override suspend fun task() {
        val chooser = JFileChooser();
        chooser.setCurrentDirectory(java.io.File("."));
        chooser.setDialogTitle("Title");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        //
        if (chooser.showOpenDialog(Window) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): "
                    +  chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : "
                    +  chooser.getSelectedFile());
        }
        else {
            System.out.println("No Selection ");
        }
    }
}