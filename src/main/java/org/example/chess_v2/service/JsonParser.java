package org.example.chess_v2.service;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import lombok.AllArgsConstructor;
import org.example.chess_v2.data.ChessExerciseData;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * JsonParser class is designed for parsing JSON files with chess exercises.
 */
public class JsonParser {
    /**
     * Method parses JSON files with chess exercises from specified folder.
     *
     * @return List of objects representing chess exercises.
     */
    public List<ChessExerciseData> parse(InputStream inputStream) {
        List<ChessExerciseData> allExercises = new CopyOnWriteArrayList<>();

        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             JsonReader jsonReader = new JsonReader(inputStreamReader)) {
            jsonReader.beginArray();
            Gson gson = new Gson();
            while (jsonReader.hasNext()) {
                ChessExerciseData exercise = gson.fromJson(jsonReader, ChessExerciseData.class);
                allExercises.add(exercise);
            }
            jsonReader.endArray();
        } catch (IOException e) {
            System.out.println("Error processing JSON input");
            e.printStackTrace();
        }
        return allExercises;
    }
    /**
     * Processes a JSON file with chess exercises and adds them to the list of all exercises.
     *
     * @param file JSON file to process.
     * @param allExercises List to store all exercises.
     */
    private void processFile(File file, List<Object> allExercises) {
        try (FileReader fileReader = new FileReader(file);
             JsonReader jsonReader = new JsonReader(fileReader)) {

            jsonReader.beginArray();

            Gson gson = new Gson();

            while (jsonReader.hasNext()) {
                ChessExerciseData exercise = gson.fromJson(jsonReader, ChessExerciseData.class);
                allExercises.add(exercise);
            }
            jsonReader.endArray();

        } catch (IOException e) {
            System.out.println("Error processing file: " + file.getName());
            e.printStackTrace();
        }
    }
}
