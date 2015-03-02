import java.util.Scanner;

import java.util.ArrayList;
import java.util.Collections;

//file I/O
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;

//date
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;



public class WorkoutData {

  public static class Exercise {
    int weight;
    int reps;
    int time;
    double distance;

    public Exercise() {
      weight = 0;
      reps = 0;
      time = 0;
      distance = 0.0;
    }
  }

  static Scanner stdIn = new Scanner(System.in);

  public static void main (String[] args) throws IOException {
    //load exercise names from file
    ArrayList<String> names = getNames();
    //greeting:
    greet();
    getMainCommand(names);
  }



  //METHODS
  static boolean validSearchTerm(String query, ArrayList<String> names) {
    for (int i = 0; i < names.size(); i++) {
      if (names.get(i).contains(toCaps(query))) {
        return true;
      }
    }
    return false;
  }


  static void search(ArrayList<String> names, int branch) throws IOException{
    System.out.print("Please enter your search term -> ");
    stdIn.nextLine();
    String term = stdIn.nextLine();
    if (validSearchTerm(term, names)) {
      if (branch == 1) {
        giveFeedback(searchedArrayList(term, names));
      }
      else if (branch == 2) {
        addData(searchedArrayList(term, names));
      }
    }
    else {
      System.out.println("Invalid Search Term... \n");
      if (branch == 1) {
        giveFeedback(names);
      }
      else if (branch == 2) {
        addData(names);
      }
    }
  }


  static ArrayList<String> searchedArrayList(String query, ArrayList<String> names) {
    ArrayList<String> output = new ArrayList<String>();
    for (int i = 0; i < names.size(); i++) {
      if (names.get(i).contains(toCaps(query))) {
        output.add(names.get(i));
      }
    }
    return output;
  }


  static ArrayList<String> getNames() throws IOException{

    Scanner readNames = new Scanner(Paths.get("ExerciseNames.txt"));
    ArrayList<String> names = new ArrayList<String>();
    while(readNames.hasNext()) {
      names.add(toCaps(readNames.nextLine()));
    }
    readNames.close();
    Collections.sort(names);
    return names;
  }
  static void getMainCommand(ArrayList<String> names) throws IOException{
    System.out.println("Would you like to...");
    System.out.println("1: Review workout data");
    System.out.println("2: Add new workout data");
    int input = stdIn.nextInt();
    if (input == 1) {
      giveFeedback(names);
    }
    else if (input == 2) {
      addData(names);
    }
    else {
      System.out.println("Invalid Command...");
      getMainCommand(names);
    }
  }
  static void giveFeedback(ArrayList<String> names) throws IOException{
    System.out.println("What Exercise?");
    printExercisesMenu(names);
    int cmd = getInt(0, names.size());
    switch (cmd) {
      case 0:
        search(names, 1);
        break;
      default: {
        analysis(names.get(cmd - 1));
      }
    }
  }
  static void analysis(String name) throws IOException{
    Scanner fileIn = new Scanner(Paths.get(name + ".txt"));
    int dataType = fileIn.nextInt();

    ArrayList<Exercise> exercise = new ArrayList<Exercise>();

    switch (dataType) {
      case 1:
        while (fileIn.hasNext()) {
          //read time
          fileIn.next();
          //fill temp with data
          Exercise temp = new Exercise();
          temp.weight = fileIn.nextInt();
          temp.reps = fileIn.nextInt();
          //add temp to exercises
          exercise.add(temp);
        }
      case 2:
        while (fileIn.hasNext()) {
          //read time
          fileIn.next();
          //fill temp with data
          Exercise temp = new Exercise();
          temp.time = fileIn.nextInt();
          temp.distance = fileIn.nextDouble();
          //add temp to exercises
          exercise.add(temp);
        }
      case 3:
        while (fileIn.hasNext()) {
          //read time
          fileIn.next();
          //fill temp with data
          Exercise temp = new Exercise();
          temp.time = fileIn.nextInt();
          //add temp to exercises
          exercise.add(temp);
        }
      case 4:
        while (fileIn.hasNext()) {
          //read time
          fileIn.next();
          //fill temp with data
          Exercise temp = new Exercise();
          temp.reps = fileIn.nextInt();
          //add temp to exercises
          exercise.add(temp);
        }
    }
    System.out.println("There are " + exercise.size() + "entries for this exercise.");
    System.out.println("How many would you like to view?");

    int cmd2 = getInt(0, exercise.size());

    System.out.println("Confirmed. Viewing past " + cmd2 + " Days of " + name + ":");

    //ADD MORE FORMATTING HERE
    for (int i = (exercise.size() - cmd2); i < exercise.size(); i++) {
      switch (dataType) {
        case 1:
          System.out.println("Weight: " + exercise.get(i).weight + " Reps: " + exercise.get(i).reps);
          break;
        case 2:
          System.out.println("Time: " + timeFormatter(exercise.get(i).time) + " Distance: " + exercise.get(i).distance);
          break;
        case 3:
          System.out.println("Time: " + timeFormatter(exercise.get(i).time));
          break;
        case 4:
          System.out.println("Reps: " + exercise.get(i).reps);
          break;
      }
    }
    System.out.println("Press '1' to continue...");
    getInt(1,1);

    getMainCommand(getNames());
  }
  static String timeFormatter(int seconds) {
    String output = "";
    int minutes = 0;

    while (seconds >= 60) {
      minutes++;
      seconds -= 60;
    }

    if (minutes < 10) {
      output += ("0" + minutes);
    }
    else {
      output += minutes;
    }
    output+=":";

    if(seconds< 10) {
      output += "0";
    }
    output+= seconds;

    return output;
  }


  static void addData(ArrayList<String> names) throws IOException{
    System.out.println("What Exercise?");
    printExercisesMenu(names);
    System.out.println("-1: New Exercise");
    int cmd = getInt(-1, names.size());
    if (cmd == 0) {
      //SEARCH
      search(names, 2);
    }
    else if (cmd == -1) {
      //ADD NEW EXERCISE
      addExercise(names);
    }
    else {
      //ACTUALLY ADD DATA
      String data = getDataString(names.get(cmd - 1));
      writeData(names.get(cmd - 1), data);
      System.out.println("SAVED: " + data);

      //back to main menu
      ArrayList<String> newNames = getNames();
      getMainCommand(newNames);
    }
  }

  static void writeData(String name, String data) throws IOException, FileNotFoundException {
    File f = new File(name + ".txt");
    //if file exists
    if(f.exists() && !f.isDirectory()) {
      PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter((name + ".txt"), true)));
      out.println(data);
      out.close();
    }
    //file does not exist; it must be created
    else {
      PrintWriter writer = new PrintWriter((name +".txt"));
      writer.println(data);
      writer.close();
    }
  }

  static String getDataString(String name) throws IOException{
    Scanner fileIn = new Scanner(Paths.get(name + ".txt"));
    int dataType = fileIn.nextInt();

    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date date = new Date();

    String data = dateFormat.format(date) + " ";

    if (dataType == 1) {
      System.out.println("What was your weight? (lbs)");
      data += (String.valueOf(stdIn.nextInt()) + " ");
      System.out.println("How many reps did you do at that weight?");
      data += String.valueOf(stdIn.nextInt());
    }
    else if (dataType == 2) {
      System.out.println("What was your time? [Format: mm:ss]");
      String temp = stdIn.next();
      temp = String.valueOf(parseTime(temp));
      data += (temp + " ");

      System.out.println("What was your distance? (miles)");
      data += Double.toString(stdIn.nextDouble());
    }
    else if (dataType == 3) {
      System.out.println("What was your time? [Format: mm:ss]");
      String temp = stdIn.next();
      temp = String.valueOf(parseTime(temp));
      data += temp;
    }
    else if (dataType == 4) {
      System.out.println("How many reps?");
      data += String.valueOf(stdIn.nextInt());
    }
    return data;
  }
  static void addExercise(ArrayList<String> names) throws IOException {

    System.out.println("Exercise name?");
    stdIn.nextLine();
    String newName = stdIn.nextLine();

    System.out.println("What kind of data format does the exercise hold?");
    System.out.println("1: Weight and reps");
    System.out.println("2: Time and Distance");
    System.out.println("3: Time");
    System.out.println("4: reps");

    int cmd = getInt(1,4);

    //appendExerciseNames.txt
    FileWriter fw = new FileWriter("ExerciseNames.txt",true);
    fw.write(newName + "\n");//appends the string to the file
    fw.close();

    //creates new file to hold exercise data
    PrintWriter writer = new PrintWriter(toCaps(newName) + ".txt");
    //write data type:
    writer.write(String.valueOf(cmd) + "\n");
    writer.close();

    System.out.println("Confirmed. Saved " + newName + " to list of exercises.");

    //update names arrayList and go back to beginning
    ArrayList<String> newNames = getNames();
    getMainCommand(newNames);
  }
  static int getInt(int lowest, int highest) {
    int input = stdIn.nextInt();
    if (input >= lowest && input <= highest) {
      return input;
    }
    else {
      return getInt(lowest, highest);
    }
  }
  static void greet() {
    System.out.println("\nWelcome to WorkoutData!");
    System.out.println();
  }
  static String toCaps(String x) {
    return x.toUpperCase();
  }
  static void printExercisesMenu(ArrayList<String> names) {
    for (int i = 0; i < names.size(); i++) {
      System.out.println((i+1) + ": " + names.get(i));
    }
    System.out.println("0: Refine Search");
  }
  static int parseTime(String time) {
    String[] timeArray=time.split(":");

    int minutes=Integer.parseInt(timeArray[0]);
    int seconds=Integer.parseInt(timeArray[1]);

    seconds = minutes * 60 + seconds;
    return seconds;
  }
}
