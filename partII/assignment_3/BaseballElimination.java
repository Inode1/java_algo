import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;

public class BaseballElimination {
    // input data
    private Hashtable<String, Integer> teams;
    private int[] wins;
    private int[] losses;
    private int[] remainingGames;
    private int[] gamesLeft;
    private int   size;
    // internal data
    private FlowNetwork network;
    private ArrayList<Integer> eliminatedTeams;
    // key wins, value team number
    private TreeMap<Integer, Integer> sortedBestTeams = new TreeMap<Integer, Integer>();

    public BaseballElimination(String filename) {                    // create a baseball division from given filename in format specified below
        In file    = new In(filename);
        size       = file.readInt();
        teams      = new Hashtable<String, Integer>(size);
        wins       = new int[size];
        losses     = new int[size];
        remainingGames  = new int[size];
        gamesLeft       = new int[size * size];

        // read input data        
        int i = 0;
        while (i != size) {
            teams.put(file.readString(), i);
            wins[i]           = file.readInt();
            sortedBestTeams.put(wins[i], i);
            losses[i]         = file.readInt();
            remainingGames[i] = file.readInt();
            for (int j = 0; j < size; ++j)
            {
                gamesLeft[i * size + j] = file.readInt();
            }
            ++i;
        }
        network = new FlowNetwork(size);



    }
    
    public              int numberOfTeams() {                        // number of teams
        return size;
    }
    
    public Iterable<String> teams() {                                // all teams
        return teams.keySet();
    }
    
    public              int wins(String team) {                      // number of wins for given team
        return wins[checkTeams(team)];
    }
    
    public              int losses(String team) {                    // number of losses for given team
        return losses[checkTeams(team)];    
    }
    
    public              int remaining(String team) {                 // number of remaining games for given team
        return remainingGames[checkTeams(team)];
    }
    
    public              int against(String team1, String team2) {    // number of remaining games between team1 and team2
        int lhs = checkTeams(team1);
        int rhs = checkTeams(team2);
        return wins[lhs * size + rhs];
    }
    
    public          boolean isEliminated(String team) {              // is given team eliminated?
        return checkTrivialCase(teams.get(team));
    }
    
    public Iterable<String> certificateOfElimination(String team) {  // subset R of teams that eliminates given team; null if not eliminated
        return null;
    }

    private int checkTeams(String team) {
        Integer i = teams.get(team);
        if (i == null) {
            throw new java.lang.IllegalArgumentException();
        }
        return i;
    }

    private boolean checkTrivialCase(int teamNumber) {
        NavigableMap<Integer, Integer> teamsIsBetter = sortedBestTeams.tailMap(wins[teamNumber] + remainingGames[teamNumber]
                                                        , false);
        if (teamsIsBetter.isEmpty()) {
            return false;
        }
        System.out.printf("team: %d\n", teamNumber);   
        for(Map.Entry<Integer, Integer> entry: teamsIsBetter.entrySet())
        {
            System.out.println("teams: " + entry.getValue());
        }
/*        Iterator it = teamsIsBetter.iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
        }*/
        return false;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}