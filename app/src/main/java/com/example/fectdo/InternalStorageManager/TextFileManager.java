package com.example.fectdo.InternalStorageManager;

import android.content.Context;

import com.example.fectdo.Soalan.QuizManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class TextFileManager {

    public static void saveToFile(Context context, String filename, String data) {
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_APPEND);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos)) {
            outputStreamWriter.write(data +"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveToFile(Context context, String filename, QuizManager data) {
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos)) {
            for(int i=0 ; i<data.getSize() ; i++){
                outputStreamWriter.write(data.getQuestion(i) + "\n");
                outputStreamWriter.write("a) " +data.getChoice(i, 0) + "\n");
                outputStreamWriter.write("b) " +data.getChoice(i, 1) + "\n");
                outputStreamWriter.write("c) " +data.getChoice(i, 2) + "\n");
                outputStreamWriter.write("d) " +data.getChoice(i, 3) + "\n");
                outputStreamWriter.write("Correct choice: " +data.getCorrectAnswer(i) + "\n");
                outputStreamWriter.write("*******************************************\n\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(Context context, String fileName) {
        context.deleteFile(fileName);
    }
}
