package twitterAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import ca.ubc.ece.cpen221.mp3.graph.AdjacencyListGraph;
import ca.ubc.ece.cpen221.mp3.graph.AdjacencyMatrixGraph;
import ca.ubc.ece.cpen221.mp3.graph.Algorithms;
import ca.ubc.ece.cpen221.mp3.graph.NoPathException;
import ca.ubc.ece.cpen221.mp3.staff.Graph;
import ca.ubc.ece.cpen221.mp3.staff.Vertex;
/**
 * @author Cinnie Hsiung, Yuqing Du
 */
public class TwitterAnalysis {

    private final static String COMMON_INFLUENCERS = "commonInfluencers";
    private final static String NUM_RETWEETS = "numRetweets";

    private final static File TWITTER_DATA = new File("datasets/twitter.txt");
    private final static File QUERY_OUTPUT = new File("datasets/queryOutput.txt");
    private final static File QUERY_INPUT = new File("datasets/queryInput.txt");

    public static void main(String[] args) {

        Graph twitterData = new AdjacencyListGraph();

        // The FileInputStream to open and read from the file that
        // has the Twitter data.
        FileInputStream twitterStream;

        // Let us try to open the data file.
        // The file name is hardcoded, which is not elegant.
        // Suffices for now.
        try {
            twitterStream = new FileInputStream(TWITTER_DATA);
            System.out.println("Attempting to open Twitter Data file.");
        } catch (FileNotFoundException e) {
            // If, for some reason, the file was not found,
            // then throw an exception.
            // The file is however included in the git repo
            // so this should not happen.
            throw new RuntimeException(e);
        }

        // We have opened the file.
        // Let us try to read data.
        try {
            // We will use a BufferedReader to read the data from the file.
            System.out.println("Attempting to read Twitter Data file.");
            BufferedReader twitterReader = new BufferedReader(new InputStreamReader(twitterStream));

            // We will read one line at a time and then split it.
            // The format for twitter.txt is as follows:
            // - Column 1: user a
            // - Column 2: user b

            String line;
            int count = 1;
            // Read each line of the file until there is nothing left to read.
            while ((line = twitterReader.readLine()) != null) {
                System.out.println("Reading line " + count + " and splitting it into columns.");
                // Split the line into columns using the split( )
                // method for Strings.
                String[] columns = line.split(" -> ");

                // After the split, we should have the following (as Strings):
                // - columns[0] contains the user a
                // - columns[1] contains the user b,

                Vertex userA = new Vertex(columns[0]);
                Vertex userB = new Vertex(columns[1]);

                twitterData.addVertex(userA);
                twitterData.addVertex(userB);
                twitterData.addEdge(userA, userB);

                count++;

            }

            System.out.println("Finished reading Twitter Data file. Now closing the file.");
            twitterReader.close();
            twitterStream.close();
        } catch (Exception e) {
            // If, for any reason, we had some problems reading data...
            throw new RuntimeException(e);
        }

        // READING LIST OF QUERIES

        FileInputStream queryInputStream;
        FileOutputStream queryOutputStream;
        File input = QUERY_INPUT;
        File output = QUERY_OUTPUT;

        // Let us try to open the data file.
        try {
            System.out.println("Now opening the query input and output files.");
            queryInputStream = new FileInputStream(input);
            queryOutputStream = new FileOutputStream(output);

            if (!output.exists()) {
                output.createNewFile();
                System.out.println("The output file did not exist. We created a new one.");
            }

        } catch (FileNotFoundException e) {
            // If, for some reason, the file was not found,
            // then throw an exception.
            // This should not happen.
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            // We will use a BufferedReader to read the data from the file.
            System.out.println("Attempting to read the query input file.");
            BufferedReader queryInputReader = new BufferedReader(new InputStreamReader(queryInputStream));
            BufferedWriter queryOutputWriter = new BufferedWriter(new OutputStreamWriter(queryOutputStream));

            // We will read one line at a time and then split it.
            // The format for twitter.txt is as follows:
            // - Column 1: query type
            // - Column 2: user a
            // - Column 3: user b
            // - Column 4: question mark (may or may not be present)

            String line;

            Graph visitedQueryCommonInfluencers = new AdjacencyListGraph();
            Graph visitedQueryNumRetweets = new AdjacencyListGraph();

            // Read each line of the file until there is nothing left to read.
            while ((line = queryInputReader.readLine()) != null) {
                System.out.println("Reading each query and splitting the necessary information.");

                // Split the line into columns using the split( )
                // method for Strings.
                String[] columns = line.split(" ");

                // After the split, we should have the following (as Strings):
                // - columns[0] contains the query
                // - columns[1] contains the user a
                // - columns[2] contains the user b
                // - columns[3] contains the question mark

                String question = new String();
                String query = columns[0];
                Vertex userAA = new Vertex(columns[1]);
                Vertex userBB = new Vertex(columns[2]);

                if (columns.length == 4)
                    question = columns[3];

                if (question.equals("?")) {
                    System.out.println("This was a valid query. Beginning Algorithm.");

                    if (!visitedQueryCommonInfluencers.getVertices().contains(userAA)) {
                        visitedQueryCommonInfluencers.addVertex(userAA);
                        System.out.println(
                                "The 1st vertex from the query input was not in the visitedQueryCommonInfluencersGraph. Adding it.");
                    }

                    if (!visitedQueryCommonInfluencers.getVertices().contains(userBB)) {
                        visitedQueryCommonInfluencers.addVertex(userBB);
                        System.out.println(
                                "The 2nd vertex from the query input was not in the visitedQueryCommonInfluencersGraph. Adding it.");
                    }

                    if (!visitedQueryNumRetweets.getVertices().contains(userAA)) {
                        visitedQueryNumRetweets.addVertex(userAA);
                        System.out.println(
                                "The 1st vertex from the query input was not in the visitedQueryNumRetweetsGraph. Adding it.");
                    }

                    if (!visitedQueryNumRetweets.getVertices().contains(userBB)) {
                        visitedQueryNumRetweets.addVertex(userBB);
                        System.out.println(
                                "The 2nd vertex from the query input was not in the visitedQueryNumRetweetsGraph. Adding it.");
                    }

                    if (query.equals(COMMON_INFLUENCERS) && !visitedQueryCommonInfluencers.edgeExists(userAA, userBB)) {
                        System.out.println("The query was for common influencers. Beginning Algorithm.");

                        queryOutputWriter.write("query: " + query + " " + userAA.toString() + " " + userBB.toString());
                        queryOutputWriter.newLine();
                        queryOutputWriter.write("<result>");
                        queryOutputWriter.newLine();

                        List<Vertex> testList = new LinkedList<Vertex>();
                        testList.addAll(Algorithms.commonDownstreamVertices(twitterData, userAA, userBB));
                        System.out.println("Common influencers have been found. Beginning to write to output file.");

                        if (testList.isEmpty()) {
                            queryOutputWriter.write("0");
                        } else {
                            for (Vertex currentVertex : testList) {

                                queryOutputWriter.write(currentVertex.toString());
                                queryOutputWriter.newLine();
                            }
                        }

                        queryOutputWriter.write("</result>");
                        queryOutputWriter.newLine();
                        queryOutputWriter.newLine();

                        System.out.println("Finished printing to output file. Adding the analyzed edge to the graph.");
                        visitedQueryCommonInfluencers.addEdge(userAA, userBB);

                    }

                    if (query.equals(NUM_RETWEETS) && !visitedQueryNumRetweets.edgeExists(userAA, userBB)) {
                        System.out.println("The query was for number of retweets. Beginning Algorithm.");

                        queryOutputWriter.write("query: " + query + " " + userAA.toString() + " " + userBB.toString());
                        queryOutputWriter.newLine();
                        queryOutputWriter.write("<result>");
                        queryOutputWriter.newLine();

                        try {
                            int toWrite = Algorithms.shortestDistance(twitterData, userBB, userAA);
                            queryOutputWriter.write(String.valueOf(toWrite));
                        } catch (NoPathException e) {
                            queryOutputWriter.write("Infinity.");
                        }

                        System.out.println("The shortest distance has been found. Writing to output file.");

                        queryOutputWriter.newLine();
                        queryOutputWriter.write("</result>");
                        queryOutputWriter.newLine();
                        queryOutputWriter.newLine();

                        System.out.println("Finished printing to output file. Adding the analyzed edge to the graph.");
                        visitedQueryNumRetweets.addEdge(userAA, userBB);

                    }

                }

            }

            System.out.println("Finished analyzing all the queries. Now closing the files.");
            queryInputReader.close();
            queryInputStream.close();

            queryOutputWriter.close();
            queryOutputStream.close();
        } catch (Exception e) {
            // If, for any reason, we had some problems reading data...
            throw new RuntimeException(e);
        }
        System.out.println("Finished.");
    }
}
